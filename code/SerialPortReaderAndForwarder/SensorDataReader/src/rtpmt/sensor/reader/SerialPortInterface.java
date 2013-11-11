/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.sensor.reader;

import java.io.IOException;

/**
 * SerialPortInterface 
 * @author Kumar
 */
public interface SerialPortInterface {
    /**
     * 
     * @throws java.io.IOException
     */
    public void open() throws IOException;
    /**
     * 
     * @throws java.io.IOException
     */
    public void close() throws IOException;
    /**
     * 
     * @return 
     * @throws java.io.IOException 
     */
    public byte read() throws IOException;
    /**
     * 
     * @param data
     * @return 
     * @throws java.io.IOException 
     */
    public boolean write(byte data) throws IOException;
    /**
     * 
     * @param data
     * @return 
     * @throws java.io.IOException 
     */
    public boolean write(byte[] data) throws IOException;
    
    public void flush() throws IOException;
    
}
