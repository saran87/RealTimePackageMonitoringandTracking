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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import rtpmt.location.tracker.Location;
import rtpmt.location.tracker.LocationTracker;
import rtpmt.network.packet.SensorMessage.SensorInformation;

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
  
  final static int P_SERVICE_REPORT_RATE = 254;
  
  final static int P_UPDATE = 1;

  final static int P_PACKET_NO_ACK = 69;

  final static int P_UNKNOWN = 255;
  
  final static int MTU = 600;

  final static int ACK_TIMEOUT = 1000; // in milliseconds

  private InputStream input;
  private OutputStream output;

  private boolean inSync;

  private byte[] receiveBuffer = new byte[MTU];

  private int seqNo;

  // Packets are received by a separate thread and placed in a
  // per-packet-type queue. If received[x] is null, then x is an
  // unknown protocol (but P_UNKNOWN and P_PACKET_ACK are handled
  // specially)
  private Thread reader;

  private LinkedList[] received;
  
  private HashMap<Integer,Integer> sensorList;

  /**
   * Packetizers are built using the makeXXX methods in BuildSource
   */
 public Packetizer(String name, InputStream io,OutputStream ou) {
    super(name);
    this.input = io;
    output = ou;
    inSync = false;
    seqNo = 13;
    reader = new Thread(this);
    received = new LinkedList[256];
    received[P_ACK] = new LinkedList();
    received[P_PACKET_NO_ACK] = new LinkedList();
    sensorList = new HashMap<Integer, Integer>();
  }

  synchronized public void open(Messenger messages) throws IOException {
    super.open(messages);
    if (!reader.isAlive()) {
      reader.start();
    }
  }

  protected void openSource() throws IOException {
    //io.open();
  }

    @Override
  protected void closeSource() {
        try {
            input.close();
        } catch (IOException ex) {
            Logger.getLogger(Packetizer.class.getName()).log(Level.SEVERE, null, ex);
        }
  }

  protected byte[] readProtocolPacket(int packetType, long deadline)
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
      return (byte[]) inPackets.removeFirst();
    }
  }

  // Place a packet in its packet queue, or reject unknown packet
  // types (which don't have a queue)
  protected void pushProtocolPacket(int packetType, byte[] packet) {
    LinkedList inPackets = received[packetType];

    if (inPackets != null) {
      synchronized (inPackets) {
        inPackets.add(packet);
        inPackets.notify();
      }
    } else if (packetType != P_UNKNOWN) {
        /*
      try {
       writeFramedPacket(P_UNKNOWN, packetType,1, ackPacket);
      } catch (IOException e) {
      }*/
      message(name + ": ignoring unknown packet type 0x"
          + Integer.toHexString(packetType));
    }
  }
  /**
   * gets the Non Acknowledgment packet from the queue and return it
   * @return array of bytes
   * @throws IOException 
   */
    @Override
  protected byte[] readSourcePacket() throws IOException {
    // Packetizer packet format is identical to PacketSource's
    for (;;) {
      byte[] packet = readProtocolPacket(P_PACKET_NO_ACK, 0);
      if (packet.length >= 1) {
        return packet;
      }
    }
  }
  
   /**
   * gets the Non Acknowledgment packet from the queue and return it
   * @return array of bytes
   * @throws IOException 
   */
    @Override
    protected SensorInformation readFormattedPacket() throws IOException {
    // Packetizer packet format is identical to PacketSource's
    for (;;) {
    
      byte[] packet = readProtocolPacket(P_PACKET_NO_ACK, 0);
      Dump.printPacket(System.out, packet);
      if (packet.length >= 1) {
          
          PacketHelper packetHelper = new PacketHelper(packet);
          
          SensorInformation.Builder message = SensorInformation.newBuilder();
      
          message.setDeviceId("1");
          
          DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
          Date date = new Date();
          message.setTimeStamp(dateFormat.format(date));
          
          SensorInformation.Sensor.Builder sensor = SensorInformation.Sensor.newBuilder();
          sensor.setSensorId("1");
          sensor.setSensorUnit("F");
          sensor.setSensorType(SensorInformation.SensorType.TEMPERATURE);
          sensor.setSensorValue(String.valueOf(packetHelper.getValue()));
          //sensor.setSensorValue("80");
          message.addSensors(sensor);
          
          sensor = SensorInformation.Sensor.newBuilder();
          sensor.setSensorId("2");
          sensor.setSensorUnit("gValue");
          sensor.setSensorType(SensorInformation.SensorType.VIBRATION);
          sensor.setSensorValue("45g");
          message.addSensors(sensor);
          SensorInformation.LocationInformation.Builder location = SensorInformation.LocationInformation.newBuilder();
          Location loc = LocationTracker.getLocation();
          if(loc !=null ){
            location.setLatitude(loc.getLatitude());
            location.setLongitude(loc.getLongitude());
            message.setLocation(location);
          }
          else{
            location.setLatitude(43.084603);
            location.setLongitude(-77.680312);
            message.setLocation(location);
          }
          return message.build();
      }
    }
  }
  
   protected PacketHelper getPacket() throws IOException {
    // Packetizer packet format is identical to PacketSource's
    for (;;) {
      byte[] rawpacket = readProtocolPacket(P_PACKET_NO_ACK, 0);
     
      if (rawpacket.length >= 1) {
           PacketHelper packet = new PacketHelper();
           
           return packet;
      }
    }
  }
  /**
   * writes the source packet to the mote
   * @param packet
   * @return
   * @throws IOException 
   */
  // Write an ack-ed packet
    @Override
  protected boolean writeSourcePacket(byte[] packet) throws IOException {
    for (int retries = 0; retries < 25; retries++) {
      writeFramedPacket(P_SERVICE_REQUEST,1, packet);

      long deadline = System.currentTimeMillis() + ACK_TIMEOUT;

      byte[] ack = readProtocolPacket(P_ACK, deadline);
      if (ack == null) {
        if (DEBUG) {
          message(name + ": ACK timed out");
        }
        continue;
      }
      if (ack[0] == (byte) seqNo) {
        if (DEBUG) {
          message(name + ": Rcvd ACK");
        }
        return true;
      }
    }

    return false;
  }

  static private byte dummyPacket[] = new byte[0];

  public void run() 
  {
    try 
    {
      for (;;) 
      {
        byte[] packet = readFramedPacket();
        int packetType = packet[0] & 0xff;
        //int pdataOffset = 0;
        if (packetType == P_REGISTRATION) 
        {
          int nodeId = (packet[1] & 0xff)
                       | (packet[2] & 0xff) << 8;
          sendServiceRequest(nodeId);
          // And merge with un-acked packets
          packetType = P_PACKET_NO_ACK;
        }
        else if(packetType == P_UPDATE){
             packetType = P_PACKET_NO_ACK;
             int nodeId = (packet[1] & 0xff)
                       | (packet[2] & 0xff) << 8;
            //sendThresholdRequest(nodeId);
        }
        /*int dataLength = packet.length - pdataOffset;
        byte[] dataPacket = new byte[dataLength];
        System.arraycopy(packet, pdataOffset, dataPacket, 0, dataLength);*/
        pushProtocolPacket(packetType, packet);
      }
    } catch (IOException e) 
    {
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
    
    for (;;) 
    {
      
      // System.out.print(input.read());

      
      if (!inSync) 
      {
        message(name + ": resynchronising");
        // re-synchronise 
        int b  = input.read() & 0xff;
        while (b != 170){
            b  = input.read() & 0xff;
        }
        System.out.println("count:"+ count + "InSync:" + inSync);
        syncFrame[count++] = (byte)(b & 0xff);
       
        while (count < FRAME_SYNC.length)
        {
             b =  input.read();
             syncFrame[count++] = (byte)(b & 0xff);    
        }
       
        if(DEBUG){
            Dump.printPacket(System.out, syncFrame);
        }
        
        if(Utils.compare(syncFrame,FRAME_SYNC))
        {
          inSync = true;
          System.out.println("IN SYNC");
        }
        count = 0;
      }
     
      else{
        byte b;
        if (count >= MTU) 
        {
          // PacketHelper too long, give up and try to resync
          message(name + ": packet too long");
          inSync = false;
          continue;
        }
        if(!isLength)
        {
          byte command =  (byte)input.read();
          receiveBuffer[count++] = command;
          receiveBuffer[count++] = (byte)input.read();
          receiveBuffer[count++] = (byte)input.read();
  
          receiveBuffer[count++] = (byte)input.read();
          receiveBuffer[count++] = (byte)input.read();
          
          int length = (receiveBuffer[count-1] & 0xff) | (receiveBuffer[count-2] & 0xff) << 8;
          payLoad = count + length;
          
          isLength = true;
          continue;
        }
        else if (count < payLoad){
            b = (byte) (input.read() & 0xff);
        }
        else{
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

          if (readCrc == computedCrc) {
            return packet;
          } else {
            message(name + ": bad packet");
            /*
             * We don't lose sync here. If we did, garbage on the line at startup
             * will cause loss of the first packet.
            */
            count = 0;
            inSync = false;
            continue;
          }
        }
        receiveBuffer[count++] = b;
    }
    }
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
      while (escapePtr < FRAME_SYNC.length)
      {
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
      crc = crc & 0xff ;
      escaped[escapePtr++] = (byte)crc;
      escaped[escapePtr++] = (byte)(crc >> 8);
    }
  }

  private synchronized void sendServiceRequest(int nodeId) throws IOException{
      // send ack
       writeFramedPacket(P_SERVICE_REQUEST,nodeId,dummyPacket);
  }
  
  private synchronized void sendReportRate(int nodeId) throws IOException{
      
      dummyPacket = new byte[4];
      dummyPacket[0] = (0 &0xff);//service
      dummyPacket[1] = 1 &0xff; //service Id
      dummyPacket[2] = 1 &0xff;
      dummyPacket[3] = 1 >> 8;
      
      writeFramedPacket(P_SERVICE_REPORT_RATE,nodeId,dummyPacket);
  }
  
  // Write a packet of type 'packetType', first byte 'firstByte'
  // and bytes 2..'count'+1 in 'packet'
  private synchronized void writeFramedPacket(int packetType, int nodeId,
      byte[] packet) throws IOException {
   
      if (DEBUG) {
      System.err.println("sending: ");
      Dump.printByte(System.err, packetType);
      Dump.printByte(System.err, packet.length);
      Dump.printPacket(System.err, packet);
      System.err.println();
    }
  
     /*
    byte[] realPacket = new byte[8];
    count = 0;
    realPacket[count++] = (byte)(170 & 0xff);
    realPacket[count++] = (byte)(255 & 0xff);
    realPacket[count++] = (byte)(85 & 0xff);
    realPacket[count++] = (byte)(255 & 0xff);
    realPacket[count++] = (byte)(33 & 0xff);
    realPacket[count++] = (byte)(232 & 0xff);
    realPacket[count++] = (byte)(18 & 0xff);
    realPacket[count++] = (byte)(52 & 0xff);
   */

    Escaper buffer = new Escaper(packet.length + 7);

    buffer.nextByte(packetType);
    
    //Node Id is 16 bit
    buffer.nextByte(nodeId &0xff);
    buffer.nextByte(nodeId >> 8);
    
    //+2 for crc
    int length = packet.length + 2; 
    //length 
    buffer.nextByte(length & 0xff);
    buffer.nextByte(length >> 8);
    
    for (int i = 0; i < length-2; i++) {
      buffer.nextByte(packet[i]);
      System.out.println(i);
    }

    buffer.terminate();

    byte[] realPacket = new byte[buffer.escapePtr];
    System.arraycopy(buffer.escaped, 0, realPacket, 0, buffer.escapePtr);

    if (true) {
      Dump.dump("encoded", realPacket);
    }
     output.flush();
     output.write(realPacket);
  }
  
  
}
