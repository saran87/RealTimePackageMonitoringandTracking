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
    protected String ProtocolType;
    
    //This byte indicates it is an AM message
    protected String MessageType;
    
    //These two hexa bytes are the Destination address (FF FF indicates broadcast)
    protected String DestinationAddress;
    
    //Source address, this indicates address of last mote before sink node.
    //As this is a one hop application, it is also the Origin of the message
    protected String SourceAddress;
    
    //Payload Length
    protected String PayloadLength;
     
    //Group Id
    protected String GroupId;
      
    //Handle Id to identify the message type
    protected String HandleId;
    
}
