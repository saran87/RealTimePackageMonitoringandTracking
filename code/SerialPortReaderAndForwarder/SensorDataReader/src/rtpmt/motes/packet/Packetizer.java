// $Id: Packetizer.java,v 1.8 2010-06-29 22:07:41 scipio Exp $

/*									tab:4
 * Copyright (c) 2000-2003 The Regents of the University  of California.  
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the
 *   distribution.
 * - Neither the name of the University of California nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (c) 2002-2003 Intel Corporation
 * All rights reserved.
 *
 * This file is distributed under the terms in the attached INTEL-LICENSE     
 * file. If you do not find these files, copies can be found by writing to
 * Intel Research Berkeley, 2150 Shattuck Avenue, Suite 1300, Berkeley, CA, 
 * 94704.  Attention:  Intel License Inquiry.
 */
package rtpmt.motes.packet;

import rtpmt.sensor.util.Packet;
import rtpmt.sensor.util.Utils;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import rtpmt.location.tracker.Location;
import rtpmt.location.tracker.LocationTracker;
import rtpmt.network.packet.SensorMessage.SensorInformation;
import rtpmt.sensor.reader.SerialPortInterface;

/**
 * The Packetizer class implements the new mote-PC protocol, using a ByteSource
 * for low-level I/O
 */
public class Packetizer extends AbstractSource implements Runnable {
    /*
     * Protocol inspired by, but not identical to, RFC 1663. There is
     * currently no protocol establishment phase, and a single byte
     * ("packet type") to identify the kind/target/etc of each packet.
     * 
     * The protocol is really, really not aiming for high performance.
     * 
     * There is however a hook for future extensions: implementations
     * are required to answer all unknown packet types with a P_UNKNOWN
     * packet.
     * 
     * To summarise the protocol: 
     * - the two sides (A & B) are connected by a (potentially
     *   unreliable) byte stream
     *
     * - the two sides exchange packets framed by 0x7e (SYNC_BYTE) bytes
     *
     * - each packet has the form 
     *     <packet type> <data bytes 1..n> <16-bit crc> 
     *   where the crc (see net.tinyos.util.Crc) covers the packet type
     *   and bytes 1..n
     *
     * - bytes can be escaped by preceding them with 0x7d and their
     *   value xored with 0x20; 0x7d and 0x7e bytes must be escaped,
     *   0x00 - 0x1f and 0x80-0x9f may be optionally escaped
     *
     * - There are currently 5 packet types: 
     *   P_PACKET_NO_ACK: A user-packet, with no ack required
     *   P_PACKET_ACK: A user-packet with a prefix byte, ack
     *   required. The receiver must send a P_ACK packet with the 
     *   prefix byte as its contents.  
     *   P_ACK: ack for a previous P_PACKET_ACK packet 
     *   P_UNKNOWN: unknown packet type received. On reception of an
     *   unknown packet type, the receicer must send a P_UNKNOWN packet,
     *   the first byte must be the unknown packet type. 
     *
     * - Packets that are greater than a (private) MTU are silently
     *   dropped.
     */

    final static boolean DEBUG = true;
    final static int[] FRAME_SYNC = {170, 255, 85};
    final static int SYNC_BYTE = 126;
    final static int ESCAPE_BYTE = 125;
    final static int P_ACK = 67;
    final static int P_REGISTRATION = 153;
    final static int P_SERVICE_REQUEST = 255;
    final static int P_SERVICE_RESPONSE = 0;
    final static int P_SERVICE_REPORT_RATE = 254;
    final static int P_SERVICE_UPDATE_THRESHOLD = 253;
    final static int P_TIME_SYNC = 160;
    final static int P_UPDATE = 1;
     final static int P_UPDATE_THRESHOLD = 2;
    final static int P_UNKNOWN = 255;
    final static int MTU = 1000;
    final static int ACK_TIMEOUT = 1000; // in milliseconds
    private final SerialPortInterface port;
    private boolean inSync;
    private final byte[] receiveBuffer = new byte[MTU];
    private final int seqNo;
    // Packets are received by a separate thread and placed in a
    // per-packet-type queue. If received[x] is null, then x is an
    // unknown protocol (but P_UNKNOWN and P_PACKET_ACK are handled
    // specially)
    private final Thread reader;
    private final LinkedList[] received;
    private final ConcurrentHashMap<String, Packet> partialData;
    private final HashMap<Integer, Long> sensorList;
    

