/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.motes.packet;

/**
 *
 * @author kumar
 */
public class Header {
    
    //Handle Id to identify the message type
    protected int ProtocolType;
    
    //This 2 byte indicates the Node Id
    protected int NodeId;
    
    //Payload Length
    protected int PayloadLength;
     
    //service
    protected int Service;
      
    //service Id
    protected int ServiceId;
    
}
