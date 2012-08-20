/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.motes.packet;

import java.io.IOException;
import java.io.InputStream;

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
  
  private int[] receiveBuffer;
  
  /**
   * Constructor initializes the input stream with input stream of serial port
   */
  public Packetizer(InputStream input){
      this.input = input;
  }
  
  /**
   * Read packet
   */
  public void readPacket() throws IOException
  {
    //get available number of bytes in the input stream
    int available = input.available();
    //Intialized received buffer to the available byte length in the input stream
    receiveBuffer = new int[available];
    //read from the input stream
    int count = 0;
    while(input.available()>0){
         int b = input.read();
         if(b != SYNC_BYTE && b!=ESCAPE_BYTE && count>2 ){
             receiveBuffer[count] = b;
         }
         count++;
    }          
  }
  
  /**
   * Dump Packet converts the packet data into hex strings and returns as a string
   * @return string of packet in hex string
   */
  public String dumpPacket()
  {
      StringBuilder dump = new StringBuilder();
      
      for(int i=0; i<receiveBuffer.length;i++)
      {
            dump.append(Integer.toHexString(receiveBuffer[i]));
            dump.append(" ");
      }  
      
      return dump.toString();
  }
  
  /**
   * Get Temperature from the received packet
   */
  
  public String getTemperature()
  {
       int IntegerhexValue_int = Integer.getInteger("17", 16);
       int DecimalhexValue_int = Integer.getInteger("45", 16);
       String temp  = "1716";
       String temperature = Integer.toString(IntegerhexValue_int) + Integer.toString(DecimalhexValue_int);
       double temperature_double =  Double.parseDouble(temperature);
       double Fah_double = temperature_double * 9 / 5 + 32; 
      
      return temp;
  }
}
