/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.sensor.util;;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import rtpmt.location.tracker.Location;
import rtpmt.location.tracker.LocationTracker;
import rtpmt.network.packet.NetworkMessage.PackageInformation;
import rtpmt.packages.Package;
import rtpmt.packages.PackageList;

/**
 *
 * @author Saravana kumar
 */
public final class Packet extends Header {

    private final ArrayList<Byte> PayLoad;
    static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private Date date = null;
   
    public Packet() {
        PayLoad = new ArrayList<Byte>();
        date = new Date();
    }

    /**
     * Constructor
     * @param byteArray
     */
    public Packet(byte[] byteArray) {

        PayLoad = new ArrayList<Byte>();
     
        this.ProtocolType = byteArray[0] & 0xff;
        
        int i = 5;
        this.NodeId = (byteArray[1]& 0xff) << 8 | (byteArray[2] & 0xff);
        this.PayloadLength = (byteArray[3] & 0xff << 8) | (byteArray[4] & 0xff);
        
        int payLoadLength = byteArray.length;
        //to handle black box response packet
        if(this.ProtocolType != Constants.P_BLACKBOX_RESPONSE){
            
            this.Service = byteArray[5];
            this.ServiceId = byteArray[6];
            i = 7;
            payLoadLength =  byteArray.length-4;
        }
        
        for (;i < payLoadLength ; i++) {
            this.addDataToPacket(byteArray[i]);
        }
        
        if(this.ProtocolType != Constants.P_BLACKBOX_RESPONSE){
            ByteBuffer bb = ByteBuffer.wrap(byteArray, i, 4);
            long timeStamp = (bb.getInt() * (long) 1000);
            date = new Date(timeStamp);
        }
    }
    
    /**
     * Constructor
     * @param byteArray
     */
    public Packet(byte[] byteArray,boolean isSDCardData) {

        PayLoad = new ArrayList<Byte>();

        this.ProtocolType = byteArray[0];
        
        int i = 0;
        int payLoadLength = byteArray.length;
        //to handle black box response packet
        if(this.ProtocolType != Constants.P_BLACKBOX_RESPONSE){
            this.NodeId = (byteArray[1]& 0xff) << 8 | (byteArray[2] & 0xff);
            this.PayloadLength = (byteArray[3] & 0xff << 8) | (byteArray[4] & 0xff);
            this.Service = byteArray[5];
            this.ServiceId = byteArray[6];
            //to give space for timestamp
            payLoadLength = byteArray.length - 4;
            i = 7;
        }else{
            if(isSDCardData){
                ByteBuffer bb = ByteBuffer.wrap(byteArray, 1, 4);
                this.PayloadLength = bb.getInt();
                i = 5;
            }else{
                this.PayloadLength = (byteArray[1]& 0xff) << 8 | (byteArray[2] & 0xff);
                i = 3;
            }
        }
        
        for (;i < payLoadLength; i++) {
            this.addDataToPacket(byteArray[i]);
        }
       
        if(this.ProtocolType != Constants.P_BLACKBOX_RESPONSE){
            ByteBuffer bb = ByteBuffer.wrap(byteArray, i, 4);
            long timeStamp = (bb.getInt() * (long) 1000);
            date = new Date(timeStamp);
        }
    }
    /**
     * add Data to the packet
     *
     * @param value
     */
    public void addDataToPacket(Byte value) {
        PayLoad.add(value);
    }

    /**
     * get the payload length
     *
     * @return size of payload
     */
    public int getDataLength() {

        return PayLoad.size();

    }

    /**
     * get the payload data based on the index
     *
     * @param index
     * @return byte
     */
    public Byte getData(int index) {

        return PayLoad.get(index);

    }

    /**
     * get the payload data
     *
     * @return byte of data
     */
    public Byte[] getData() {
        return PayLoad.toArray(new Byte[0]);
    }

    /**
     * get the value from the packet received from the sensor will return only
     * if it is update packet otherwise -1
     *
     * @return
     */
    public double getValue() {


        int value = (PayLoad.get(0) & 0xff) << 8 |  (PayLoad.get(1) & 0xff);
        double temperature = (value / 32) - 50;
        temperature = (temperature * 1.8) + 32;

        return temperature;

    }

