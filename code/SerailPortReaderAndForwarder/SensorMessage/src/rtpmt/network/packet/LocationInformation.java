/**
 * Location Information 
 * Contains the Location information of the device or the sensor 
 * 
 */
package rtpmt.network.packet;

/**
 *
 * @author Saravana Kumar 
 * @author Rochester Institute of Technology
 */
public class LocationInformation {
     //member to store latitude information 
    private double latitude;
    
    /**
     * sets the latitude of the location
     * @param value latitude of the location
     */
    public void setLatitude(double value){
        this.latitude = value;
    }
    
    /**
     * gets the latitude of the location
     * @return latitude of the location of the device from which the packet is received
     */
    public double getLatitude(){
        return this.latitude;
    }
    
    //member to store longitude of  location  
    private double longitude;
    
    /**
     * sets the longitude of the location
     * @param value  longitude of the location of the device from which the packet is received
     */
    public void setLongitude(double value){
        this.longitude = value;
    }
   
    /**
     * gets the longitude of the location
     * @return longitude of the location of the device from  which the packet is received
     */
    public double getLongitude(){
        return this.longitude;
    }
}
