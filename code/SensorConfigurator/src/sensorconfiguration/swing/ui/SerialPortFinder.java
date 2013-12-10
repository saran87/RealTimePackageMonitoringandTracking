/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sensorconfiguration.swing.ui;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import rtpmt.sensor.reader.SensorReader;

/**
 *
 * @author Kumar
 */
public class SerialPortFinder {
    private static int TIMEOUT = 100;
    private static rtpmt.sensor.reader.SerialPort port;
    
    /**
     * @return    
     * A HashSet containing the CommPortIdentifier for all serial 
     * ports that are not currently being used.
     */
    private static HashSet<CommPortIdentifier> getAvailableSerialPorts() {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        while (thePorts.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
            switch (com.getPortType()) {
            case CommPortIdentifier.PORT_SERIAL:
                try {
                    CommPort thePort = com.open("CommUtil", 50);
                    thePort.close();
                    h.add(com);
                } catch (PortInUseException e) {
                    System.out.println("Port, "  + com.getName() + ", is in use while finding.");
                } catch (Exception e) {
                    System.err.println("Failed to open port " +  com.getName());
                    e.printStackTrace();
                }
            }
        }
        return h;
    }
    
    
    public static CommPortIdentifier findSensorPort(){
        
        HashSet<CommPortIdentifier> comPortList = getAvailableSerialPorts();
       
        for (CommPortIdentifier commPortIdentifier : comPortList) {
            try {
                
                System.out.println("Port, "  + commPortIdentifier.getName() + ", is in use.");
                //the method below returns an object of type CommPort
                CommPort commPort = commPortIdentifier.open("RFID", TIMEOUT);
                
                //the CommPort object can be casted to a SerialPort object
                SerialPort serialPort = (SerialPort)commPort;
                //setting serial port parameters
                //this setting is based on telosb mote specification
                //baud rate is important here i.e(9600)
                //for controlling GUI elements
                serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                //initialize the input and output stream
                InputStream input = serialPort.getInputStream();
                OutputStream output = serialPort.getOutputStream();
                
                port = new rtpmt.sensor.reader.SerialPort(input, output);
                boolean isRealTime = false;
                SensorReader packetReader = new SensorReader(port, isRealTime);
                packetReader.open();
                //packetReader.addSensorEventHandler()    
                packetReader.close();
                commPort.close();
                return commPortIdentifier;
            } catch (PortInUseException ex) {
                Logger.getLogger(SerialPortFinder.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedCommOperationException ex) {
                Logger.getLogger(SerialPortFinder.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SerialPortFinder.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(SerialPortFinder.class.getName()).log(Level.SEVERE, null, ex);
            }catch(Exception ex){
                Logger.getLogger(SerialPortFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        return null;
        
    }

    
    
}
