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
    
    private ArrayList<String> PayLoad;
     
    final static int MOTE_DATA_TYPE = 16;
     
    final static int TEMPERATURE_INDEX = 1;
    
    final static int X_INDEX = 2;
     
    final static int Y_INDEX = 3;
    
    public PacketHelper(){
        PayLoad = new ArrayList<String>();
    }
    
    /**
     * 
     */
    public PacketHelper(byte[] byteArray){
         
        PayLoad = new ArrayList<String>();

        this.DestinationAddress = Integer.toHexString(byteArray[1] & 0xff) + Integer.toHexString(byteArray[2] & 0xff);
        
        this.SourceAddress = Integer.toHexString(byteArray[3] & 0xff) + Integer.toHexString( byteArray[4] & 0xff);
          
        this.PayloadLength = Integer.toHexString(byteArray[5] & 0xff);
        
        this.GroupId =  Integer.toHexString(byteArray[6] & 0xff);

        this.HandleId = Integer.toHexString(byteArray[7] & 0xff);
          
        int payLoadLength = Integer.parseInt(this.PayloadLength, MOTE_DATA_TYPE);
          
        //7 byets are already used ,so add 7 plus the payload length
        int limit = 7 + payLoadLength;
        
        for(int i = 8 ; i < limit; i++ ){
            String data =  Integer.toHexString(byteArray[i] & 0xff) + Integer.toHexString(byteArray[++i] & 0xff);
            this.addDataToPacket(data);
        }
        
    }
     /**
     * get the temperature from the packet received from the motes
     * @return temperature in Fahrenheit
     */
   
    public void addDataToPacket(String value){
        
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
    public String getData(int index){
        
        return PayLoad.get(index);
        
    }
    
    /**
     * get the temperature from the packet received from the motes
     * @return temperature in Fahrenheit
     */
   
    public String getTemperature(){
        
        String temperature = null;
        
        if(PayLoad.size() >  TEMPERATURE_INDEX){
            
            String hexString = PayLoad.get(TEMPERATURE_INDEX);
            
            int rawValue = Integer.parseInt(hexString,MOTE_DATA_TYPE);
            
            //Fahrenheit temperature calculation
            double fTemperature = CalculationConstants.D1_3V_F  + (CalculationConstants.D2_14BIT_F * rawValue);
            
            temperature = Double.toString(fTemperature);
        }
        else{
            temperature = "Temperature not received";  
        }
        
        return temperature;        
    }
    /**
     * get the x g value from the packet received from the motes
     * @return x g value
     */
    public String xGValue(){
        
        String gvalue = null;
        
        if(PayLoad.size() >  X_INDEX){
            
            String hexString = PayLoad.get(X_INDEX);
            
            int rawValue = Integer.parseInt(hexString,MOTE_DATA_TYPE);
           
            //RawVoltage Calculation
            double rawVolatage =  (rawValue/4096.0 ) * 2.5;
            
            //Acceleration value calculation
            Double acceleration = (rawVolatage - 1.5)/0.057;
           
            gvalue =  acceleration.toString();
        }
        else{
            gvalue = "vibration not received";  
        }
        
        return gvalue;      
    }
     /**
     * get the y g value from the packet received from the motes
     * @return y g value
     */
    public String yGValue(){
        
        String gvalue = null;
        
        if(PayLoad.size() >=  Y_INDEX){
            
            String hexString = PayLoad.get(Y_INDEX);
            
            int rawValue = Integer.parseInt(hexString,MOTE_DATA_TYPE);
            
            //RawVoltage Calculation
            double rawVolatage =  (rawValue/4096.0) * 2.5;
            
            //Acceleration value calculation
            Double acceleration = (rawVolatage - 1.5)/0.057;
            
            gvalue =  acceleration.toString();
        }
        else{
            gvalue = "vibration not received";  
        }
        
        return gvalue;        
    }
}
