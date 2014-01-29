/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.packages;

import java.util.Date;
import java.util.HashMap;
import rtpmt.network.packet.NetworkMessage;
import rtpmt.sensor.util.Constants;

/**
 *
 * @author Kumar
 */
public class Package {
    
    private int shortId;
    private String sensorId;
    private String packageId;
    private String truckId;
    private String comments;
    private boolean isFlashDataAvailable;
    private int batteryLevel;
    private int batteryHealth;
    private int maxVibXaxis;
    private int maxVibYaxis;
    private int maxVibZaxis;
    private int minVibXaxis;
    private int minVibYaxis;
    private int minVibZaxis;
    private int offsetVibXaxis;
    private int offsetVibYaxis;
    private int offsetVibZaxis;

    public boolean isIsFlashDataAvailable() {
        return isFlashDataAvailable;
    }

    public void setIsFlashDataAvailable(boolean isFlashDataAvailable) {
        this.isFlashDataAvailable = isFlashDataAvailable;
    }

    public int getMaxVibXaxis() {
        return maxVibXaxis;
    }

    public void setMaxVibXaxis(int maxVibXaxis) {
        this.maxVibXaxis = maxVibXaxis;
    }

    public int getMaxVibYaxis() {
        return maxVibYaxis;
    }

    public void setMaxVibYaxis(int maxVibYaxis) {
        this.maxVibYaxis = maxVibYaxis;
    }

    public int getMaxVibZaxis() {
        return maxVibZaxis;
    }

    public void setMaxVibZaxis(int maxVibZaxis) {
        this.maxVibZaxis = maxVibZaxis;
    }

    public int getMinVibXaxis() {
        return minVibXaxis;
    }

    public void setMinVibXaxis(int minVibXaxis) {
        this.minVibXaxis = minVibXaxis;
    }

    public int getMinVibYaxis() {
        return minVibYaxis;
    }

    public void setMinVibYaxis(int minVibYaxis) {
        this.minVibYaxis = minVibYaxis;
    }

    public int getMinVibZaxis() {
        return minVibZaxis;
    }

    public void setMinVibZaxis(int minVibZaxis) {
        this.minVibZaxis = minVibZaxis;
    }

    public int getOffsetVibXaxis() {
        return offsetVibXaxis;
    }

    public void setOffsetVibXaxis(int offsetVibXaxis) {
        this.offsetVibXaxis = offsetVibXaxis;
    }

    public int getOffsetVibYaxis() {
        return offsetVibYaxis;
    }

    public void setOffsetVibYaxis(int offsetVibYaxis) {
        this.offsetVibYaxis = offsetVibYaxis;
    }

    public int getOffsetVibZaxis() {
        return offsetVibZaxis;
    }

    public void setOffsetVibZaxis(int offsetVibZaxis) {
        this.offsetVibZaxis = offsetVibZaxis;
    }
    
    

    public void setIsFlashDataAvailable(int data) {
        this.isFlashDataAvailable = data != 0;
    }

    public boolean isFlashDataAvailable() {
        return isFlashDataAvailable;
    }
    
    private final HashMap<Sensor, Config> sensorConfigs;
    
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId.toUpperCase();
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId.toUpperCase();
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public String getNote() {
        String note = this.truckId + "-" + this.packageId + "-" + this.comments;
        return note;
    }
    
    public void setNote(String note) {
      if(note!=null && !note.isEmpty()){
         String[] noteArray = note.split("-");
         if(noteArray.length>2){
            this.setTruckId(noteArray[0]);
            this.setPackageId(noteArray[1]);
            this.setComments(noteArray[2]);
         }
      }
    }
    
    public void setBatteryLevel(int batteryLevel){
        this.batteryLevel = batteryLevel;
    }
    
    public int getBatteryLevel(){
        return this.batteryLevel;
    }
    
    public void setBatteryHealth(int batteryHealth){
        this.batteryHealth = batteryHealth;
    }
    
    public int getBatteryHealth(){
        return this.batteryHealth;
    }
   
    
    public String getUniqueId() {
        return  this.sensorId + Constants.SEPERATOR + this.truckId + Constants.SEPERATOR + this.packageId;
    }

    public HashMap<Sensor, Config> getConfigs() {
        return sensorConfigs;
    }   
    
