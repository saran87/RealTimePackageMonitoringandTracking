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

    private ArrayList<Byte> PayLoad;
    static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private Date date = null;

    public Packet() {
        PayLoad = new ArrayList<Byte>();
        date = new Date();
    }

    /**
     * Constructor 
     */
    public Packet(byte[] byteArray) {

        PayLoad = new ArrayList<Byte>(byteArray.length);

        this.ProtocolType = byteArray[0];
        this.NodeId = (byteArray[1]& 0xff) << 8 | (byteArray[2] & 0xff);
        this.PayloadLength = (byteArray[3] & 0xff << 8) | (byteArray[4] & 0xff);
        this.Service = byteArray[5];
        this.ServiceId = byteArray[6];

        int i = 0;
        for (i = 7; i < byteArray.length - 4; i++) {
            this.addDataToPacket(byteArray[i]);
        }

        ByteBuffer bb = ByteBuffer.wrap(byteArray, i, 4);
        long timeStamp = (bb.getInt() * (long) 1000);
        date = new Date(timeStamp);

    }

    /**
     * add Data to the packet
     *
     * @return void
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
        int i = 0;
        
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

        if (this.Service == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isTemperature() {

        if (this.Service == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isVibration() {

        if (this.Service == 2) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isShock() {

        if (this.Service == 3) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isX() {

        boolean isTrue;

        if (this.Service == 3 || this.Service == 2) {
            if (this.ServiceId == 1) {
                isTrue = true;
            } else {
                isTrue = false;
            }
        } else {
            isTrue = false;
        }

        return isTrue;
    }

    public boolean isY() {


        boolean isTrue;

        if (this.Service == 3 || this.Service == 2) {
            if (this.ServiceId == 2) {
                isTrue = true;
            } else {
                isTrue = false;
            }
        } else {
            isTrue = false;
        }

        return isTrue;
    }

    public boolean isZ() {

        boolean isTrue;

        if (this.Service == 3 || this.Service == 2) {

            if (this.ServiceId == 3) {
                isTrue = true;
            } else {
                isTrue = false;
            }
        } else {
            isTrue = false;
        }

        return isTrue;
    }
}
