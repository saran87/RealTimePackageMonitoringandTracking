/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.motes.packet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rtpmt.sensor.reader.SerialPortInterface;

/**
 *
 * @author thirumalaisamy
 */
public class TestReading extends Thread{
    
    private final SerialPortInterface port;
    public boolean isComplete = false;
    private int payLoad;

       
    public File dataFile = new File("temp");
    private FileOutputStream output;
    public TestReading(SerialPortInterface port,int payLoad) throws FileNotFoundException{
        this.output = new FileOutputStream(dataFile);
        this.port = port;
        this.payLoad = payLoad;
        
    }

   
    
    @Override
    public void run(){
        System.out.println("Started");
        try {
            byte tempData;
            
            while(payLoad >0){
                tempData = port.read();
               output.write(tempData);
               payLoad--;
            }
        } catch (IOException ex) {
            Logger.getLogger(TestReading.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            isComplete = true;
            try {
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(TestReading.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
