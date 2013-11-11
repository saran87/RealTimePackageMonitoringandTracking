/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.sensor.reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Kumar
 */
public class SerialPort implements SerialPortInterface{
    
    private final InputStream input;
    private final OutputStream output;
    
    public  SerialPort(InputStream _input, OutputStream _output){
        
        this.input = _input;
        this.output = _output;
    }

    @Override
    public void open() {
      
    }

    @Override
    public void close() {
        try {
            input.close();
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(SerialPort.class.getName()).log(Level.SEVERE,"Not able to close input/output stream", ex);
        }
    }

    @Override
    public byte read() {
        byte data = 0;
        
        try{
           
            data = (byte) (input.read() &  0xff);
           
        } catch (IOException ex) {
            Logger.getLogger(SerialPort.class.getName()).log(Level.SEVERE, "Error while reading the data ", ex);
        }
        
        return data;
    }

    @Override
    public boolean write(byte data) {
        
        try{
            output.write(data);
            return true;
           
        } catch (IOException ex) {
            Logger.getLogger(SerialPort.class.getName()).log(Level.SEVERE, "Error while writing the data to serial port", ex);
            return false;
        }
    }

    @Override
    public boolean write(byte[] data) {
      
         try{
            output.write(data);
            return true;
           
        } catch (IOException ex) {
            Logger.getLogger(SerialPort.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
         
       
    }

    @Override
    public void flush() {
        try {
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(SerialPort.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
