/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.desktop.client;


import gnu.io.*;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;
import rtpmt.motes.packet.Packetizer;
import rtpmt.network.packet.SensorMessage.SensorInformation;
import rtpmt.network.tcp.TCPClient;

public class Communicator implements SerialPortEventListener, Runnable
{
    //passed from main GUI
    RealTimePackageTracking window = null;
    //Tcp client object to send data over network
    TCPClient tCPClient;
    //for containing the ports that will be found
    private Enumeration ports = null;
    //map the port names to CommPortIdentifiers
    private HashMap portMap = new HashMap();

    //this is the object that contains the opened port
    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;

    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;

    //just a boolean flag that i use for enabling
    //and disabling buttons depending on whether the program
    //is connected to a serial port or not
    private boolean bConnected = false;

    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    //some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;

    //a string for recording what goes on in the program
    //this string is written to the GUI
    String logText = "";

    //packet reader to read the data from the motes
    Packetizer packetReader;
    
    //background thread for sending data to the server.
    private Thread bgCommunicator;
    
    //to indicate whether to send data to server or not
    private boolean ServerAvailable;
    
    public Communicator(RealTimePackageTracking window)
    {
        this.window = window;
        bgCommunicator = new Thread(this);
        ServerAvailable = false;
    }
    

    //search for all the serial ports
    //pre: none
    //post: adds all the found ports to a combo box on the GUI
    public void searchForPorts()
    {
        ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements())
        {
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                window.jcmbComPort.addItem(curPort.getName());
                portMap.put(curPort.getName(), curPort);
            }
        }
    }

    //connect to the selected port in the combo box
    //pre: ports are already found by using the searchForPorts method
    //post: the connected comm port is stored in commPort, otherwise,
    //an exception is generated
    public void connect()
    {
        String selectedPort = (String)window.jcmbComPort.getSelectedItem();
        selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);

        CommPort commPort = null;

        try
        {
            //the method below returns an object of type CommPort
            commPort = selectedPortIdentifier.open("RFID", TIMEOUT);
            //the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort)commPort;
            //setting serial port parameters 
            //this setting is based on telosb mote specification
            //baud rate is important here i.e(9600)
            serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
            //for controlling GUI elements
            setConnected(true);

            //logging
            logText = selectedPort + " opened successfully.";
            window.txtLog.setForeground(Color.black);
            window.txtLog.append(logText + "\n");
               
            
            //CODE ON SETTING BAUD RATE ETC OMITTED
            //XBEE PAIR ASSUMED TO HAVE SAME SETTINGS ALREADY
        }
        catch (PortInUseException e)
        {
            logText = selectedPort + " is in use. (" + e.toString() + ")";
            
            window.txtLog.setForeground(Color.RED);
            window.txtLog.append(logText + "\n");
        }
        catch (Exception e)
        {
            logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";
            window.txtLog.append(logText + "\n");
            window.txtLog.setForeground(Color.RED);
        }
    }

    //open the input and output streams
    //pre: an open port
    //post: initialized intput and output streams for use to communicate data
    public boolean initIOStream()
    {
        //return value for whather opening the streams is successful or not
        boolean successful = false;

        try {
            //initialize the input and output stream
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
           
            //writeData(0, 0);            
            successful = true;
            return successful;
        }
        catch (IOException e) {
            logText = "I/O Streams failed to open. (" + e.toString() + ")";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
            return successful;
        }
    }
    /**
    *  Initialize the packet reader from the motes
    *  pre: initialize the input stream and output stream
    *  post: packetReader is initialized and ready to read
    */
    public boolean initPacketReader(){
        
        //return value for whather opening the streams is successful or not
        boolean successful = false;
        
        try {
            packetReader = new Packetizer("Packet Reader", input,output);
            packetReader.open(null); 
            successful = true;
        }
        catch(IOException ex){
            logText = "I/O Streams failed to open. (" + ex.toString() + ")";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
            successful = false;
        }
        
        return successful;
    }
    //starts the event listener that knows whenever data is available to be read
    //pre: an open serial port
    //post: an event listener for the serial port that knows when data is recieved
    public void initListener()
    {
        try
        {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            
            if (!bgCommunicator.isAlive()) {
                 bgCommunicator.start();
            }
            
        }
        catch (TooManyListenersException e)
        {
            logText = "Too many listeners. (" + e.toString() + ")";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
        }
    }

    //disconnect the serial port
    //pre: an open serial port
    //post: clsoed serial port
    public void disconnect()
    {
        //close the serial port
        try
        {
            writeData(0, 0);

            serialPort.removeEventListener();
            serialPort.close();
            input.close();
            output.close();
            //diconnect tcp connection
            tCPClient.disconnect();
            setConnected(false);

            logText = "Disconnected.";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
        }
        catch (Exception e)
        {
            logText = "Failed to close " + serialPort.getName() + "(" + e.toString() + ")";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
        }
    }

    final public boolean getConnected()
    {
        return bConnected;
    }

    public void setConnected(boolean bConnected)
    {
        this.bConnected = bConnected;
        
        if(bConnected)
        {
            window.jblConnectedStatus.setText("Connected");
            window.jblConnectedStatus.setForeground(Color.GREEN);
        }
        else
        {
            window.jblConnectedStatus.setText("Not Connected");          
            window.jblConnectedStatus.setForeground(Color.RED);           
        }
    }

    //what happens when data is received
    //pre: serial event is triggered
    //post: processing on the data it reads
    @Override
    public void serialEvent(SerialPortEvent evt) {
        
        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {
            try
            {
                String dummydata = "#:367:N:43.1049:W:77.6233:ZBBBB00000849";
                byte[] data = new byte[input.available()];
                       
                //input.read(data);
                //System.out.println(data);
                //logText =  packetReader.dumpPacket();
                
                //logText = packetReader.getTemperature();
               // String data = dummydata + temperature;
                
               // tCPClient.sendData(data);
                
                window.txtLog.append("Data:" + logText);
                logText = "";
                window.txtLog.append("\n");
            }
            catch (Exception e)
            {
                logText = "Failed to read data. (" + e.toString() + ")";
                window.txtLog.setForeground(Color.red);
                window.txtLog.append(logText + "\n");
            }
        }
    }

    //method that can be called to send data
    //pre: open serial port
    //post: data sent to the other device
    public void writeData(int leftThrottle, int rightThrottle)
    {
        try
        {
            output.write(leftThrottle);
            output.flush();
            //this is a delimiter for the data
            output.write(DASH_ASCII);
            output.flush();
            
            output.write(rightThrottle);
            output.flush();
            //will be read as a byte so it is a space key
            output.write(SPACE_ASCII);
            output.flush();
        }
        catch (Exception e)
        {
            logText = "Failed to write data. (" + e.toString() + ")";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
        }
    }

    //
   public void initalizeTCPClient(String ipAddress,int portNumber) {
       try{
            tCPClient = new TCPClient();
            tCPClient.connect(ipAddress, portNumber);
            ServerAvailable =true;
       }catch(Exception ex){
           window.showModalMessage(ex.getMessage());
       }   
    }
   
   //thread to get the mote packet from the queue and send it to the server
    @Override
    public void run() {
       try {
        for (;;) {
                   //byte[] packet = packetReader.readRawPacket();
                   
                SensorInformation sensorInfo = packetReader.readPacket();
                for (SensorInformation.Sensor sensor : sensorInfo.getSensorsList()) {
                    String message = sensor.getSensorType().name() +" : " + sensor.getSensorValue() + " " + sensor.getSensorUnit() + "   " + sensorInfo.getTimeStamp();
                    if(sensor.getSensorType().name().equalsIgnoreCase( "TEMPERATURE"))
                        window.txtLog.append(message + "\n");
                }
               
                System.out.println();
                System.out.flush();
                
                if(ServerAvailable){
                    tCPClient.sendData(sensorInfo);
                }
	  }
       }
       catch(IOException ex){
            logText = "Too many listeners. (" + ex.toString() + ")";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
       }
    }

}