    public Package(){
        sensorConfigs = new HashMap<Sensor, Config>();
    }
    /**
     * 
     * @param _shortId
     * @param macId 
     */
    public Package(int _shortId,String macId){
        sensorConfigs = new HashMap<Sensor, Config>();
        this.shortId = _shortId;
        this.sensorId = macId;
    }
    /**
     * 
     * @param macId
     * @param packageId
     * @param truckId 
     */
    public Package(String macId,String packageId,String truckId){
        sensorConfigs = new HashMap<Sensor, Config>();
        this.sensorId = macId;
        this.packageId = packageId.toUpperCase();
        this.truckId = truckId.toUpperCase();
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
    /**
     * 
     * @param threshold 
     */
    public void setMinTemperatureThreshold(double threshold){
         setMinThresholdValue(Sensor.TEMPERATURE,threshold);
    }
    public void setMinHumidtyThreshold(double threshold){
         setMinThresholdValue(Sensor.HUMIDITY,threshold);
    }
    public void setMinVibrationThreshold(double threshold){
        setMinThresholdValue(Sensor.VIBRATION,threshold);
    }
    
    public void setMinShockThreshold(double threshold){
        setMinThresholdValue(Sensor.SHOCK,threshold);
    }
    /**
     * 
     * @param threshold 
     */
    public void setMinTemperatureThreshold(int threshold){
         setMinThresholdValue(Sensor.TEMPERATURE,threshold);
    }
    public void setMinHumidtyThreshold(int threshold){
         setMinThresholdValue(Sensor.HUMIDITY,threshold);
    }
    public void setMinVibrationThreshold(int threshold){
        setMinThresholdValue(Sensor.VIBRATION,threshold);
    }
    
    public void setMinShockThreshold(int threshold){
        setMinThresholdValue(Sensor.SHOCK,threshold);
    }
    /**
     * 
     * @param threshold 
     */
    public void setMaxTemperatureThreshold(double threshold){
         setMaxThresholdValue(Sensor.TEMPERATURE,threshold);
    }
    public void setMaxHumidtyThreshold(double threshold){
         setMaxThresholdValue(Sensor.HUMIDITY,threshold);
    }
    public void setMaxVibrationThreshold(double threshold){
        setMaxThresholdValue(Sensor.VIBRATION,threshold);
    }
    
    public void setMaxShockThreshold(double threshold){
        setMaxThresholdValue(Sensor.SHOCK,threshold);
    }
    
    /**
     * 
     * @param threshold 
     */
    public void setMaxTemperatureThreshold(int threshold){
         setMaxThresholdValue(Sensor.TEMPERATURE,threshold);
    }
    public void setMaxHumidtyThreshold(int threshold){
         setMaxThresholdValue(Sensor.HUMIDITY,threshold);
    }
    public void setMaxVibrationThreshold(int threshold){
        setMaxThresholdValue(Sensor.VIBRATION,threshold);
    }
    
    public void setMaxShockThreshold(int threshold){
        setMaxThresholdValue(Sensor.SHOCK,threshold);
    }
    
    /**
     * 
     * @param type
     * @param value 
     */
    public void setMinThresholdValue(Sensor type, double value){
       
        if(this.sensorConfigs.containsKey(type)){
            Config config;
            config = this.sensorConfigs.get(type);
            config.setMinThresholdValue(value);
        }else{
            Config config = new Config(type);
            config.setMinThresholdValue(value);
            this.sensorConfigs.put(type, config);
        }
    }
    
    public void setMinThresholdValue(Sensor type, int value){
       
        if(this.sensorConfigs.containsKey(type)){
            Config config;
            config = this.sensorConfigs.get(type);
            config.setMinThresholdValue(value);
        }else{
            Config config = new Config(type);
            config.setMinThresholdValue(value);
            this.sensorConfigs.put(type, config);
        }
    }
    /**
     * 
     * @param type
     * @param value 
     */
    public void setMaxThresholdValue(Sensor type, double value){
       
        if(this.sensorConfigs.containsKey(type)){
            Config config;
            config = this.sensorConfigs.get(type);
            config.setMaxThresholdValue(value);
        }else{
            Config config = new Config(type);
            config.setMaxThresholdValue(value);
            this.sensorConfigs.put(type, config);
        }
       
    }
    
   /**
    * 
    * @param type
    * @param value 
    */
    public void setMaxThresholdValue(Sensor type, int value){
       
        if(this.sensorConfigs.containsKey(type)){
            Config config;
            config = this.sensorConfigs.get(type);
            config.setMaxThresholdValue(value);
        }else{
            Config config = new Config(type);
            config.setMaxThresholdValue(value);
            this.sensorConfigs.put(type, config);
        }
       
    }
    
    /**
     * 
     * @param type
     * @param secs 
     */
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
   
    /**
     * 
     * @param type
     * @param secs 
     */
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

    /**
     * @return the shortId
     */
    public int getShortId() {
        return shortId;
    }

    /**
     * @param shortId the shortId to set
     */
    public void setShortId(int shortId) {
        this.shortId = shortId;
    }
    
    public byte[] getBlackBoxConfigPacket(){
        byte[] configPacket = getDefaultConfigPacket();
        if(this.sensorConfigs.containsKey(Sensor.TEMPERATURE)){
            Config config = this.sensorConfigs.get(Sensor.TEMPERATURE);
            configPacket[Constants.TEMP_MAX_THRES_INDEX] = (byte)(config.getMaxRawThreshold() & 0xff);
            configPacket[Constants.TEMP_MAX_THRES_INDEX+1] = (byte)(config.getMaxRawThreshold() >> 8) ;
            
            configPacket[Constants.TEMP_TIME_INDEX] = (byte)(config.getTimePeriod()& 0xff);
            configPacket[Constants.TEMP_TIME_INDEX+1] = (byte)(config.getTimePeriod() >> 8) ; 
            
            configPacket[Constants.TEMP_TIME_OVER_THRES_INDEX] = (byte)(config.getAfterThresholdTimePeriod()& 0xff);
            configPacket[Constants.TEMP_TIME_OVER_THRES_INDEX+1] = (byte)(config.getAfterThresholdTimePeriod() >> 8) ; 
        }
        
        if(this.sensorConfigs.containsKey(Sensor.HUMIDITY)){
            Config config = this.sensorConfigs.get(Sensor.HUMIDITY);
            configPacket[Constants.HUMD_MAX_THRES_INDEX] = (byte)(config.getMaxRawThreshold() & 0xff);
            configPacket[Constants.HUMD_MAX_THRES_INDEX+1] = (byte)(config.getMaxRawThreshold() >> 8) ;
            
            configPacket[Constants.HUMD_TIME_INDEX] = (byte)(config.getTimePeriod()& 0xff);
            configPacket[Constants.HUMD_TIME_INDEX+1] = (byte)(config.getTimePeriod() >> 8) ; 
            
            configPacket[Constants.HUMD_TIME_OVER_THRES_INDEX] = (byte)(config.getAfterThresholdTimePeriod()& 0xff);
            configPacket[Constants.HUMD_TIME_OVER_THRES_INDEX+1] = (byte)(config.getAfterThresholdTimePeriod() >> 8) ; 
        }
        
        if(this.sensorConfigs.containsKey(Sensor.VIBRATION)){
            Config config = this.sensorConfigs.get(Sensor.VIBRATION);
            configPacket[Constants.VIB_X_MAX_THRES_INDEX] = (byte)(config.getMaxRawThreshold() & 0xff);
            configPacket[Constants.VIB_X_MAX_THRES_INDEX+1] = (byte)(config.getMaxRawThreshold() >> 8) ;
            //for Y-Axis
            configPacket[Constants.VIB_Y_MAX_THRES_INDEX] = (byte)(config.getMaxRawThreshold() & 0xff);
            configPacket[Constants.VIB_Y_MAX_THRES_INDEX+1] = (byte)(config.getMaxRawThreshold() >> 8) ;
            //for z-axis
            configPacket[Constants.VIB_Z_MAX_THRES_INDEX] = (byte)(config.getMaxRawThreshold() & 0xff);
            configPacket[Constants.VIB_Z_MAX_THRES_INDEX+1] = (byte)(config.getMaxRawThreshold() >> 8) ;
            
            configPacket[Constants.VIB_X_TIME_INDEX] = (byte)(config.getTimePeriod()& 0xff);
            configPacket[Constants.VIB_X_TIME_INDEX+1] = (byte)(config.getTimePeriod() >> 8) ; 
            configPacket[Constants.VIB_Y_TIME_INDEX] = (byte)(config.getTimePeriod()& 0xff);
            configPacket[Constants.VIB_Y_TIME_INDEX+1] = (byte)(config.getTimePeriod() >> 8) ; 
            configPacket[Constants.VIB_Z_TIME_INDEX] = (byte)(config.getTimePeriod()& 0xff);
            configPacket[Constants.VIB_Z_TIME_INDEX+1] = (byte)(config.getTimePeriod() >> 8) ; 
            
            configPacket[Constants.VIB_X_TIME_OVER_THRES_INDEX] = (byte)(config.getAfterThresholdTimePeriod()& 0xff);
            configPacket[Constants.VIB_X_TIME_OVER_THRES_INDEX+1] = (byte)(config.getAfterThresholdTimePeriod() >> 8);
            configPacket[Constants.VIB_Y_TIME_OVER_THRES_INDEX] = (byte)(config.getAfterThresholdTimePeriod()& 0xff);
            configPacket[Constants.VIB_Y_TIME_OVER_THRES_INDEX+1] = (byte)(config.getAfterThresholdTimePeriod() >> 8) ;
            configPacket[Constants.VIB_Z_TIME_OVER_THRES_INDEX] = (byte)(config.getAfterThresholdTimePeriod()& 0xff);
            configPacket[Constants.VIB_Z_TIME_OVER_THRES_INDEX+1] = (byte)(config.getAfterThresholdTimePeriod() >> 8) ;
        }
        
        if(this.sensorConfigs.containsKey(Sensor.SHOCK)){
            Config config = this.sensorConfigs.get(Sensor.SHOCK);
            configPacket[Constants.SHOCK_X_MAX_THRES_INDEX] = (byte)(config.getMaxRawThreshold() & 0xff);
            configPacket[Constants.SHOCK_X_MAX_THRES_INDEX+1] = (byte)(config.getMaxRawThreshold() >> 8);
            //for y-axis
            configPacket[Constants.SHOCK_Y_MAX_THRES_INDEX] = (byte)(config.getMaxRawThreshold() & 0xff);
            configPacket[Constants.SHOCK_Y_MAX_THRES_INDEX+1] = (byte)(config.getMaxRawThreshold() >> 8);
            //for z-axis
            configPacket[Constants.SHOCK_Z_MAX_THRES_INDEX] = (byte)(config.getMaxRawThreshold() & 0xff);
            configPacket[Constants.SHOCK_Z_MAX_THRES_INDEX+1] = (byte)(config.getMaxRawThreshold() >> 8);
        }
        return configPacket;
    }
    
    public byte[] getDefaultConfigPacket(){
        byte[] configPacket = new byte[Constants.P_CONFIG_SIZE];
        
        configPacket[Constants.TEMP_MAX_THRES_INDEX] = (byte)(Constants.DEFAULT_TEMP_MAX_THRES & 0xff);
        configPacket[Constants.TEMP_MAX_THRES_INDEX+1] = (byte)(Constants.DEFAULT_TEMP_MAX_THRES >> 8) ;

        configPacket[Constants.TEMP_MIN_THRES_INDEX] = (byte)(Constants.DEFAULT_TEMP_MIN_THRES & 0xff);
        configPacket[Constants.TEMP_MIN_THRES_INDEX+1] = (byte)(Constants.DEFAULT_TEMP_MIN_THRES >> 8);
        
        configPacket[Constants.TEMP_TIME_INDEX] = (byte)(Constants.DEFAULT_TEMP_TIME & 0xff);
        configPacket[Constants.TEMP_TIME_INDEX+1] = (byte)(Constants.DEFAULT_TEMP_TIME >> 8) ; 

        configPacket[Constants.TEMP_TIME_OVER_THRES_INDEX] = (byte)(Constants.DEFAULT_TEMP_TIME_OVER_THRES & 0xff);
        configPacket[Constants.TEMP_TIME_OVER_THRES_INDEX+1] = (byte)(Constants.DEFAULT_TEMP_TIME_OVER_THRES >> 8) ; 
        
        configPacket[Constants.TEMP_UNITS] = (byte)(Constants.DEFAULT_TEMP_UNITS);//Just one byte
        configPacket[Constants.TEMP_UNIT_MAXIMUM] = (byte)(Constants.DEFAULT_TEMP_UNIT_MAXIMUM & 0xff);
        configPacket[Constants.TEMP_UNIT_MAXIMUM+1] = (byte)(Constants.DEFAULT_TEMP_UNIT_MAXIMUM & 0xff);
        
        configPacket[Constants.TEMP_UNIT_MINIMUM] = (byte)(Constants.DEFAULT_TEMP_UNIT_MINIMUM & 0xff);
        configPacket[Constants.TEMP_UNIT_MINIMUM+1] = (byte)(Constants.DEFAULT_TEMP_UNIT_MINIMUM & 0xff);
        
        //Humditity 
        configPacket[Constants.HUMD_MAX_THRES_INDEX] = (byte)(Constants.DEFAULT_HUMD_MAX_THRES & 0xff);
        configPacket[Constants.HUMD_MAX_THRES_INDEX+1] = (byte)(Constants.DEFAULT_HUMD_MAX_THRES >> 8) ;

        configPacket[Constants.HUMD_MIN_THRES_INDEX] = (byte)(Constants.DEFAULT_HUMD_MIN_THRES & 0xff);
        configPacket[Constants.HUMD_MIN_THRES_INDEX+1] = (byte)(Constants.DEFAULT_HUMD_MIN_THRES >> 8);
        
        configPacket[Constants.HUMD_TIME_INDEX] = (byte)(Constants.DEFAULT_HUMD_TIME & 0xff);
        configPacket[Constants.HUMD_TIME_INDEX+1] = (byte)(Constants.DEFAULT_HUMD_TIME >> 8) ; 

        configPacket[Constants.HUMD_TIME_OVER_THRES_INDEX] = (byte)(Constants.DEFAULT_HUMD_TIME_OVER_THRES & 0xff);
        configPacket[Constants.HUMD_TIME_OVER_THRES_INDEX+1] = (byte)(Constants.DEFAULT_HUMD_TIME_OVER_THRES >> 8) ; 
        
        configPacket[Constants.HUMD_UNITS] = (byte)(Constants.DEFAULT_HUMD_UNITS);//Just one byte
        configPacket[Constants.HUMD_UNIT_MAXIMUM] = (byte)(Constants.DEFAULT_HUMD_UNIT_MAXIMUM & 0xff);
        configPacket[Constants.HUMD_UNIT_MAXIMUM+1] = (byte)(Constants.DEFAULT_HUMD_UNIT_MAXIMUM >> 8);
        
        configPacket[Constants.HUMD_UNIT_MINIMUM] = (byte)(Constants.DEFAULT_HUMD_UNIT_MINIMUM & 0xff);
        configPacket[Constants.HUMD_UNIT_MINIMUM+1] = (byte)(Constants.DEFAULT_HUMD_UNIT_MINIMUM >> 8);
        
        //Shock 
        configPacket[Constants.SHOCK_X_MAX_THRES_INDEX] = (byte)(Constants.DEFAULT_SHOCK_X_MAX_THRES & 0xff);
        configPacket[Constants.SHOCK_X_MAX_THRES_INDEX+1] = (byte)(Constants.DEFAULT_SHOCK_X_MAX_THRES >> 8);
        //for y-axis
        configPacket[Constants.SHOCK_Y_MAX_THRES_INDEX] = (byte)(Constants.DEFAULT_SHOCK_Y_MAX_THRES & 0xff);
        configPacket[Constants.SHOCK_Y_MAX_THRES_INDEX+1] = (byte)(Constants.DEFAULT_SHOCK_Y_MAX_THRES >> 8);
        //for z-axis
        configPacket[Constants.SHOCK_Z_MAX_THRES_INDEX] = (byte)(Constants.DEFAULT_SHOCK_Z_MAX_THRES & 0xff);
        configPacket[Constants.SHOCK_Z_MAX_THRES_INDEX+1] = (byte)(Constants.DEFAULT_SHOCK_Z_MAX_THRES>> 8);

        configPacket[Constants.SHOCK_X_UNIT_INDEX] = (byte)(Constants.DEFAULT_SHOCK_X_UNIT & 0xff);
        configPacket[Constants.SHOCK_Y_UNIT_INDEX] = (byte)(Constants.DEFAULT_SHOCK_Y_UNIT & 0xff);
        configPacket[Constants.SHOCK_Z_UNIT_INDEX] = (byte)(Constants.DEFAULT_SHOCK_Z_UNIT & 0xff);
        configPacket[Constants.SHOCK_X_MAXIMUM_INDEX] = (byte)(Constants.DEFAULT_SHOCK_X_MAXIMUM & 0xff);
        configPacket[Constants.SHOCK_X_MAXIMUM_INDEX+1] = (byte)(Constants.DEFAULT_SHOCK_X_MAXIMUM >> 8);
        configPacket[Constants.SHOCK_Y_MAXIMUM_INDEX] = (byte)(Constants.DEFAULT_SHOCK_Y_MAXIMUM & 0xff);
        configPacket[Constants.SHOCK_Y_MAXIMUM_INDEX+1] = (byte)(Constants.DEFAULT_SHOCK_Y_MAXIMUM >> 8);
        configPacket[Constants.SHOCK_Z_MAXIMUM_INDEX] = (byte)(Constants.DEFAULT_SHOCK_Z_MAXIMUM & 0xff);
        configPacket[Constants.SHOCK_Z_MAXIMUM_INDEX+1] = (byte)(Constants.DEFAULT_SHOCK_Z_MAXIMUM >> 8);
        configPacket[Constants.SHOCK_X_MINIMUM_INDEX] = (byte)(Constants.DEFAULT_SHOCK_X_MINIMUM & 0xff);
        configPacket[Constants.SHOCK_X_MINIMUM_INDEX+1] = (byte)(Constants.DEFAULT_SHOCK_X_MINIMUM >> 8);
        configPacket[Constants.SHOCK_Y_MINIMUM_INDEX] = (byte)(Constants.DEFAULT_SHOCK_Y_MINIMUM & 0xff);
        configPacket[Constants.SHOCK_Y_MINIMUM_INDEX+1] = (byte)(Constants.DEFAULT_SHOCK_Y_MINIMUM >> 8);
        configPacket[Constants.SHOCK_Z_MINIMUM_INDEX] = (byte)(Constants.DEFAULT_SHOCK_Z_MINIMUM & 0xff);
        configPacket[Constants.SHOCK_Z_MINIMUM_INDEX+1] = (byte)(Constants.DEFAULT_SHOCK_Z_MINIMUM >> 8);
        configPacket[Constants.SHOCK_FREE_FALL_THRESHOLD_INDEX] = (byte)(Constants.DEFAULT_SHOCK_FREE_FALL_THRESHOLD & 0xff);
        configPacket[Constants.SHOCK_FREE_FALL_TIME_INDEX] = (byte)(Constants.DEFAULT_SHOCK_FREE_FALL_TIME & 0xff);
        configPacket[Constants.SHOCK_ACTIVITY_THRESHOLD_INDEX] = (byte)(Constants.DEFAULT_SHOCK_ACTIVITY_THRESHOLD & 0xff);
        
        configPacket[Constants.VIB_X_MAX_THRES_INDEX] = (byte)(Constants.DEFAULT_VIB_X_MAX_THRES & 0xff);
        configPacket[Constants.VIB_X_MAX_THRES_INDEX+1] = (byte)(Constants.DEFAULT_VIB_X_MAX_THRES >> 8) ;
        //for Y-Axis
        configPacket[Constants.VIB_Y_MAX_THRES_INDEX] = (byte)(Constants.DEFAULT_VIB_Y_MAX_THRES & 0xff);
        configPacket[Constants.VIB_Y_MAX_THRES_INDEX+1] = (byte)(Constants.DEFAULT_VIB_Y_MAX_THRES >> 8) ;
        //for z-axis
        configPacket[Constants.VIB_Z_MAX_THRES_INDEX] = (byte)(Constants.DEFAULT_VIB_Z_MAX_THRES & 0xff);
        configPacket[Constants.VIB_Z_MAX_THRES_INDEX+1] = (byte)(Constants.DEFAULT_VIB_Z_MAX_THRES >> 8) ;

        configPacket[Constants.VIB_X_TIME_INDEX] = (byte)(Constants.DEFAULT_VIB_X_TIME & 0xff);
        configPacket[Constants.VIB_X_TIME_INDEX+1] = (byte)(Constants.DEFAULT_VIB_X_TIME >> 8) ; 
        configPacket[Constants.VIB_Y_TIME_INDEX] = (byte)(Constants.DEFAULT_VIB_Y_TIME& 0xff);
        configPacket[Constants.VIB_Y_TIME_INDEX+1] = (byte)(Constants.DEFAULT_VIB_Y_TIME >> 8) ; 
        configPacket[Constants.VIB_Z_TIME_INDEX] = (byte)(Constants.DEFAULT_VIB_Z_TIME& 0xff);
        configPacket[Constants.VIB_Z_TIME_INDEX+1] = (byte)(Constants.DEFAULT_VIB_Z_TIME >> 8) ; 

        configPacket[Constants.VIB_X_TIME_OVER_THRES_INDEX] = (byte)(Constants.DEFAULT_VIB_X_TIME_OVER_THRES & 0xff);
        configPacket[Constants.VIB_X_TIME_OVER_THRES_INDEX+1] = (byte)(Constants.DEFAULT_VIB_X_TIME_OVER_THRES >> 8);
        configPacket[Constants.VIB_Y_TIME_OVER_THRES_INDEX] = (byte)(Constants.DEFAULT_VIB_Y_TIME_OVER_THRES & 0xff);
        configPacket[Constants.VIB_Y_TIME_OVER_THRES_INDEX+1] = (byte)(Constants.DEFAULT_VIB_Y_TIME_OVER_THRES >> 8) ;
        configPacket[Constants.VIB_Z_TIME_OVER_THRES_INDEX] = (byte)(Constants.DEFAULT_VIB_Z_TIME_OVER_THRES & 0xff);
        configPacket[Constants.VIB_Z_TIME_OVER_THRES_INDEX+1] = (byte)(Constants.DEFAULT_VIB_Z_TIME_OVER_THRES >> 8) ;

        configPacket[Constants.VIB_X_UNIT_INDEX]= (byte)(Constants.DEFAULT_VIB_X_UNIT & 0xff);
        configPacket[Constants.VIB_Y_UNIT_INDEX] = (byte)(Constants.DEFAULT_VIB_Y_UNIT & 0xff);
        configPacket[Constants.VIB_Z_UNIT_INDEX] = (byte)(Constants.DEFAULT_VIB_Z_UNIT & 0xff);
        configPacket[Constants.VIB_X_MAXIMUM_INDEX] = (byte)(Constants.DEFAULT_VIB_X_MAXIMUM & 0xff);
        configPacket[Constants.VIB_X_MAXIMUM_INDEX+1] = (byte)(Constants.DEFAULT_VIB_X_MAXIMUM >> 8);
        configPacket[Constants.VIB_Y_MAXIMUM_INDEX] = (byte)(Constants.DEFAULT_VIB_Y_MAXIMUM & 0xff);
        configPacket[Constants.VIB_Y_MAXIMUM_INDEX+1] = (byte)(Constants.DEFAULT_VIB_Y_MAXIMUM >> 8);
        configPacket[Constants.VIB_Z_MAXIMUM_INDEX] = (byte)(Constants.DEFAULT_VIB_Z_MAXIMUM & 0xff);
        configPacket[Constants.VIB_Z_MAXIMUM_INDEX+1] = (byte)(Constants.DEFAULT_VIB_Z_MAXIMUM >> 8);
        configPacket[Constants.VIB_X_MINIMUM_INDEX] = (byte)(Constants.DEFAULT_VIB_X_MINIMUM & 0xff);
        configPacket[Constants.VIB_X_MINIMUM_INDEX+1] = (byte)(Constants.DEFAULT_VIB_X_MINIMUM >> 8);
        configPacket[Constants.VIB_Y_MINIMUM_INDEX] = (byte)(Constants.DEFAULT_VIB_Y_MINIMUM & 0xff);
        configPacket[Constants.VIB_Y_MINIMUM_INDEX+1] = (byte)(Constants.DEFAULT_VIB_Y_MINIMUM >> 8);
        configPacket[Constants.VIB_Z_MINIMUM_INDEX] = (byte)(Constants.DEFAULT_VIB_Z_MINIMUM & 0xff);
        configPacket[Constants.VIB_Z_MINIMUM_INDEX+1] = (byte)(Constants.DEFAULT_VIB_Z_MINIMUM >> 8);
        configPacket[Constants.VIB_X_OFFSET_INDEX] = (byte)(Constants.DEFAULT_VIB_X_OFFSET & 0xff);
        configPacket[Constants.VIB_Y_OFFSET_INDEX] = (byte)(Constants.DEFAULT_VIB_Y_OFFSET  & 0xff);
        configPacket[Constants.VIB_Z_OFFSET_INDEX] = (byte)(Constants.DEFAULT_VIB_Z_OFFSET  & 0xff);
        configPacket[Constants.VIB_INACTIVITY_THRES_INDEX] = (byte)(Constants.DEFAULT_VIB_INACTIVITY_THRES  & 0xff);
        configPacket[Constants.VIB_INACTIVITY_TIME] = (byte)(Constants.DEFAULT_VIB_INACTIVITY_TIME  & 0xff);
        configPacket[Constants.VIB_TAP_THRESHOLD] = (byte)(Constants.DEFAULT_VIB_TAP_THRESHOLD  & 0xff);
        
         //Radio Config 
        configPacket[Constants.RADIO_RESTART_DELAY_INDEX] = (byte)(Constants.DEFAULT_RADIO_RESTART_DELAY  & 0xff);
        configPacket[Constants.RADIO_MAX_RETRIES_INDEX] = (byte)(Constants.DEFAULT_RADIO_MAX_RETRIES  & 0xff);
        configPacket[Constants.RADIO_MAX_FAILURES_INDEX]= (byte)(Constants.DEFAULT_RADIO_MAX_FAILURES  & 0xff);
        configPacket[Constants.RADIO_PANID_INDEX] = (byte)(Constants.DEFAULT_RADIO_PANID  & 0xff);
        configPacket[Constants.RADIO_PANID_INDEX + 1] = (byte)(Constants.DEFAULT_RADIO_PANID >> 8);

        //misc config
        configPacket[Constants.CONFIG_VIB_SHOCk_BOTH_INDEX] = (byte)(Constants.DEFAULT_CONFIG_VIB_SHOCk_BOTH & 0xff);
        configPacket[Constants.DATA_IN_FLASH_INDEX] = (byte)(Constants.DEFAULT_DATA_IN_FLASH & 0xff);
         
        return configPacket;
    }
    
    
    public NetworkMessage.PackageInformation getConfigMessage(boolean isRealTime) {
 
            NetworkMessage.PackageInformation.Builder message = NetworkMessage.PackageInformation.newBuilder();
            //compulsary information
            message.setIsRealTime(isRealTime);
            message.setMessageType(NetworkMessage.PackageInformation.MessageType.CONFIG);
            message.setTimeStamp(new Date().getTime());
           
            if(this.getSensorId()!= null &&  !this.getSensorId().isEmpty()){
                message.setSensorId(this.getSensorId());
            }else{
                message.setSensorId(Constants.NO_ID);
            }
            if(this.getPackageId() != null ){
                message.setPackageId(this.getPackageId());
            }else{
                message.setPackageId(Constants.NO_ID);
            }
            if(this.getTruckId() != null ){  
                message.setTruckId(this.getTruckId()) ;
            }else{
                message.setTruckId(Constants.NO_ID);
            }
            if(this.getComments() != null ){ 
               message.setComments(this.getComments());
            }else{
                message.setComments(Constants.NO_ID);
            }

            NetworkMessage.PackageInformation.Config.Builder config = NetworkMessage.PackageInformation.Config.newBuilder();

            if (this.getConfigs().containsKey(Sensor.TEMPERATURE)) {  
               message.addConfigs(this.getConfigs().get(Sensor.TEMPERATURE).getConfigMessage()); 
            } 
            if (this.getConfigs().containsKey(Sensor.HUMIDITY)) {  
               message.addConfigs(this.getConfigs().get(Sensor.HUMIDITY).getConfigMessage()); 
            } 
            if (this.getConfigs().containsKey(Sensor.VIBRATION)) {  
               message.addConfigs(this.getConfigs().get(Sensor.VIBRATION).getConfigMessage()); 
            } 
            if (this.getConfigs().containsKey(Sensor.SHOCK)) {  
               message.addConfigs(this.getConfigs().get(Sensor.SHOCK).getConfigMessage()); 
            } 

            
            return message.build();
    }
    
    
    /**
     * 
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Package other = (Package) obj;
        if (this.shortId != other.shortId) {
            return false;
        }
        if (this.sensorId == null ? other.sensorId != null : !this.sensorId.equals(other.sensorId)) {
            return false;
        }
        if ((this.packageId == null) ? (other.packageId != null) : !this.packageId.equals(other.packageId)) {
            return false;
        }
        if ((this.truckId == null) ? (other.truckId != null) : !this.truckId.equals(other.truckId)) {
            return false;
        }
        if ((this.comments == null) ? (other.comments != null) : !this.comments.equals(other.comments)) {
            return false;
        }
        if ( (this.sensorConfigs == null || other.sensorConfigs == null)) {
            return false;
        }
        
        Config config1 = this.sensorConfigs.get(Sensor.TEMPERATURE);
        Config config2 = this.sensorConfigs.get(Sensor.TEMPERATURE);
        if(!config1.equals(config2)) return false;
        config1 = this.sensorConfigs.get(Sensor.HUMIDITY);
        config2 = this.sensorConfigs.get(Sensor.HUMIDITY);
        if(!config1.equals(config2)) return false;
        config1 = this.sensorConfigs.get(Sensor.VIBRATION);
        config2 = this.sensorConfigs.get(Sensor.VIBRATION);
        if(!config1.equals(config2)) return false;
        config1 = this.sensorConfigs.get(Sensor.SHOCK);
        config2 = this.sensorConfigs.get(Sensor.SHOCK);
        
        return config1.equals(config2);
    }
    
   /**
    * 
    * @return 
    */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.shortId;
        hash = 17 * hash + (this.sensorId != null ? this.sensorId.hashCode() : 0);
        hash = 17 * hash + (this.packageId != null ? this.packageId.hashCode() : 0);
        hash = 17 * hash + (this.truckId != null ? this.truckId.hashCode() : 0);
        hash = 17 * hash + (this.comments != null ? this.comments.hashCode() : 0);
        hash = 17 * hash + (this.sensorConfigs != null ? this.sensorConfigs.hashCode() : 0);
        return hash;
    }
    
    
}
