/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.motes.packet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *
 * @author Kumar
 */
public class TestPacketizer extends Packetizer {
    
    public TestPacketizer(String name, InputStream io) {
       
        super(name,io);
 
    }
    
    private byte[] readFramedPacket() throws IOException {
        
        ArrayList<Byte> list = new ArrayList<Byte>();
        
        for(;;){
            
            //list.to
            
            break;
        }
        
       return toByteArray(list);
    }
    
    /**
     * Converts array list of Byte to array of bytes
     * @param in
     * @return 
     */
    public static byte[] toByteArray (ArrayList<Byte> in) {
	
        byte[] temp = new byte[in.size()];
        
        for (int i = 0; i<temp.length; i++) {
                temp[i] = in.get(i);
        }
        
	return temp;
    }
}
