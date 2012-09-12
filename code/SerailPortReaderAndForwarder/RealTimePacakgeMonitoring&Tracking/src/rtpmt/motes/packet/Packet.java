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
public class Packet extends Header {
    
    private ArrayList<String> PayLoad;
     
    final static int MOTE_DATA_TYPE = 16;
     
    final static int TEMPERATURE_INDEX = 1;
    
    
    public Packet(){
        PayLoad = new ArrayList<String>();
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
}