    /**
     * get the value from the packet received from the sensor will return only
     * if it is update packet otherwise -1
     *
     * @return
     */
    public double getHumidity() {


        int value = (PayLoad.get(1) & 0xff)
                | (PayLoad.get(0) & 0xff) << 8;
        double humdity = (value / 16) - 24;

        return humdity;
    }

    public String getVibration() {

        StringBuilder vibration = new StringBuilder();
        double g;

        for (int i = 0; i < PayLoad.size(); i++) {
            short value = PayLoad.get(i);
            g = (value * 15.6) / 1000;
            vibration.append(String.valueOf(g)).append(" ");
        }
        vibration.deleteCharAt(vibration.length() - 1);

        return vibration.toString();
    }

    public String getShock() {

        StringBuilder shock = new StringBuilder();
        double g;
        
        for (int i=0; i < PayLoad.size(); i++) {
            //Unsigned integer value, note the 0xffff not 0xff
            short value = (short) (PayLoad.get(i) & 0xFF);
            g = (value - 128) / 0.64;
            shock.append(String.valueOf(g)).append(" ");
        }

        shock.deleteCharAt(shock.length() - 1);
        return shock.toString();

    }

    /**
     * get the temperature from the packet received from the motes
     *
     * @return temperature in Fahrenheit
     */
    public String getTimeStamp() {

        return dateFormat.format(date);
    }

    public boolean isHumidty() {
        return this.Service == 1;
    }

    public boolean isTemperature() {
        return this.Service == 0;
    }

    public boolean isVibration() {
        return this.Service == 2;

    }

    public boolean isShock() {
        return this.Service == 3;

    }
    /**
     * 
     * @return 
     */
    public boolean isX() {

        boolean isTrue;

        if (this.Service == 3 || this.Service == 2) {
            isTrue = this.ServiceId == 1;
        } else {
            isTrue = false;
        }
        return isTrue;
    }
    /**
     * 
     * @return 
     */
    public boolean isY() {


        boolean isTrue;

        if (this.Service == 3 || this.Service == 2) {
            isTrue = this.ServiceId == 2;
        } else {
            isTrue = false;
        }
        return isTrue;
    }
    /**
     * 
     * @return 
     */
    public boolean isZ() {

        boolean isTrue;

        if (this.Service == 3 || this.Service == 2) {
            isTrue = this.ServiceId == 3;
        } else {
            isTrue = false;
        }
        return isTrue;
    }
    /**
     * 
     * @return 
     */
    public boolean isPartialPacket() {

        return this.isVibration() || this.isShock();
    }
    /**
     * 
     * @param byteValue
     * @param N
     * @return 
     */
    private boolean isBitSet(Byte byteValue, int N){

        return (byteValue & (1<<N)) == 1;

    }
    private int getPacketNumber(){
        Byte value;
        value = PayLoad.get(0);
        return (value & (1<<4));
    }
    /**
     * 
     * @param partialpacket
     * @return 
     */
    public boolean isCompletePacket(Packet partialpacket) {

        Byte value;
        value = PayLoad.get(0);
        if(this.Service == partialpacket.Service  && this.ServiceId == partialpacket.ServiceId &&
                this.date.equals(partialpacket.date)){

            return !(this.isBitSet(value, 4) && partialpacket.isBitSet(value,4) );
        }

        return false;
    }

    public void combinePacket(Packet partialpacket) {
       //remove the first information byte
        partialpacket.PayLoad.remove(0);
        this.PayLoad.remove(0);
        this.PayLoad.addAll( partialpacket.PayLoad);
    }
    
    
    public String uniqueId(){
        return  (this.getTimeStamp()) + this.NodeId + this.Service + this.ServiceId;
    }
    
