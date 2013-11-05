/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.sensor.util;

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import rtpmt.motes.packet.Header;

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

        this.ProtocolType = byteArray[0];
        this.NodeId = (byteArray[1]& 0xff) << 8 | (byteArray[2] & 0xff);
        this.PayloadLength = (byteArray[3] & 0xff << 8) | (byteArray[4] & 0xff);
        this.Service = byteArray[5];
        this.ServiceId = byteArray[6];

        int i = 7;
        for (;i < byteArray.length - 4; i++) {
            this.addDataToPacket(byteArray[i]);
        }

        ByteBuffer bb = ByteBuffer.wrap(byteArray, i, 4);
        long timeStamp = (bb.getInt() * (long) 1000);
        date = new Date(timeStamp);

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

        return (Byte[]) PayLoad.toArray(new Byte[0]);

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
            vibration.append(String.valueOf(g)).append("|");
        }
        vibration.deleteCharAt(vibration.length() - 1);

        return vibration.toString();
    }

    public String getShock() {

        StringBuilder shock = new StringBuilder();
        double g;
        int i;

        for (i = 2; i < 72; i++) {
            short value = PayLoad.get(i);
            g = (value * 15.6) / 1000;
            shock.append(String.valueOf(g)).append("|");
        }
        for (; i < PayLoad.size(); i++) {
            //Unsigned integer value, note the 0xffff not 0xff
            short value = (short) (PayLoad.get(i) & 0xFF);
            g = (value - 128) / 0.64;
            shock.append(String.valueOf(g)).append("|");
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
       
        if(this.getPacketNumber() == 0){
            //remove the first information byte
            partialpacket.PayLoad.remove(0);
            this.PayLoad.addAll( partialpacket.PayLoad);
        }else{
            //remove the first information byte
             this.PayLoad.remove(0);
             this.PayLoad.addAll(0,partialpacket.PayLoad);
        }
    }

}
