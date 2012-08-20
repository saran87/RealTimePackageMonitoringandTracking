/**
 * Sensor Information 
 *  Contains the information from a sensor
 */
package rtpt.network.packet;

/**
 *
 * @author Saravanakumar
 * @author Rochester Institute of Technology
 */
public class SensorInformation {
    
    private String sensorId;
     /**
     * sets the sensor id communicated to the device
     * @param value sensor id 
     */
    public void setSensorId(String value){
        this.sensorId = value;
    }
    
    /**
     * gets the sensor id
     * @return sensorid of the connected sensor
     */
    public String getSensorId(){
        return this.sensorId;
    }
    private String sensorType;
     /**
     * sets the SensorType
     * @param value SensorType
     */
    public void setSensorType(String value){
        this.sensorType = value;
    }
    
    /**
     * gets the SensorType
     * @return SensorType
     */
    public String getSensorType(){
        return this.sensorType;
    }
    private String sensorValue;
     /**
     * sets the SensorValue
     * @param value SensorValue
     */
    public void setSensorValue(String value){
        this.sensorValue = value;
    }
    
    /**
     * gets the SensorValue
     * @return SensorValue
     */
    public String getSensorValue(){
        return this.sensorValue;
    }
    
    
    private String sensorUnit;
     /**
     * sets the sensor unit
     * @param value sensor unit
     */
    public void setSensorUnit(String value){
        this.sensorUnit = value;
    }
    
    /**
     * gets the sensor unit
     * @return sensor unit
     */
    public String getSensorUnit(){
        return this.sensorUnit;
    }
    
}
