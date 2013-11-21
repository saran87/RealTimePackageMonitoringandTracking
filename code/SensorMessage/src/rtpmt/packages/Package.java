/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.packages;

import java.util.HashMap;

/**
 *
 * @author Kumar
 */
public class Package {
    
    private long sensorId;
    
    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public HashMap<Sensor, Config> getConfigs() {
        return sensorConfigs;
    }
    
    private String packageId;
    private String truckId;
    private String comments;
    
    private final HashMap<Sensor, Config> sensorConfigs;
    
    public Package(){
        sensorConfigs = new HashMap<Sensor, Config>();
    }
   
    //Time Period Configuration
    public void setTemperatureTimePeriod(int secs){
        setTimePeriod(Sensor.TEMPERATURE,secs);
    }
    public void setHumididtyTimePeriod(int secs)
    {
         setTimePeriod(Sensor.HUMIDITY,secs);
    }
    public void setVibrationTimePeriod(int secs){         
        setTimePeriod(Sensor.VIBRATION,secs);
    }
    
    //Time Period Configuration
    public void setTemperatureAfterThresholdTimePeriod(int secs){
        setAfterThresholdTimePeriod(Sensor.TEMPERATURE,secs);
    }
    public void setHumididtyAfterThresholdTimePeriod(int secs)
    {
         setAfterThresholdTimePeriod(Sensor.HUMIDITY,secs);
    }
    public void setVibrationAfterThresholdTimePeriod(int secs){         
        setAfterThresholdTimePeriod(Sensor.VIBRATION,secs);
    }
    
    public void setTemperatureThreshold(double threshold){
         setThresholdValue(Sensor.TEMPERATURE,threshold);
    }
    public void setHumidtyThreshold(double threshold){
         setThresholdValue(Sensor.HUMIDITY,threshold);
    }
    public void setVibrationThreshold(double threshold){
        setThresholdValue(Sensor.VIBRATION,threshold);
    }
    
    public void setShockThreshold(double threshold){
        setThresholdValue(Sensor.SHOCK,threshold);
    }
    
    public void setThresholdValue(Sensor type, double value){
       
        if(this.sensorConfigs.containsKey(type)){
            Config config;
            config = this.sensorConfigs.get(type);
            config.setThresholdValue(value);
        }else{
            Config config = new Config(type);
            config.setThresholdValue(value);
            this.sensorConfigs.put(type, config);
        }
       
    }
    
    public void setTimePeriod(Sensor type, int secs){
        if(this.sensorConfigs.containsKey(type)){
            Config config;
            config = this.sensorConfigs.get(type);
            config.setTimePeriod(secs);
        }else{
            Config config = new Config(type);
            config.setTimePeriod(secs);
            this.sensorConfigs.put(type, config);
        }
    }

    private void setAfterThresholdTimePeriod(Sensor type, int secs) {
        if(this.sensorConfigs.containsKey(type)){
            Config config;
            config = this.sensorConfigs.get(type);
            config.setAfterThresholdTimePeriod(secs);
        }else{
            Config config = new Config(type);
            config.setAfterThresholdTimePeriod(secs);
            this.sensorConfigs.put(type, config);
        }
    }
    
}
