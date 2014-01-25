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
import rtpmt.location.tracker.Location;
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
            //start from 7th position
            i = 7;
            //now payloadlength is -4 used for timestamp
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
     * @param isSDCardData
     */
    public Packet(byte[] byteArray,boolean isSDCardData) {

        PayLoad = new ArrayList<Byte>();

        this.ProtocolType = byteArray[0] & 0xff;
        
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

        return truncate(temperature,4);

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

        return truncate(humdity,4);
    }

    public String getVibration() {

        StringBuilder vibration = new StringBuilder();
        double g;

        for (int i = 1; i < PayLoad.size(); i++) {
            short value = PayLoad.get(i);
            g = (value * 15.6) / 1000;
            vibration.append(String.valueOf(truncate(g,4))).append(" ");
        }
        vibration.deleteCharAt(vibration.length() - 1);

        return vibration.toString();
    }

    public String getShock() {

        StringBuilder shock = new StringBuilder();
        double g;
        
        for (int i=1; i < PayLoad.size(); i++) {
            //Unsigned integer value, note the 0xffff not 0xff
            short value = (short) (PayLoad.get(i) & 0xFF);
            g = (value - 128) / 0.64;
            shock.append(String.valueOf(truncate(g,4))).append(" ");
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
    /**
     * 
     * @return boolean 
     */
    public boolean isAboveThreshold(){
        return this.ProtocolType == Constants.P_UPDATE_THRESHOLD;
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
    
    public boolean isInstanteaous(){
        return this.isBitSet(PayLoad.get(0),2);
    }
    
    public boolean isFreeFall(){
        return this.isBitSet(PayLoad.get(0),3);
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

        return ((byteValue >> N) & 1) == 1;

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

        if(this.Service == partialpacket.Service  && this.ServiceId == partialpacket.ServiceId &&
                this.date.equals(partialpacket.date)){
            return !(this.isBitSet(PayLoad.get(0), 5) && this.isBitSet(partialpacket.PayLoad.get(0),5));
        }

        return false;
    }

    public void combinePacket(Packet partialpacket) {
       //remove the first information byte
        partialpacket.PayLoad.remove(0);
        //this.PayLoad.remove(0); we need it to check for instanteaousshock
        this.PayLoad.addAll( partialpacket.PayLoad);
    }
    
    
    public String uniqueId(){
        return  (this.getTimeStamp()) + this.NodeId + this.Service + this.ServiceId;
    }
    
    /**
     * This one works only for the get board ID request, Don't use it for other requests
     * @return 
     */
    public String sensorId(){
       StringBuilder sensorId =  new StringBuilder();
       
       if(this.ProtocolType == Constants.P_BLACKBOX_RESPONSE){
           for (int i = 0; i < 8; i++) {
               sensorId.append(Integer.toHexString(this.PayLoad.get(i) & 0xff).toUpperCase());
           }
           return sensorId.toString();
           /*
             return ((((long)this.PayLoad.get(0) & 0xff) << 56) |
                (((long)this.PayLoad.get(1) & 0xff) << 48) |
                (((long)this.PayLoad.get(2) & 0xff) << 40) |
                (((long)this.PayLoad.get(3) & 0xff) << 32) |
                (((long)this.PayLoad.get(4) & 0xff) << 24) |
                (((long)this.PayLoad.get(5) & 0xff) << 16) |
                (((long)this.PayLoad.get(6) & 0xff) <<  8) |
                (((long)this.PayLoad.get(7) & 0xff)));
           */
        }else{
           return "00";
       }
    }
    
    /**
     * 
     * @return 
     */
     public int getBatteryLevel(){
         
         int batteryLevel = 0;
         if(this.PayLoad != null && this.PayLoad.size() > 1){
             batteryLevel =   (PayLoad.get(0) & 0xff)
                | (PayLoad.get(1) & 0xff) << 8;
         }
         return batteryLevel;
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
    private PackageInformation.Builder getNetworkMessage(boolean isRealTime) {
            Package pack = PackageList.getPackage(this.NodeId);
            if(pack==null){
                pack = PackageList.getPackage(0);
             }
            PackageInformation.Builder message = PackageInformation.newBuilder();
            //compulsary information
            message.setIsRealTime(isRealTime);
            message.setMessageType(PackageInformation.MessageType.SENSOR_INFO);
            message.setTimeStamp(this.date.getTime());

            if(pack!=null){
                if(pack.getSensorId()!= null &&  !pack.getSensorId().isEmpty()){
                    message.setSensorId(pack.getSensorId());
                }else{
                    message.setSensorId(Constants.NO_ID);
                }
                if(pack.getPackageId() != null ){
                    message.setPackageId(pack.getPackageId());
                }else{
                    message.setPackageId(Constants.NO_ID);
                }
                if(pack.getTruckId() != null ){  
                    message.setTruckId(pack.getTruckId()) ;
                }else{
                    message.setTruckId(Constants.NO_ID);
                }
                /*
                * no need for comments in the sensorInformation
                if(pack.getComments() != null ){ 
                   message.setComments(pack.getComments());
                }else{
                    message.setComments(Constants.NO_ID);
                }*/
            }
            //is sensor value above threshold
            message.setIsAboveThreshold(this.isAboveThreshold());
            PackageInformation.Sensor.Builder sensor = PackageInformation.Sensor.newBuilder();
            if (this.isTemperature()) {
                sensor.setSensorUnit("F");
                sensor.setSensorType(PackageInformation.SensorType.TEMPERATURE);
                sensor.setSensorValue(String.valueOf(this.getValue()));
                message.addSensors(sensor);
            } else if (this.isHumidty()) {
                sensor = PackageInformation.Sensor.newBuilder();
                sensor.setSensorUnit("%RH");
                sensor.setSensorType(PackageInformation.SensorType.HUMIDITY);
                sensor.setSensorValue(String.valueOf((this.getHumidity())));
                message.addSensors(sensor);
            } else if (this.isVibration()) {
                sensor = PackageInformation.Sensor.newBuilder();
                sensor.setSensorUnit("g");
                if (this.isX()) {
                    sensor.setSensorType(PackageInformation.SensorType.VIBRATIONX);
                } else if (this.isY()) {
                    sensor.setSensorType(PackageInformation.SensorType.VIBRATIONY);
                } else if (this.isZ()) {
                    sensor.setSensorType(PackageInformation.SensorType.VIBRATIONZ);
                }
                sensor.setSensorValue(String.valueOf((this.getVibration())));
                message.addSensors(sensor);
            } else if (this.isShock()) {
                sensor = PackageInformation.Sensor.newBuilder();
                sensor.setSensorUnit("g");
                if (this.isX()) {
                    sensor.setSensorType(PackageInformation.SensorType.SHOCKX);
                } else if (this.isY()) {
                    sensor.setSensorType(PackageInformation.SensorType.SHOCKY);
                } else if (this.isZ()) {
                    sensor.setSensorType(PackageInformation.SensorType.SHOCKZ);
                }
             
                message.setIsInstantaneous(this.isInstanteaous());
                sensor.setSensorValue(String.valueOf((this.getShock())));
                message.addSensors(sensor);
            }

            
            return message;
    }
    
    /**
     * 
     * @param location
     * @return 
     */
    public PackageInformation getRealTimeMessage(Location location){
       boolean isRealTime = true;
       PackageInformation.Builder message= getNetworkMessage(isRealTime);
       PackageInformation.LocationInformation.Builder loc;
       loc = PackageInformation.LocationInformation.newBuilder();
        if (location != null) {
            loc.setLatitude(location.getLatitude());
            loc.setLongitude(location.getLongitude());
            message.setLocation(loc);
        }
        return message.build();
    }
    
    /**
     * 
     * @return 
     */
    public PackageInformation getBlackBoxMessage(){
        boolean isRealTime = false;
        PackageInformation.Builder message= getNetworkMessage(isRealTime);
        return message.build();
    }
    
    public double truncate(double number, int precision)
    {
        double prec = Math.pow(10, precision);
        int integerPart = (int) number;
        double fractionalPart = number - integerPart;
        fractionalPart *= prec;
        int fractPart = (int) fractionalPart;
        fractionalPart = (double) (integerPart) + (double) (fractPart)/prec;
        return fractionalPart;
    }

}
