/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sensorconfiguration.swing.ui;

import filewriter.FileWriter;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import rtpmt.motes.packet.Packetizer;
import rtpmt.network.packet.SensorMessage.SensorInformation;

/**
 *
 * @author Kumar
 */
public class UIEventHanler extends ValidateUI implements Runnable {

    private final MainUI UIObject;
    //packet reader to read the data from the motes
    Packetizer packetReader;
    //background thread for sending data to the server.
    private final Thread bgCommunicator;
    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;
    private final FileOutputStream datalog;
    private final String DATA_LOG_FILE = "datalog.buff";
    /*
     * Connected sensor indicator
     */
    boolean isSensorConnected;
    SerialPort serialPort;

    public UIEventHanler(MainUI object) throws FileNotFoundException {

        UIObject = object;
        bgCommunicator = new Thread(this);
        datalog = new FileOutputStream(DATA_LOG_FILE);
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

        ArrayList<CommPortIdentifier> serialPortList = new ArrayList<CommPortIdentifier>();
        while (ports.hasMoreElements()) {
            CommPortIdentifier curPort = (CommPortIdentifier) ports.nextElement();
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                System.out.println(curPort.getName());
                serialPortList.add(curPort);
            }
        }
        try {

            if (serialPortList.size() > 0) {
                CommPort commPort = serialPortList.get(serialPortList.size() - 1).open(null, 0);
                serialPort = (SerialPort) commPort;
                    //setting serial port parameters 
                //this setting is based on telosb mote specification
                //baud rate is important here i.e(9600)
                //for controlling GUI elements
                serialPort.setSerialPortParams(230400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                isSensorConnected = true;
            }

        } catch (PortInUseException ex) {
            // System.err.println(ex);
        } catch (UnsupportedCommOperationException ex) {
            // System.err.println(ex);
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
            rtpmt.sensor.reader.SerialPort port;
            port = new rtpmt.sensor.reader.SerialPort(input, output);
            packetReader = new Packetizer("Packet Reader", port);
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
                String message = sensorInfo.getDeviceId() + "," + sensorInfo.getTimeStamp() + ",";
                UIObject.txtLog.append(message);
                for (SensorInformation.Sensor sensor : sensorInfo.getSensorsList()) {

                    message = sensor.getSensorType().name() + " : " + sensor.getSensorValue() + " " + sensor.getSensorUnit();
                    UIObject.txtLog.append(message + "\n");
                }
                sensorInfo.writeTo(datalog);
                /*
                 HashMap<Integer, Long> sensorList = packetReader.getSensorList();

                 for (Map.Entry<Integer, Long> entry : sensorList.entrySet()) {
                 Integer integer = entry.getKey();
                 Long long1 = entry.getValue();

                 UIObject.txtLog.append("Sensor " + long1  + " is using ShortId:" + integer + "\n");
                 }
                 */
            }
        } catch (IOException ex) {
            String logText = "Too many listeners. (" + ex.toString() + ")";
            UIObject.txtLog.setForeground(Color.red);
            UIObject.txtLog.append(logText + "\n");
        }
    }

    void configureSensor(ArrayList<Integer> timeInterval, ArrayList<Integer> threshold) {

        try {
            packetReader.configure(timeInterval, threshold);
        } catch (IOException ex) {
            String logText = "Too many listeners. (" + ex.toString() + ")";
            UIObject.txtLog.setForeground(Color.red);
            UIObject.txtLog.append(logText + "\n");
        }

    }

    void generateCSV(File file) throws FileNotFoundException, Exception {
        try {     
            FileWriter fw = new  FileWriter();
            FileInputStream inputStream = new FileInputStream(DATA_LOG_FILE);
            fw.writeCSV(inputStream, file);
        } catch (Exception ex) {
            Logger.getLogger(UIEventHanler.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
}
