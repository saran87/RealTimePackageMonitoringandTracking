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

import rtpmt.sensor.util.Constants;
import rtpmt.sensor.util.Packet;
import rtpmt.sensor.util.Utils;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import rtpmt.sensor.reader.SerialPortInterface;
import rtpmt.packages.Package;
import rtpmt.packages.Sensor;
import rtpmt.packages.Config;
import rtpmt.packages.SensorEventHandler;
import rtpmt.packages.PackageList;

/**
 * The Packetizer class implements the new RTMPT protocol, using a ByteSource
 * for low-level I/O
 *
 * @author Saravana Kumar
 * @version 1.0
 */
public class RealTimeReader extends AbstractSource implements Runnable {

    final static boolean DEBUG = true;
    private final SerialPortInterface port;
    private boolean inSync;
    private final byte[] receiveBuffer = new byte[Constants.MTU];
    private final Thread reader;
    private boolean isThreadRunning = false;
    private final LinkedList[] received;
    private final ConcurrentHashMap<String, Packet> partialData;

    /**
     * Packetizers are built using the makeXXX methods in BuildSource
     *
     * @param name
     * @param _inputPort
     */
    public RealTimeReader(String name, SerialPortInterface _inputPort) {
        super(name);
        this.port = _inputPort;
        inSync = false;
        reader = new Thread(this);
        received = new LinkedList[256];
        received[Constants.P_ACK] = new LinkedList<Packet>();
        received[Constants.P_UPDATE] = new LinkedList<Packet>();
        partialData = new ConcurrentHashMap<String, Packet>();
    }

    @Override
    protected void openSource() throws IOException {
        port.open();
        if (!reader.isAlive()) {
            isThreadRunning = true;
            reader.start();
        }
    }

    @Override
    protected void closeSource() throws IOException {
        port.close();
        isThreadRunning = false;
    }

