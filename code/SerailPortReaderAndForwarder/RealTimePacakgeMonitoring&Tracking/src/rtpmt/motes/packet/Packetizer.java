/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.motes.packet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author kumar
 */
public class Packetizer {
    
  final static boolean DEBUG = false;

  final static int SYNC_BYTE = 126;

  final static int ESCAPE_BYTE = 125;
  
  final static int P_ACK = 67;

  final static int P_PACKET_ACK = 68;

  final static int P_PACKET_NO_ACK = 69;

  final static int P_UNKNOWN = 255;
  
  final static int MTU = 256;

  final static int ACK_TIMEOUT = 1000; // in milliseconds

  private InputStream input = null;
  
  
  private Queue<Packet> packetQueue;
  
  /**
   * Constructor initializes the input stream with input stream of serial port
   */
  public Packetizer(InputStream input){
      this.input  = input;
      packetQueue = new LinkedBlockingQueue<Packet>();
  }
  
  /**
   * Read packet
   */
  public void readPacket() throws IOException
  {
    //get available number of bytes in the input stream
    int available = input.available();
    
    int count = 0;
    Packet packet = new Packet();
    boolean inSync = false;
    
    if(input.available()>18){
         
        int b = input.read();
         
         if(b == SYNC_BYTE ){
             if(!inSync){
                 inSync = true;
             }
             else{
                 System.out.println("bad packet");
                 return;
             }
        }
        
        b = input.read();
        packet.ProtocolType =  Integer.toHexString(b);
       
        b = input.read();
        packet.ProtocolType =  Integer.toHexString(b);
        
        packet.DestinationAddress =   Integer.toHexString(input.read()) + Integer.toHexString(input.read());
        
        packet.SourceAddress = Integer.toHexString(input.read()) + Integer.toHexString( input.read());
       
        
        b = input.read();
        packet.PayloadLength = Integer.toHexString(b);
        
        b = input.read();
        packet.GroupId =  Integer.toHexString(b);
        
        b = input.read();
        packet.HandleId = Integer.toHexString(b);
        
        int payLoadLength = Integer.parseInt(packet.PayloadLength, Packet.MOTE_DATA_TYPE);
        
        for(int i = 0 ; i<payLoadLength/2; i++ ){
            String data =  Integer.toHexString(input.read()) + Integer.toHexString( input.read());
            packet.addDataToPacket(data);
        }
        
        String CRC =  Integer.toHexString(input.read()) + Integer.toHexString( input.read());
        b = input.read();
        
        packetQueue.add(packet);
    }          
  }
  
  /**
   * Dump Packet converts the packet data into hex strings and returns as a string
   * @return string of packet in hex string
   */
  public String dumpPacket()
  {
      StringBuilder dump = new StringBuilder();
      if(!packetQueue.isEmpty()){
        Packet packet = packetQueue.peek();

        for(int i=0; i<packet.getDataLength();i++)
        {
                dump.append(packet.getData(i));
                dump.append(" ");
        }  
      }
      else{
          dump.append("No Packet available");
      }
      return dump.toString();
  }
  
  /**
   * Get Temperature from the received packet
   */
  
  public String getTemperature()
  {
       /*int IntegerhexValue_int = Integer.getInteger("17", 16);
       int DecimalhexValue_int = Integer.getInteger("45", 16);
       String temp  = "1716";
       String temperature = Integer.toString(IntegerhexValue_int) + Integer.toString(DecimalhexValue_int);
       double temperature_double =  Double.parseDouble(temperature);
       double Fah_double = temperature_double * 9 / 5 + 32; */
        String temperature = "";
        if(!packetQueue.isEmpty()){

            Packet packet = packetQueue.remove();
            temperature   = packet.getTemperature() + " F";
        }
        else{
            temperature = "Bad packet";
        }
            return temperature;
        }
}
