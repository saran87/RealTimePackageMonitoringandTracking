/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.sensor.util;

/**
 *
 * @author kumar
 */
public class Header {
    
    //Handle Id to identify the message type
    public int ProtocolType;
    
    //This 2 byte indicates the Node Id
    public int NodeId;
    
    //Payload Length
    public int PayloadLength;
     
    //service
    public int Service;
      
    //service Id
    public int ServiceId;
    
}
