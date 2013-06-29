/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.motes.packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    
    static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private Date date = null;
    
    public PacketHelper(){
        PayLoad = new ArrayList<Byte>();
        date = new Date();
    }
    
    /**
     * 
     */
    public PacketHelper(byte[] byteArray){
         
        PayLoad = new ArrayList<Byte>();

        this.ProtocolType = byteArray[0];
        
        this.NodeId = (byteArray[1] & 0xff)
                       | (byteArray[2] & 0xff) << 8;          
        this.PayloadLength = (byteArray[3] & 0xff) | (byteArray[4] & 0xff) << 8;
        
        this.Service =  byteArray[5];

        this.ServiceId = byteArray[6];
          
        System.out.println(this.Service + "--" + this.ServiceId);
        int i = 0;
        for(i = 7 ; i < byteArray.length - 4; i++ ){
            this.addDataToPacket(byteArray[i]);
        }
        
        ByteBuffer bb = ByteBuffer.wrap(byteArray, i, 4);
        long timeStamp = (bb.getInt() * (long)1000);
        date = new Date(timeStamp);
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
     * get the value from the packet received from the sensor
     * will return only if it is  update packet otherwise -1
     * @return 
     */
    public double getHumidity(){
  
        
        int value = (PayLoad.get(1) & 0xff)
                       | (PayLoad.get(0) & 0xff) << 8;
        
        double humdity = (value / 16 ) - 24;
         
   
        return humdity;
        
    }
    
    
    public String getVibration(){
  
        StringBuilder vibration = new StringBuilder();
         float g; 
        for(int i = 0; i< PayLoad.size(); i++){
            g = (float)((int) PayLoad.get(i) * 15.6);
            vibration.append(String.valueOf(g)).append("-");
        }
        vibration.deleteCharAt(vibration.length()-1);
        return vibration.toString();
        
    }
    
    public String getShock(){
  
        StringBuilder shock = new StringBuilder();
        float g; 
        int i = 0;
        for(i=2; i < 70; i++){
            g = (float)((int) PayLoad.get(i) * 15.6);
            shock.append(String.valueOf(g)).append("-");
        }
        for(; i< PayLoad.size(); i++){
            g = (float) (((int)PayLoad.get(i) -128) * 0.64);
            shock.append(String.valueOf(g)).append("-");
        }
        shock.deleteCharAt(shock.length()-1);
        return shock.toString();
        
    }
    /**
     * get the temperature from the packet received from the motes
     * @return temperature in Fahrenheit
     */
    public String getTimeStamp(){
        
       
        return dateFormat.format(date);
   
    }
   
    
    
    public boolean isHumidty(){
        
        if(this.Service == 1){
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean isTemperature(){
        
        if(this.Service == 0 && this.ServiceId ==1){
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean isVibration(){
        
        if(this.Service == 2){
            return true;
        }
        else{
            return false;
        }
        
    }
    
     public boolean isShock(){
        
        if(this.Service == 3){
            return true;
        }
        else{
            return false;
        }
        
    }
     
     public boolean isX(){
        
        boolean isTrue = false;
        
        if(this.Service == 3 || this.Service == 2){
            if( this.ServiceId == 1){
                isTrue = true;
            }else{
                isTrue = false;
            }
        }
        else{
            isTrue = false;
        }
        
        return isTrue;
    }
     
      public boolean isY(){
        
        
        boolean isTrue = false;
        
        if(this.Service == 3 || this.Service == 2){
            if( this.ServiceId == 2){
                isTrue = true;
            }else{
                isTrue = false;
            }
        }
        else{
            isTrue = false;
        }
        
        return isTrue;
        
    }
      
     public boolean isZ(){
        
        
        boolean isTrue = false;
        
        if(this.Service == 3 || this.Service == 2){
            if( this.ServiceId == 3){
                isTrue = true;
            }else{
                isTrue = false;
            }
        }
        else{
            isTrue = false;
        }
        
        return isTrue;
        
    }
    
}
