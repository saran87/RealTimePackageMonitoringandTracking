/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sensorconfiguration.swing.ui;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rtpmt.motes.packet.Packetizer;
import rtpmt.network.packet.SensorMessage.SensorInformation;

/**
 *
 * @author Kumar
 */
public class UIEventHanler extends ValidateUI implements Runnable {

    private MainUI UIObject;
    //packet reader to read the data from the motes
    Packetizer packetReader;
    //background thread for sending data to the server.
    private Thread bgCommunicator;
    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;
    /*
     * Connected sensor indicator
     */
    boolean isSensorConnected;
    SerialPort serialPort;

    public UIEventHanler(MainUI object) {

        UIObject = object;
        bgCommunicator = new Thread(this);
    }

    /**
     * Initialize serial port with connected sensor port
     */
    public void initSerialPort() {

        searchForPorts();
        if (isSensorConnected) {
            initIOStream();
            initPacketReader();

            if (!bgCommunicator.isAlive()) {
                bgCommunicator.start();
            }
        }

    }

    //search for all the serial ports
    //pre: none
    //post: adds all the found ports to a combo box on the GUI
    protected void searchForPorts() {
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();

        Pattern pattern = Pattern.compile("\\d+");
        isSensorConnected = false;
        while (ports.hasMoreElements()) {
            CommPortIdentifier curPort = (CommPortIdentifier) ports.nextElement();

            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                try {
                    CommPort commPort = curPort.open(null, 0);
                    Matcher tokenMatcher = pattern.matcher(commPort.getName());
                    if (tokenMatcher.find()) {
                        serialPort = (SerialPort) commPort;
                        //setting serial port parameters 
                        //this setting is based on telosb mote specification
                        //baud rate is important here i.e(9600)
                        //for controlling GUI elements
                        serialPort.setSerialPortParams(230400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                        isSensorConnected = true;
                    }


                } catch (Exception ex) {
                    // System.err.println(ex);
                }
            }
        }
        UIObject.setConnected(isSensorConnected);
    }
    //open the input and output streams
    //pre: an open port
    //post: initialized intput and output streams for use to communicate data

    private boolean initIOStream() {
        //return value for whather opening the streams is successful or not
        boolean successful = false;

        try {
            //initialize the input and output stream
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();

            //writeData(0, 0);            
            successful = true;
            return successful;
        } catch (IOException e) {
            String logText = "I/O Streams failed to open. (" + e.toString() + ")";
            UIObject.txtLog.setForeground(Color.red);
            UIObject.txtLog.append(logText + "\n");
            return successful;
        }
    }

    /**
     * Initialize the packet reader from the motes pre: initialize the input
     * stream and output stream post: packetReader is initialized and ready to
     * read
     */
    private boolean initPacketReader() {

        //return value for whather opening the streams is successful or not
        boolean successful = false;

        try {
            packetReader = new Packetizer("Packet Reader", input, output);
            packetReader.open(null);
            successful = true;
        } catch (IOException ex) {
            String logText = "I/O Streams failed to open. (" + ex.toString() + ")";
            UIObject.txtLog.setForeground(Color.red);
            UIObject.txtLog.append(logText + "\n");
            successful = false;
        }

        return successful;
    }

    //thread to get the mote packet from the queue and send it to the server
    @Override
    public void run() {
        try {
            for (;;) {

                SensorInformation sensorInfo = packetReader.readPacket();

                for (SensorInformation.Sensor sensor : sensorInfo.getSensorsList()) {

                    String message = sensor.getSensorType().name() + " : " + sensor.getSensorValue() + " " + sensor.getSensorUnit() + "   " + sensorInfo.getTimeStamp();
                    UIObject.txtLog.append(message + "\n");
                }


                HashMap<Integer, Long> sensorList = packetReader.getSensorList();

                for (Map.Entry<Integer, Long> entry : sensorList.entrySet()) {
                    Integer integer = entry.getKey();
                    Long long1 = entry.getValue();

                    UIObject.txtLog.append("Sensor " + long1  + " is using ShortId:" + integer + "\n");
                }

            }
        } catch (IOException ex) {
            String logText = "Too many listeners. (" + ex.toString() + ")";
            UIObject.txtLog.setForeground(Color.red);
            UIObject.txtLog.append(logText + "\n");
        }
    }

    void configureSensor(ArrayList<Integer> timeInterval, ArrayList<Double> threshold) {

        try {
            packetReader.configure(timeInterval, threshold);
        } catch (IOException ex) {
            String logText = "Too many listeners. (" + ex.toString() + ")";
            UIObject.txtLog.setForeground(Color.red);
            UIObject.txtLog.append(logText + "\n");
        }


    }
}