    /**
     *
     * @param packetType
     * @param deadline
     * @return
     * @throws IOException
     */
    protected Packet readProtocolPacket(int packetType, long deadline)
            throws IOException {
        LinkedList inPackets;
        inPackets = received[packetType];

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

    /**
     * Place a packet in its packet queue, or reject unknown packet types (which
     * don't have a queue)
     *
     * @param packetType
     * @param packet
     */
    @SuppressWarnings("unchecked")
    protected void pushProtocolPacket(int packetType, Packet packet) {
        LinkedList<Packet> inPackets;
        inPackets = received[packetType];

        if (inPackets != null) {

            synchronized (inPackets) {
                inPackets.add(packet);
                inPackets.notify();
            }

        } else if (packetType != Constants.P_UNKNOWN) {
            message(name + ": ignoring unknown packet type 0x"
                    + Integer.toHexString(packetType));
        }
    }

    /**
     * TO-DO check the working of combining packets
     *
     * @param packet
     */
    private void handlePartialPackets(Packet packet) {
        if (packet.isPartialPacket()) {
            String key = packet.uniqueId();
            System.out.println("Key:" + key);
            if (partialData.containsKey(key)) {
                Packet partialpacket = partialData.get(key);
                if (partialpacket.isCompletePacket(packet)) {
                    partialpacket.combinePacket(packet);
                    System.out.println("I am Combining the packets" + packet.getDataLength());
                    pushProtocolPacket(Constants.P_UPDATE, partialData.remove(key));
                } else {
                    System.out.println("mismatch packet");
                    Dump.dump(packet);
                }

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
    public Packet readPacket() throws IOException {
        // Packetizer packet format is identical to PacketSource's
        for (;;) {

            Packet packet = readProtocolPacket(Constants.P_UPDATE, 0);
            if (DEBUG) {
                Dump.dump(packet);
            }

            return packet;
        }
    }

    private static final byte dummyPacket[] = new byte[0];

    private void publishNewSensor(Package newPackage) {
        if (eventListenerObjects != null) {
            for (SensorEventHandler iSensorEventHandler : eventListenerObjects) {
                iSensorEventHandler.newSensorAdded(newPackage);
            }
        }
    }

    @Override
    public void run() {
        
            while (isThreadRunning) {
                try {
                byte[] packet = readFramedPacket();
                int packetType = packet[0] & 0xff;
                int nodeId = (packet[1] & 0xff) << 8 | (packet[2] & 0xff);

                if (packetType == Constants.P_REGISTRATION) {
                    StringBuilder macId = new StringBuilder();
                    for (int i = 5; i < packet.length; i++) {
                        macId.append(Integer.toHexString(packet[i] & 0xff).toUpperCase());
                    }

                    Integer shortId = nodeId;
                    PackageList.addPackage(shortId, macId.toString());
                    
                    //sendServiceRequest(nodeId); // Sending the Service Request
                    sendTimeSyncPacket(nodeId);
                    sendGetBoardId(nodeId);
                    System.out.println("Service Request sent!");

                } else if (packetType == Constants.P_SERVICE_RESPONSE) {

                    System.out.println("Service Response Recieved Sent");

                } else if (packetType == Constants.P_UPDATE || packetType == Constants.P_UPDATE_THRESHOLD) {
                    packetType = Constants.P_UPDATE;
                    Packet packetHelper = new Packet(packet);
                    if (packetHelper.isPartialPacket()) {
                        handlePartialPackets(packetHelper);
                    } else {
                        pushProtocolPacket(packetType, packetHelper);
                    }
                }else if(packetType ==  Constants.P_BLACKBOX_RESPONSE){
                      Packet packetHelper = new Packet(packet);
                      String sensorId = packetHelper.sensorId();
                      publishNewSensor(PackageList.updateSensorId(nodeId, sensorId));
                }
            }   catch (IOException ex) {
                    Logger.getLogger(RealTimeReader.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (Exception e){
                    Logger.getLogger(RealTimeReader.class.getName()).log(Level.SEVERE, null, e);
                }
            }
 
    }

    /*
     * Packet Reader
     */
    // Read system-level packet. If inSync is false, we currently don't
    // have sync
    private byte[] readFramedPacket() throws IOException {

        int count = 0;
        boolean isLength = false;
        int payLoad = 0;
        inSync = false;
        byte[] syncFrame = new byte[Constants.FRAME_SYNC.length];

        for (;;) {
            if (!inSync) {
                message(name + ": resynchronising");
                // re-synchronise 

                int b = port.read() & 0xff;

                while (b != 170) {

                    b = port.read() & 0xff;
                    //System.out.println("I am try reading frame:" + b);
                }
               /*System.out.println("count:" + count + "InSync:" + inSync);
                if (count >= Constants.MTU) {
                    // PacketHelper too long, give up and try to resync
                    message(name + ": packet too long");
                    inSync = false;
                    count = 0;
                    continue;
                }*/
                syncFrame[count++] = (byte) (b & 0xff);

                while (count < Constants.FRAME_SYNC.length) {
                    b = port.read();
                    syncFrame[count++] = (byte) (b & 0xff);
                }

                if (DEBUG) {
                    Dump.printPacket(System.out, syncFrame);
                }

                if (Utils.compare(syncFrame, Constants.FRAME_SYNC)) {
                    inSync = true;
                    System.out.println("IN SYNC");
                }
                count = 0;
            } else {
                byte b;
                if (count >= Constants.MTU) {
                    // PacketHelper too long, give up and try to resync
                    message(name + ": packet too long");
                    inSync = false;
                    count = 0;
                    continue;
                }
                if (!isLength) {
                    byte command = port.read();
                    receiveBuffer[count++] = command;

                    receiveBuffer[count++] = port.read();
                    receiveBuffer[count++] = port.read();
                   
                    receiveBuffer[count++] = port.read();
                    receiveBuffer[count++] = port.read();

                    int length = (receiveBuffer[count - 1] & 0xff) | (receiveBuffer[count - 2] & 0xff) << 8;
                    payLoad = count + length;
                    System.out.println("PayLoad Length: " + payLoad);
                    if(payLoad < 15 || payLoad > 526 ){
                       inSync = false;
                       count = 0;
                       continue;
                    }
                    isLength = true;
                    continue;
                } else if (count < payLoad) {
                    b = (byte) (port.read() & 0xff);
                    //System.out.println("I read frame:" + b);
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
    public boolean configure(Package pack) throws IOException, NullPointerException, InterruptedException {

        Integer shortId = pack.getShortId();
        String macId = pack.getSensorId();

        if (shortId != 0) {
            HashMap<Sensor, Config> configList = pack.getConfigs();

            for (Map.Entry<Sensor, Config> entry1 : configList.entrySet()) {
                Sensor sensor = entry1.getKey();
                Config config = entry1.getValue();
                int[] serviceIds = getServiceIDs(sensor);

                if (!sensor.equals(Sensor.SHOCK)) {
                    sendReportRate(shortId, serviceIds[0], serviceIds[1], config.getTimePeriod());
                }
                sendThreshold(shortId, serviceIds[0], serviceIds[1], config.getAfterThresholdTimePeriod(), config.getMaxRawThreshold());
            }
        }
        return true;
    }

    // Class to build a framed, escaped and crced packet byte stream
    private void sendTimeSyncPacket(int nodeId) throws IOException {
        long currentTime = System.currentTimeMillis() / 1000;

        System.out.println("SendTime: " + currentTime);
        byte timePacket[] = (new BigInteger(Long.toHexString(currentTime), 16)).toByteArray();//ByteBuffer.allocate(8). array();
        writeFramedPacket(Constants.P_TIME_SYNC, nodeId, timePacket);
    }

    private int[] getServiceIDs(Sensor sensor) {
        int[] serviceIds = new int[2];

        switch (sensor) {
            case TEMPERATURE:
                serviceIds[0] = 0;
                serviceIds[1] = 3;
                break;
            case HUMIDITY:
                serviceIds[0] = 1;
                serviceIds[1] = 1;
                break;
            case VIBRATION:
                serviceIds[0] = 2;
                serviceIds[1] = 255;
                break;
            case SHOCK:
                serviceIds[0] = 3;
                serviceIds[1] = 255;
                break;
        }

        return serviceIds;
    }

    private synchronized void sendServiceRequest(int nodeId) throws IOException {
        // send ack
        writeFramedPacket(Constants.P_SERVICE_REQUEST, nodeId, dummyPacket);
    }

    private synchronized void sendReportRate(int nodeId, int service, int serviceId, int reportRate) throws IOException {

        byte[] payload = new byte[4];
        payload[0] = (byte) (service & 0xff);//service
        payload[1] = (byte) (serviceId & 0xff); //service Id
        payload[2] = (byte) (reportRate >> 8);
        payload[3] = (byte) (reportRate & 0xff);

        writeFramedPacket(Constants.P_SERVICE_REPORT_RATE, nodeId, payload);
    }

    private synchronized void sendThreshold(int nodeId, int service, int serviceId, int reportRate, int threshold) throws IOException {

        byte[] payload = new byte[6];
        payload[0] = (byte) (service & 0xff);//service
        payload[1] = (byte) (serviceId & 0xff); //service Id
        payload[2] = (byte) (reportRate >> 8);
        payload[3] = (byte) (reportRate & 0xff);
        payload[4] = (byte) (threshold >> 8);
        payload[5] = (byte) (threshold & 0xff);

        writeFramedPacket(Constants.P_SERVICE_UPDATE_THRESHOLD, nodeId, payload);
    }
    
    /**
     *
     * @return @throws IOException
     * @throws InterruptedException
     */
    private void sendGetBoardId(int nodeId) throws IOException, InterruptedException {
         
        byte[] data = new byte[4];
        data[0] = Constants.GET_BOARD_ID[0];
        data[1] = Constants.GET_BOARD_ID[1];
        writeFramedPacket(Constants.P_BLACKBOX_REQUEST,nodeId, data);

    }
    // Write a packet of type 'packetType', first byte 'firstByte'
    // and bytes 2..'count'+1 in 'packet'

    private synchronized void writeFramedPacket(int packetType, int nodeId,
            byte[] packet) throws IOException {

        SensorPacket buffer = new SensorPacket(packet.length + 7);

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

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetConfig() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetRadio() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