    /**
     * Packetizers are built using the makeXXX methods in BuildSource
     * @param name
     * @param _inputPort
     */
    public Packetizer(String name, SerialPortInterface _inputPort) {
        super(name);
        this.port = _inputPort;
        inSync = false;
        seqNo = 13;
        reader = new Thread(this);
        received = new LinkedList[256];
        received[P_ACK] = new LinkedList();
        received[P_UPDATE] = new LinkedList();
        partialData = new ConcurrentHashMap<String, Packet>();
        sensorList = new HashMap<Integer, Long>();
    }

    @Override
    synchronized public void open(Messenger messages) throws IOException {
        super.open(messages);
        if (!reader.isAlive()) {
            reader.start();
        }
    }

    @Override
    protected void openSource() throws IOException {
        //io.open();
    }

    @Override
    protected void closeSource() {
        try {
            port.close();
        } catch (IOException ex) {
            Logger.getLogger(Packetizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected Packet readProtocolPacket(int packetType, long deadline)
            throws IOException {
        LinkedList inPackets = received[packetType];

        // Wait for a packet on inPackets
        synchronized (inPackets) {
            while (inPackets.isEmpty()) {
                long now = System.currentTimeMillis();
                if (deadline != 0 && now >= deadline) {
                    return null;
                }
                try {
                    inPackets.wait(deadline != 0 ? deadline - now : 0);
                } catch (InterruptedException e) {
                    throw new IOException("interrupted");
                }
            }
            return (Packet) inPackets.removeFirst();
        }
    }

    // Place a packet in its packet queue, or reject unknown packet
    // types (which don't have a queue)
    protected void pushProtocolPacket(int packetType, Packet packet) {
        LinkedList inPackets = received[packetType];

        if (inPackets != null) {

            synchronized (inPackets) {
                inPackets.add(packet);
                inPackets.notify();
            }

        } else if (packetType != P_UNKNOWN) {
            message(name + ": ignoring unknown packet type 0x"
                    + Integer.toHexString(packetType));
        }
    }

    private void handlePartialPackets(Packet packet) {
        if (packet.isPartialPacket()) {
            String key = packet.uniqueId();
            System.out.println("Key:"+ key);
            if (partialData.containsKey(key)) {
                Packet partialpacket = partialData.get(key);
                partialpacket.isCompletePacket(packet);
                partialpacket.combinePacket(partialpacket);
                System.out.println("I am Cobining the packets" + packet.getDataLength());
                pushProtocolPacket(P_UPDATE, partialData.remove(key));
               
            } else {
                partialData.put(key, packet);
            }
        }
    }

    /**
     * gets the packet from the queue and return it
     *
     * @return Packet object
     * @throws IOException
     */
    @Override
    protected Packet readSourcePacket() throws IOException {
        // Packetizer packet format is identical to PacketSource's
        for (;;) {
            Packet packet = readProtocolPacket(P_UPDATE, 0);

            return packet;
        }
    }

    /**
     * gets the Non Acknowledgment packet from the queue and return it
     *
     * @return array of bytes
     * @throws IOException
     */
    @Override
    protected SensorInformation readFormattedPacket() throws IOException {
        // Packetizer packet format is identical to PacketSource's
        for (;;) {

            Packet packet = readProtocolPacket(P_UPDATE, 0);
            if (DEBUG) {
                Dump.dump(packet);
            }

            SensorInformation.Builder message = SensorInformation.newBuilder();
            long packageId = sensorList.containsKey(packet.NodeId) ? sensorList.get(packet.NodeId) : 1;
            message.setDeviceId(String.valueOf(packageId));
       
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            message.setTimeStamp(packet.getTimeStamp());

            SensorInformation.Sensor.Builder sensor = SensorInformation.Sensor.newBuilder();

            if (packet.isTemperature()) {
                sensor.setSensorId("1");
                sensor.setSensorUnit("F");
                sensor.setSensorType(SensorInformation.SensorType.TEMPERATURE);
                sensor.setSensorValue(String.valueOf(packet.getValue()));
                //sensor.setSensorValue("80");
                message.addSensors(sensor);
            } else if (packet.isHumidty()) {
                sensor = SensorInformation.Sensor.newBuilder();
                sensor.setSensorId("2");
                sensor.setSensorUnit("%RH");
                sensor.setSensorType(SensorInformation.SensorType.HUMIDITY);
                System.out.println(packet.getHumidity());
                sensor.setSensorValue(String.valueOf((packet.getHumidity())));
                message.addSensors(sensor);
            } else if (packet.isVibration()) {
                sensor = SensorInformation.Sensor.newBuilder();
                sensor.setSensorId("3");
                sensor.setSensorUnit("g");
                if (packet.isX()) {
                    System.out.println("X=");
                    sensor.setSensorType(SensorInformation.SensorType.VIBRATIONX);
                } else if (packet.isY()) {
                    System.out.println("Y=");
                    sensor.setSensorType(SensorInformation.SensorType.VIBRATIONY);
                } else if (packet.isZ()) {
                    System.out.println("Z=");
                    sensor.setSensorType(SensorInformation.SensorType.VIBRATIONZ);
                }
                System.out.print(packet.getVibration());
                sensor.setSensorValue(String.valueOf((packet.getVibration())));
                message.addSensors(sensor);
            } else if (packet.isShock()) {
                sensor = SensorInformation.Sensor.newBuilder();
                sensor.setSensorId("4");
                sensor.setSensorUnit("g");
                if (packet.isX()) {
                    sensor.setSensorType(SensorInformation.SensorType.SHOCKX);
                } else if (packet.isY()) {
                    sensor.setSensorType(SensorInformation.SensorType.SHOCKY);
                } else if (packet.isZ()) {
                    sensor.setSensorType(SensorInformation.SensorType.SHOCKZ);
                }
                System.out.println(packet.getShock());
                sensor.setSensorValue(String.valueOf((packet.getShock())));
                message.addSensors(sensor);
            }

            SensorInformation.LocationInformation.Builder location = SensorInformation.LocationInformation.newBuilder();
            Location loc = LocationTracker.getLocation();
            if (loc != null) {
                location.setLatitude(loc.getLatitude());
                location.setLongitude(loc.getLongitude());
                message.setLocation(location);
            } else {
                location.setLatitude(43.084603);
                location.setLongitude(-77.680312);
                message.setLocation(location);
            }
            return message.build();
        }
    }

    protected Packet getPacket() throws IOException {
        // Packetizer packet format is identical to PacketSource's
        for (;;) {
            Packet rawpacket = readProtocolPacket(P_UPDATE, 0);
            return rawpacket;
        }
    }

    private static final byte dummyPacket[] = new byte[0];

    @Override
    public void run() {
        try {
            for (;;) {
                byte[] packet = readFramedPacket();
                int packetType = packet[0] & 0xff;
                int nodeId = (packet[1] & 0xff) << 8 | (packet[2] & 0xff);

                //int pdataOffset = 0;
                if (packetType == P_REGISTRATION) {

                    //TODO Store nodeid, 64bit id (MAC) in hashtable
                    Long macId ;
                    macId = (long)(packet[12] & 0xff) << 56 | 
                            (packet[11] & 0xff)<< 48 | (packet[10] & 0xff) << 40 | (packet[9] & 0xff) << 32
                            |  (packet[8] & 0xff)<< 24 | (packet[7] & 0xff) << 16
                            | (packet[6] & 0xff) << 8 | (packet[5] & 0xff);
                    Integer shortId = nodeId;
                    sensorList.put(shortId, macId);

                    sendServiceRequest(nodeId); // Sending the Service Request
                    sendTimeSyncPacket(nodeId);
                    System.out.println("Service Request sent!");

                    // And merge with un-acked packets
                    packetType = P_UPDATE;
                } else if (packetType == P_SERVICE_RESPONSE) {
                    //Log.i("Packetizer", "Service Response Received");
                    //System.out.println("Sending Threshold for node: "+count);
                    //sendReportRate(nodeId);
                    //sendThresholdRequest(nodeId);

                    System.out.println("Service Response Recieved Sent");

                } else if (packetType == P_UPDATE || packetType == P_UPDATE_THRESHOLD) {
                    packetType = P_UPDATE;
                    Packet packetHelper = new Packet(packet);
                    if(packetHelper.isPartialPacket()){
                        handlePartialPackets(packetHelper);
                    }else{
                        pushProtocolPacket(packetType, packetHelper);
                    }
                }
            }
        } catch (IOException e) {
        }
    }

    /*
     * TINY OS Packet reader
     */
    // Read system-level packet. If inSync is false, we currently don't
    // have sync
    private byte[] readFramedPacket() throws IOException {

        int count = 0;
        boolean isLength = false;
        int payLoad = 0;
        inSync = false;
        byte[] syncFrame = new byte[FRAME_SYNC.length];

        for (;;) {
            if (!inSync) {
                message(name + ": resynchronising");
                // re-synchronise 

                int b = port.read() & 0xff;

                while (b != 170) {

                    b = port.read() & 0xff;
                    System.out.println(b);
                }
                System.out.println("count:" + count + "InSync:" + inSync);
                if (count >= MTU) {
                    // PacketHelper too long, give up and try to resync
                    message(name + ": packet too long");
                    inSync = false;
                    count = 0;
                    continue;
                }
                syncFrame[count++] = (byte) (b & 0xff);

                while (count < FRAME_SYNC.length) {
                    b = port.read();
                    syncFrame[count++] = (byte) (b & 0xff);
                }

                if (DEBUG) {
                    Dump.printPacket(System.out, syncFrame);
                }

                if (Utils.compare(syncFrame, FRAME_SYNC)) {
                    inSync = true;
                    System.out.println("IN SYNC");
                }
                count = 0;
            } else {
                byte b;
                if (count >= MTU) {
                    // PacketHelper too long, give up and try to resync
                    message(name + ": packet too long");
                    inSync = false;
                    count = 0;
                    continue;
                }
                if (!isLength) {
                    byte command = (byte) port.read();
                    receiveBuffer[count++] = command;
                    receiveBuffer[count++] = (byte) port.read();
                    receiveBuffer[count++] = (byte) port.read();

                    receiveBuffer[count++] = (byte) port.read();
                    receiveBuffer[count++] = (byte) port.read();

                    int length = (receiveBuffer[count - 1] & 0xff) | (receiveBuffer[count - 2] & 0xff) << 8;
                    payLoad = count + length;

                    isLength = true;
                    continue;
                } else if (count < payLoad) {
                    b = (byte) (port.read() & 0xff);
                    System.out.print(b);
                } else {
                    byte[] packet = new byte[count - 2];
                    System.arraycopy(receiveBuffer, 0, packet, 0, count - 2);

                    int readCrc = (receiveBuffer[count - 1] & 0xff)
                            | (receiveBuffer[count - 2] & 0xff) << 8;
                    int computedCrc = Crc.calc(packet, packet.length);

                    if (DEBUG) {
                        System.err.println("received: ");
                        Dump.printPacket(System.err, packet);
                        System.err.println(" rcrc: " + Integer.toHexString(readCrc)
                                + " ccrc: " + Integer.toHexString(computedCrc));
                    }

                    //if (readCrc != computedCrc) {
                    return packet;
                    /* } else {
                     message(name + ": bad packet");
                     /*
                     * We don't lose sync here. If we did, garbage on the line at startup
                     * will cause loss of the first packet.
                     *
                     count = 0;
                     inSync = false;
                     continue;
                     }*/
                }
                receiveBuffer[count++] = b;
            }
        }
    }

    @Override
    public HashMap<Integer, Long> getSensorList() throws IOException {
        return sensorList;
    }

    @Override
    public void configure(ArrayList<Integer> timeInterval, ArrayList<Integer> thresholdList) throws IOException {
        Integer time = 0;

        for (Map.Entry<Integer, Long> entry : sensorList.entrySet()) {
            Integer shortId = entry.getKey();
            Long macId = entry.getValue();
            if (shortId != 0) {
                int serviceId;
                for (int i = 0; i < thresholdList.size(); i++) {
                    if (i == 0) {
                        serviceId = 3;
                    } else if (i == 1) {
                        serviceId = 1;
                    } else {
                        serviceId = 255;
                    }
                    Integer threshold = thresholdList.get(i);
                    System.out.println("threshold = " + threshold);
                    if (i < timeInterval.size()) {
                        time = timeInterval.get(i);
                        sendReportRate(shortId, i, serviceId, time);
                    }
                    sendThreshold(shortId, i, serviceId, time, threshold.intValue());
                }
            }
        }

    }

    private void sendTimeSyncPacket(int nodeId) throws IOException {
        long currentTime = System.currentTimeMillis()/1000;
		
        System.out.println("SendTime: "+currentTime);
        byte timePacket[] = (new BigInteger(Long.toHexString(currentTime),16)).toByteArray();//ByteBuffer.allocate(8). array();
        writeFramedPacket(P_TIME_SYNC, nodeId, timePacket);
    }

    // Class to build a framed, escaped and crced packet byte stream
    static class Escaper {

        byte[] escaped;
        int escapePtr;
        int crc;

        // We're building a length-byte packet
        Escaper(int length) {
            escaped = new byte[2 * length];
            escapePtr = 0;
            crc = 0;
            while (escapePtr < FRAME_SYNC.length) {
                escaped[escapePtr] = (byte) FRAME_SYNC[escapePtr];
                escapePtr++;
            }
        }

        void nextByte(int b) {
            b = b & 0xff;
            crc = Crc.calcByte(crc, b);

            escaped[escapePtr++] = (byte) b;
        }

        void terminate() {
            crc = crc & 0xff;
            escaped[escapePtr++] = (byte) (crc >> 8);
            escaped[escapePtr++] = (byte) crc;
        }
    }

    private synchronized void sendServiceRequest(int nodeId) throws IOException {
        // send ack
        writeFramedPacket(P_SERVICE_REQUEST, nodeId, dummyPacket);
    }

    private synchronized void sendReportRate(int nodeId, int service, int serviceId, int reportRate) throws IOException {

        byte[] payload = new byte[4];
        payload[0] = (byte) (service & 0xff);//service
        payload[1] = (byte) (serviceId & 0xff); //service Id
        payload[2] = (byte) (reportRate >> 8);
        payload[3] = (byte) (reportRate & 0xff);

        writeFramedPacket(P_SERVICE_REPORT_RATE, nodeId, payload);
    }

    private synchronized void sendThreshold(int nodeId, int service, int serviceId, int reportRate, int threshold) throws IOException {

        byte[] payload = new byte[6];
        payload[0] = (byte) (service & 0xff);//service
        payload[1] = (byte) (serviceId & 0xff); //service Id
        payload[2] = (byte) (reportRate >> 8);
        payload[3] = (byte) (reportRate & 0xff);
        payload[4] = (byte) (threshold >> 8);
        payload[5] = (byte) (threshold & 0xff);

        writeFramedPacket(P_SERVICE_UPDATE_THRESHOLD, nodeId, payload);
    }
    // Write a packet of type 'packetType', first byte 'firstByte'
    // and bytes 2..'count'+1 in 'packet'

    private synchronized void writeFramedPacket(int packetType, int nodeId,
            byte[] packet) throws IOException {

        Escaper buffer = new Escaper(packet.length + 7);

        buffer.nextByte(packetType);

        //Node Id is 16 bit
        buffer.nextByte(nodeId >> 8);
        buffer.nextByte(nodeId & 0xff);

        //+2 for crc
        int length = packet.length + 2;
        //length 
        buffer.nextByte(length >> 8);
        buffer.nextByte(length & 0xff);

        for (int i = 0; i < length - 2; i++) {
            buffer.nextByte(packet[i]);
            System.out.println(i);
        }

        buffer.terminate();

        byte[] realPacket = new byte[buffer.escapePtr];
        System.arraycopy(buffer.escaped, 0, realPacket, 0, buffer.escapePtr);

        port.flush();
        port.write(realPacket);

        if (DEBUG) {
            System.err.println("Data written: ");
            System.err.println("sending: ");
            Dump.printByte(System.err, packetType);
            Dump.printByte(System.err, packet.length);
            Dump.dump("encoded", realPacket);
            System.err.println();
        }
    }
}
