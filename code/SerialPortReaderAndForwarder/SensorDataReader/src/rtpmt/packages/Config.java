/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.packages;

import rtpmt.network.packet.NetworkMessage;

/**
 *
 * @author Kumar
 */
public class Config {
    
    private double maxThreshold;
    private int maxRawThreshold;
    private double minThreshold;
    private int minRawThreshold;
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
    public double getMinThreshold(){
        return minThreshold;
    }
    public int getMinRawThreshold(){
        return minRawThreshold;
    }
    public double getMaxThreshold(){
        return maxThreshold;
    }
    public int getMaxRawThreshold(){
        return maxRawThreshold;
    }
   
    void setMinThresholdValue(double value) {
        minThreshold = value;
        switch(this.sensorType){
           case TEMPERATURE:
               minRawThreshold = getTemperatureThreshold(value);
               break;
            case HUMIDITY:
                minRawThreshold = getHumidityThreshold(value);
                break;
            case VIBRATION:
                minRawThreshold = getVibrationThreshold(value);
                break;
            case SHOCK:
                minRawThreshold = getShockThreshold(value);
               break;
       }
    }
    void setMinThresholdValue(int value) {        
        minRawThreshold = value;
        switch(this.sensorType){
           case TEMPERATURE:
               minThreshold = getTemperatureThreshold(value);
               break;
            case HUMIDITY:
                minThreshold = getHumidityThreshold(value);
                break;
            case VIBRATION:
                minThreshold = getVibrationThreshold(value);
                break;
            case SHOCK:
                minThreshold = getShockThreshold(value);
               break;
       }
    }
    void setMaxThresholdValue(double value) {
        maxThreshold = value;
        switch(this.sensorType){
           case TEMPERATURE:
               maxRawThreshold = getTemperatureThreshold(value);
               break;
            case HUMIDITY:
                maxRawThreshold = getHumidityThreshold(value);
                break;
            case VIBRATION:
                maxRawThreshold = getVibrationThreshold(value);
                break;
            case SHOCK:
                maxRawThreshold = getShockThreshold(value);
               break;
       }
    }
    void setMaxThresholdValue(int value) {
        maxRawThreshold = value;
        switch(this.sensorType){
           case TEMPERATURE:
               maxThreshold = getTemperatureThreshold(value);
               break;
            case HUMIDITY:
                maxThreshold = getHumidityThreshold(value);
                break;
            case VIBRATION:
                maxThreshold = getVibrationThreshold(value);
                break;
            case SHOCK:
                maxThreshold = getShockThreshold(value);
               break;
       }
    }
    
        
    /**
     * Conversion of threshold values from raw values to readable value and vice versa         
     * @param _threshold
     * @return 
     */
    
    private int getTemperatureThreshold(double _threshold){      
        return (int) ((((_threshold - 32.0)/1.8) + 50) * 32);
    }
    private int getHumidityThreshold(double _threshold){
        return (int) ((_threshold + 24) * 16); 
    }
    private int getVibrationThreshold(double _threshold){
        return (int) (_threshold/0.015625); 
    }
    private int getShockThreshold(double _threshold){
       return (int) (_threshold * 0.64); 
    }
    
    
    private double getTemperatureThreshold(int _threshold){      
      return (((_threshold/32.0)-50)*1.8)+32.0;
         
    }
    private double getHumidityThreshold(int _threshold){
       return  ((_threshold/16.0)- 24);
    }
    private double getVibrationThreshold(int _threshold){
        return (_threshold * 0.015625); 
    }
    private double getShockThreshold(int _threshold){
       return (_threshold / 0.64); 
    }
    
    public NetworkMessage.PackageInformation.Config getConfigMessage(){
       NetworkMessage.PackageInformation.Config.Builder config = NetworkMessage.PackageInformation.Config.newBuilder();
       
       switch(this.sensorType){
           case TEMPERATURE:
            config.setSensorType(NetworkMessage.PackageInformation.SensorType.TEMPERATURE);
            break;
           case HUMIDITY:
            config.setSensorType(NetworkMessage.PackageInformation.SensorType.HUMIDITY);
            break;
           case VIBRATION:
            config.setSensorType(NetworkMessage.PackageInformation.SensorType.VIBRATIONX);
            break;
           case SHOCK:
            config.setSensorType(NetworkMessage.PackageInformation.SensorType.SHOCKX);
            break;
       }
       config.setTimePeriod(this.timePeriod);
       config.setMaxThreshold(this.maxThreshold);
       config.setTimePeriodAfterThreshold(this.timePeriodAfterThershold);
       return config.build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Config other = (Config) obj;
        if (this.maxRawThreshold != other.maxRawThreshold) {
            return false;
        }
        if (this.minRawThreshold != other.minRawThreshold) {
            return false;
        }
        if (this.timePeriod != other.timePeriod) {
            return false;
        }
        return this.timePeriodAfterThershold == other.timePeriodAfterThershold;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.maxRawThreshold;
        hash = 79 * hash + this.minRawThreshold;
        hash = 79 * hash + this.timePeriod;
        hash = 79 * hash + this.timePeriodAfterThershold;
        return hash;
    }
    
    
}
