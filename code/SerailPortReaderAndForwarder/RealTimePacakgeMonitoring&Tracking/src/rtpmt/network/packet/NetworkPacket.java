/**
 * Network Packet
 * Contains the packet structure of the message
 * This packet is send over the network from tcp client to tcp server
 */
package rtpmt.network.packet;

import java.util.ArrayList;

/**
 *
 * @author Saravnakumar
 * @author Rochester Institute of Technology
 */
public class NetworkPacket extends BasePacket {
    
    private LocationInformation location;
    /**
     * sets Location information to the network packet
     * @param loc Location Information
     */
    public void setLocationInformation(LocationInformation loc){
        this.location = loc;
    }
    
    /**
     * gets the location information from the network packet
     * @return location information object
     * @see LocationInformation
     */
    public LocationInformation getLocationInformation(){
        return this.location;
    }
    
    private ArrayList<SensorInformation> sensorList;
     /**
     * sets Sensor information list to the network packet
     * @param sensorList Information list
     */
    public void setSensorList( ArrayList<SensorInformation> sensorList){
        this.sensorList = sensorList;
    }
    /**
     * gets the Sensor information list from the network packet
     * @return Sensor information list object
     * @see SensorInformation
     */
    public ArrayList<SensorInformation> getSensorList(){
        return this.sensorList;
    }
     /**
     * adds Sensor information list to the network packet
     * @param packet Information list
     */
    public void addSensorPacket(SensorInformation packet){
        if(this.sensorList ==  null && packet !=null) this.sensorList= new ArrayList<SensorInformation>();
        
        if(packet!=null){
            this.sensorList.add(packet);
        }
        
    }
}