    /**
     * This one works only for the get board ID request, Don't use it for other requests
     * @return 
     */
    public long sensorId(){
       
       if(this.ProtocolType == Constants.P_BLACKBOX_RESPONSE){
             return ((((long)this.PayLoad.get(0) & 0xff) << 56) |
                (((long)this.PayLoad.get(1) & 0xff) << 48) |
                (((long)this.PayLoad.get(2) & 0xff) << 40) |
                (((long)this.PayLoad.get(3) & 0xff) << 32) |
                (((long)this.PayLoad.get(4) & 0xff) << 24) |
                (((long)this.PayLoad.get(5) & 0xff) << 16) |
                (((long)this.PayLoad.get(6) & 0xff) <<  8) |
                (((long)this.PayLoad.get(7) & 0xff)));
        }else{
           return 0;
       }
    }
    /**
     *
     * @return
     */
    @Override
    public int hashCode(){
        return this.NodeId + this.Service + this.ServiceId + this.getTimeStamp().hashCode();
        
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Packet other = (Packet) obj;
        if (this.PayLoad != other.PayLoad && (this.PayLoad == null || !this.PayLoad.equals(other.PayLoad))) {
            return false;
        }
        return this.NodeId == other.NodeId || this.date == other.date || (this.date != null && this.date.equals(other.date));
    }
    
    /**
     *
     * @param isRealTime
     * @return
     */
    public PackageInformation getNetworkMessage(boolean isRealTime) {
            Package pack = PackageList.getPackage(this.NodeId);
            if(pack==null)pack = PackageList.getPackage(0);
            PackageInformation.Builder message = PackageInformation.newBuilder();
            //compulsary information
            message.setIsRealTime(isRealTime);
            message.setMessageType(PackageInformation.MessageType.CONFIG);
            message.setTimeStamp(this.date.getTime());
            
            if(pack!=null){
                if(pack.getSensorId()!= 0 ) message.setSensorId(pack.getSensorId());
                if(pack.getPackageId() != null ) message.setPackageId(pack.getPackageId());
                if(pack.getTruckId() != null )  message.setTruckId(pack.getTruckId()) ;
                if(pack.getComments() != null ) message.setComments(pack.getComments());
            }
           
            
            PackageInformation.Sensor.Builder sensor = PackageInformation.Sensor.newBuilder();

            if (this.isTemperature()) {
                sensor.setSensorId("1");
                sensor.setSensorUnit("F");
                sensor.setSensorType(PackageInformation.SensorType.TEMPERATURE);
                sensor.setSensorValue(String.valueOf(this.getValue()));
                //sensor.setSensorValue("80");
                message.addSensors(sensor);
            } else if (this.isHumidty()) {
                sensor = PackageInformation.Sensor.newBuilder();
                sensor.setSensorId("2");
                sensor.setSensorUnit("%RH");
                sensor.setSensorType(PackageInformation.SensorType.HUMIDITY);
                System.out.println(this.getHumidity());
                sensor.setSensorValue(String.valueOf((this.getHumidity())));
                message.addSensors(sensor);
            } else if (this.isVibration()) {
                sensor = PackageInformation.Sensor.newBuilder();
                sensor.setSensorId("3");
                sensor.setSensorUnit("g");
                if (this.isX()) {
                    System.out.println("X=");
                    sensor.setSensorType(PackageInformation.SensorType.VIBRATIONX);
                } else if (this.isY()) {
                    System.out.println("Y=");
                    sensor.setSensorType(PackageInformation.SensorType.VIBRATIONY);
                } else if (this.isZ()) {
                    System.out.println("Z=");
                    sensor.setSensorType(PackageInformation.SensorType.VIBRATIONZ);
                }
                System.out.print(this.getVibration());
                sensor.setSensorValue(String.valueOf((this.getVibration())));
                message.addSensors(sensor);
            } else if (this.isShock()) {
                sensor = PackageInformation.Sensor.newBuilder();
                sensor.setSensorId("4");
                sensor.setSensorUnit("g");
                if (this.isX()) {
                    sensor.setSensorType(PackageInformation.SensorType.SHOCKX);
                } else if (this.isY()) {
                    sensor.setSensorType(PackageInformation.SensorType.SHOCKY);
                } else if (this.isZ()) {
                    sensor.setSensorType(PackageInformation.SensorType.SHOCKZ);
                }
                System.out.println(this.getShock());
                sensor.setSensorValue(String.valueOf((this.getShock())));
                message.addSensors(sensor);
            }

            PackageInformation.LocationInformation.Builder location = PackageInformation.LocationInformation.newBuilder();
            Location loc = LocationTracker.getLocation();
            if (loc != null) {
                location.setLatitude(loc.getLatitude());
                location.setLongitude(loc.getLongitude());
                message.setLocation(location);
            } else {
                location.setLatitude(43.084603);
                location.setLongitude(-77.680312);
                message.setLocation(location);
            }
            return message.build();
    }

}
