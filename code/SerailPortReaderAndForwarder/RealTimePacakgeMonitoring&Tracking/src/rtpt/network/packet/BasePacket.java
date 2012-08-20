/**
 * Base packet to hold device ID and 
 * time stamp of the packet
 */
package rtpt.network.packet;

import java.util.Date;

/**
 *
 * @author Saravana kumar
 */
public class BasePacket {
    
    //member to store device id 
    private String deviceId;
    
    /**
     * sets the device id
     * @param value  the device id
     */
    public void setDeviceId(String value){
        this.deviceId = value;
    }
    
    /**
     * gets the device id
     * @return deviceId from which the message is received
     */
    public String getDeviceId(){
        return this.deviceId;
    }
    
    //member to store timestamp of the current message 
    private Date timeStamp;
    
    /**
     * sets the device id
     * @param time 
     */
    public void setTimeStamp(Date time){
        this.timeStamp = time;
    }
    
    /**
     * sets the timestamp of the packet to current time
     */
    public void setTimeStamp(){
        this.timeStamp.setTime(System.currentTimeMillis());
    }
    /**
     * gets the packet timestamp
     * @return deviceId from which the packet is received
     */
    public Date getTimeStamp(){
        return this.timeStamp;
    }
}
