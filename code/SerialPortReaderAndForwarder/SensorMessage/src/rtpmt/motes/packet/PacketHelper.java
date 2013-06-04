/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.motes.packet;

import java.util.ArrayList;

/**
 *
 * @author Saravana kumar
 */
public final class PacketHelper extends Header {
    
    private ArrayList<Byte> PayLoad;
     
    final static int MOTE_DATA_TYPE = 16;
     
    final static int TEMPERATURE_INDEX = 1;
    
    final static int X_INDEX = 2;
     
    final static int Y_INDEX = 3;
    
    public PacketHelper(){
        PayLoad = new ArrayList<Byte>();
    }
    
    /**
     * 
     */
    public PacketHelper(byte[] byteArray){
         
        PayLoad = new ArrayList<Byte>();

        this.ProtocolType = byteArray[0];
        
        this.NodeId = (byteArray[1] & 0xff)
                       | (byteArray[2] & 0xff) << 8;          
        this.PayloadLength = byteArray[3];
        
        this.Service =  byteArray[4];

        this.ServiceId = byteArray[5];
          
 
        
        for(int i = 6 ; i < byteArray.length; i++ ){
            this.addDataToPacket(byteArray[i]);
        }
        
    }
     /**
     * get the temperature from the packet received from the motes
     * @return temperature in Fahrenheit
     */
   
    public void addDataToPacket(Byte value){
        
        PayLoad.add(value);
        
    }
    
    
    /**
     * get the temperature from the packet received from the motes
     * @return temperature in Fahrenheit
     */
    public int getDataLength(){
        
        return PayLoad.size();
        
    }
    
    /**
     * get the temperature from the packet received from the motes
     * @return temperature in Fahrenheit
     */
    public Byte getData(int index){
        
        return PayLoad.get(index);
        
    }
    
    
    /**
     * get the value from the packet received from the sensor
     * will return only if it is  update packet otherwise -1
     * @return 
     */
    public double getValue(){
  
        
        int value = (PayLoad.get(1) & 0xff)
                       | (PayLoad.get(0) & 0xff) << 8;
        
        double temperature = (value * 0.1125 ) + 32;
         
   
        return temperature;
        
    }
    
    /**
     * get the temperature from the packet received from the motes
     * @return temperature in Fahrenheit
     */
    public int getTimeStamp(){
        
        int value = (PayLoad.get(2) & 0xff)
                       | (PayLoad.get(3) & 0xff) << 8;   
        return value;
   
    }
    
    
    
}
