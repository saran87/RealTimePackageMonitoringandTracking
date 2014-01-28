/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.sensor.reader;

import java.io.IOException;
import rtpmt.motes.packet.BlackBoxReader;
import rtpmt.motes.packet.PacketSource;
import rtpmt.motes.packet.RealTimeReader;
import rtpmt.packages.SensorEventHandler;
import rtpmt.sensor.util.Packet;
import rtpmt.packages.Package;

/**
 *
 * @author Kumar
 */
public class SensorReader {
    
    private final PacketSource packetReader;
   
    /**
     * 
     * @param serialPort
     * @param isRealTime 
     */
    public SensorReader(SerialPortInterface serialPort,boolean isRealTime){
        if(isRealTime){
            packetReader = new RealTimeReader("Real Time Reader",serialPort);
        }
        else{
            packetReader = new BlackBoxReader("Black Box Reader", serialPort);
        }
    }
    
    /**
     * 
     * @throws IOException 
     * @throws java.lang.InterruptedException 
     */
    public void open() throws IOException, InterruptedException{
        packetReader.open(null);
    }
    
    /**
     * 
     * @throws IOException 
     */
    public void close() throws IOException{
        packetReader.close();
    }
    
    /**
     * 
     * @return
     * @throws IOException 
     */
    public Packet readPacket() throws IOException{
       return packetReader.readPacket();
    }
    
    /**
     * 
     * @param pack 
     * @return  
     * @throws java.io.IOException 
     * @throws java.lang.InterruptedException 
     */
    public boolean configure(Package pack) throws IOException,NullPointerException, InterruptedException{
        return packetReader.configure(pack);
    }
    
    /**
     * 
     * @param eventListenerObject
     * @return 
     */
     public boolean addSensorEventHandler(SensorEventHandler eventListenerObject){
         return packetReader.addSensorEventHandler(eventListenerObject);
     }
     
     
    /**
     * Reset the connected sensors, Format the SD card and reset the settings
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public void reset() throws InterruptedException,IOException{
        packetReader.reset();
    }
    
    /**
     * Reset the connected sensors, Format the SD card and reset the settings
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public void resetConfig() throws InterruptedException,IOException{
         packetReader.resetConfig();
    }
    
    /**
     * Reset the connected sensors, Format the SD card and reset the settings
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public void resetRadio() throws InterruptedException,IOException{
        packetReader.resetRadio();
    }
    
    /***
     * 
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public void clearData() throws InterruptedException,IOException{
        packetReader.clearData();
    }
    
    public void calibrateSensor() throws InterruptedException, IOException{
        packetReader.calibrateSensor();
    }
}
