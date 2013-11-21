/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.packages;

/**
 *
 * @author Kumar
 */
public class Config {
    
    private int threshold;
    private int timePeriod;
    private int timePeriodAfterThershold;
    private final Sensor sensorType;
    
    public Config(Sensor sensor){
        this.sensorType = sensor;
    }
    
    void setTimePeriod(int secs) {
        this.timePeriod = secs;
    }
    void setAfterThresholdTimePeriod(int secs) {
        this.timePeriodAfterThershold = secs;
    }
    public int getTimePeriod(){
        return timePeriod;
    }
    public int getAfterThresholdTimePeriod(){
        return timePeriodAfterThershold;
    }
    public int getThreshold(){
        return threshold;
    }
   
    void setThresholdValue(double value) {
        switch(this.sensorType){
           case TEMPERATURE:
               setTemperatureThreshold(value);
               break;
            case HUMIDITY:
                setHumidityThreshold(value);
                break;
            case VIBRATION:
                setVibrationThreshold(value);
                break;
            case SHOCK:
                setShockThreshold(value);
               break;
       }
    }
    
    private void setTemperatureThreshold(double _threshold){      
        threshold  = (int) ((((_threshold - 32.0)/1.8) + 50) * 32);
    }
    private void setHumidityThreshold(double _threshold){
        threshold = (int) ((_threshold + 24) * 16); 
    }
    private void setVibrationThreshold(double _threshold){
        threshold = (int) (_threshold/0.015625); 
    }
    private void setShockThreshold(double _threshold){
        threshold = (int) (_threshold * 0.64); 
    }
    
}
