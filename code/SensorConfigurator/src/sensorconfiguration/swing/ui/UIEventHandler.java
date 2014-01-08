/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sensorconfiguration.swing.ui;

import rtpmt.file.FileWriter;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import rtpmt.file.FileReader;
import rtpmt.network.packet.NetworkMessage.PackageInformation;
import rtpmt.packages.Package;
import rtpmt.packages.PackageList;
import rtpmt.packages.SensorEventHandler;
import rtpmt.sensor.reader.SensorReader;
import rtpmt.sensor.util.Constants;
import rtpmt.sensor.util.Packet;
import rtpmt.tcpclient.SensorClient;

/**
 *
 * @author Kumar
 */
public class UIEventHandler extends ValidateUI implements Runnable, SensorEventHandler {

    private final SensorConfigurator UIObject;
    //packet reader to read the data from the motes
    SensorReader packetReader;
    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;
    //map the port names to CommPortIdentifiers
    private final HashMap portMap = new HashMap();
    /*
     * Connected sensor indicator
     */
    private boolean isSensorConnected = false;
    SerialPort serialPort;
    rtpmt.sensor.reader.SerialPort port;
    private final int TIMEOUT = 1000;
    public final String FOLDER = "data/";
    public final String CONFIG_FOLDER = "configdata/";
    private FileOutputStream datalog;
    public String DATA_LOG_FILE = "datalog.buff";
    private SensorClient sensorClient;
    
    private ProgressBar bar;
    private final static String address = "localhost";
    private boolean pushToServer = false;
    
    public UIEventHandler(SensorConfigurator object) throws FileNotFoundException {
        
        UIObject = object;
        File f = new File(FOLDER);
        File cf = new File(CONFIG_FOLDER);
        try{
            if(f.mkdir() && cf.mkdir()) { 
                System.out.println("Directory Created");
            } else {
                System.out.println("Directory is not created");
            }
        } catch(Exception e){
           UIObject.handleError("Not able to create neccessary file. Run as administrator");
        } 
        //connect to server
        initServer(false);
    }

    public void initServer(boolean ui) {
        try {
            sensorClient = new SensorClient(address, 8080);
            sensorClient.connect();
            if(sensorClient.isIsServerAvaialable())
            UIObject.jLabel27.setText("Connected");
        } catch (Exception ex) {
            Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
            UIObject.txtLog.append("Cannot connect to the server. \n");
            if(ui)
            UIObject.handleError("Cannot connect to the server");
        }
    }

    /**
     * Initialize serial port with connected sensor port
     */
    public void initSerialPort() {
        if (isSensorConnected) {
            disConnectSensor();
        }
        CommPortIdentifier commPort = SerialPortFinder.findSensorPort(UIObject);
        if (commPort != null) {
            connect(commPort);
        } else {
            isSensorConnected = false;
            UIObject.handleError("Check the sensor connection and try again");
        }
        UIObject.setConnected(isSensorConnected);
    }

    /**
     * connect to the selected port in the combo box pre: ports are already
     * found by using the searchForPorts method post: the connected comm port is
     * stored in commPort, otherwise, an exception is generated
     *
     * @param commPortIdentifier
     */
    public void connect(CommPortIdentifier commPortIdentifier) {
        isSensorConnected = false;
        try {
            //the method below returns an object of type CommPort
            CommPort commPort = commPortIdentifier.open("RFID", TIMEOUT);
            //the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort) commPort;
            //setting serial port parameters 
            //this setting is based on telosb mote specification
            //baud rate is important here i.e(9600)
            //for controlling GUI elements
            serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            initIOStream();
            initPacketReader();
            isSensorConnected = true;
            UIObject.setConnected(isSensorConnected);

            //logging
            String logText = commPortIdentifier.getName() + " opened successfully.";
            UIObject.txtLog.setForeground(Color.black);
            UIObject.txtLog.append(logText + "\n");

        } catch (PortInUseException e) {
            String logText = commPortIdentifier.getName() + " is in use. (" + e.toString() + ")";
            Logger.getLogger(logText).log(Level.OFF, logText, e);
            UIObject.handleError(logText);
            serialPort.close();
        } catch (UnsupportedCommOperationException ex) {
            String logText = "Unsupported ComPort" + commPortIdentifier.getName() + "(" + ex.toString() + ")";
            Logger.getLogger(logText).log(Level.OFF, logText, ex);
            UIObject.handleError(logText);
            serialPort.close();
        } catch (Exception e) {
            String logText = "Failed to open " + commPortIdentifier.getName() + "(" + e.toString() + ")";
            Logger.getLogger(logText).log(Level.OFF, logText, e);
            UIObject.handleError("Not able to communicate with sensor, try again");
            serialPort.close();
        }

    }

    /**
     * open the input and output streams pre: an open port post:initialized
     * input and output streams for use to communicate data
     *
     * @return
     */
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
    private boolean initPacketReader() throws InterruptedException {

        //return value for whather opening the streams is successful or not
        boolean successful;

        try {

            port = new rtpmt.sensor.reader.SerialPort(input, output);
            boolean isRealTime = false;
            packetReader = new SensorReader(port, isRealTime);
            packetReader.addSensorEventHandler(this);
            packetReader.open();
            successful = true;
        } catch (IOException ex) {
            String logText = "I/O Streams failed to open. (" + ex.toString() + ")";
            UIObject.txtLog.setForeground(Color.red);
            UIObject.txtLog.append(logText + "\n");
            successful = false;
        }

        return successful;
    }

    /**
     * Disconnect the sensor
     */
    private void disConnectSensor() {
        try {
            if (packetReader != null) {
                packetReader.close();
            }
            if (serialPort != null) {
                serialPort.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            serialPort = null;
            packetReader = null;
        }
    }

    void saveDataLocally() {
        pushToServer = false;
        if (isSensorConnected) {
            bar = new ProgressBar(100, "Starting to read data",UIObject);
            UIObject.txtLog.append("Starting to save data. \n");
            try {
                Package pack = PackageList.getPackage(0);
                if (pack != null) {
                    writeConfigData();
                    DATA_LOG_FILE = FOLDER + pack.getUniqueId();
                    File file = new File(DATA_LOG_FILE);
                    file.deleteOnExit();
                    file.createNewFile();
                    datalog = new FileOutputStream(file);
                    packetReader.readPacket();
                    datalog.close();
                    System.out.println("File length "+file.length()+" last modified "+ file.lastModified());
                    UIObject.handleError("Sensor data read successfully");
                    UIObject.txtLog.append("Saved data successfully to "+file.toPath()+". \n");
                }
            } catch (IOException ex) {
                Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
                UIObject.handleError("Problem with connecting to sensor.Try again");
                UIObject.txtLog.append("Problem with connecting to sensor. Data not saved completely. \n");
            }
            catch (Exception ex) {
                Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
                UIObject.handleError("Problem with connecting to sensor.Try again");
                UIObject.txtLog.append("Problem with connecting to sensor. Data not saved completely. \n");
            }
            finally{
                bar.done();
            }
        } else {
            UIObject.handleError("Connect the sensor and try again");
            UIObject.txtLog.append("Connect the sensor and try again. Data not saved. \n");
        }
    }

    //thread to get the mote packet from the queue and send it to the server
    @Override
    public void run() {
        try {
            for (;;) {

                Packet packet = packetReader.readPacket();
                if (packet != null) {
                    PackageInformation sensorInfo = packet.getBlackBoxMessage();
                    String message = sensorInfo.getSensorId() + "," + sensorInfo.getTimeStamp() + ",";
                    UIObject.txtLog.append(message);
                    for (PackageInformation.Sensor sensor : sensorInfo.getSensorsList()) {

                        message = sensor.getSensorType().name() + " : " + sensor.getSensorValue() + " " + sensor.getSensorUnit();
                        UIObject.txtLog.append(message + "\n");
                    }
                    sensorInfo.writeDelimitedTo(datalog);
                }
            }
        } catch (IOException ex) {
            String logText = "Too many listeners. (" + ex.toString() + ")";
            UIObject.txtLog.setForeground(Color.red);
            UIObject.txtLog.append(logText + "\n");
            Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            UIObject.handleError(ex.toString());
            Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                datalog.close();
            } catch (IOException ex) {
                Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void configureSensor(Package packs) {

        try {
            if (!isSensorConnected || !packetReader.configure(packs)) {
                UIObject.handleError("Cannot configure the sensor, try again");
            } else {
                UIObject.handleError("Sensor configured Successfuly");
            }
        } catch (IOException ex) {
            String logText = "Too many listeners. (" + ex.toString() + ")";
            UIObject.txtLog.setForeground(Color.red);
            UIObject.txtLog.append(logText + "\n");
            Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
            UIObject.handleError("Cannot configure the sensor, try again");
        } catch (NullPointerException ex) {
            String logText = "NUll Pointer Exception. (" + ex.toString() + ")";
            UIObject.txtLog.setForeground(Color.red);
            UIObject.txtLog.append(logText + "\n");
            UIObject.handleError("Cannot configure the sensor, try again");
        } catch (InterruptedException ex) {
            Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
            String logText = "NUll Pointer Exception. (" + ex.toString() + ")";
            UIObject.handleError("Cannot configure the sensor, try again");
        }

    }

    void clearSensorData() {
        if (isSensorConnected) {
            try {
                packetReader.clearData();
                UIObject.handleError("Successfully cleared the data.");
            } catch (InterruptedException ex) {
                Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
                UIObject.handleError("Not able to clear the data. Try again");
            } catch (IOException ex) {
                Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
                UIObject.handleError("Not able to clear the data. Try again");
            }
            catch (Exception ex) {
                Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
                UIObject.handleError("Not able to clear the data. Try again");
            }
        } else {
            UIObject.handleError("Failed to communicated with sensor. Try again");
        }
    }

    void restoreDefaultConfig() {
        if (isSensorConnected) {
            try {
                packetReader.resetConfig();
            } catch (InterruptedException ex) {
                Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
                UIObject.handleError("Not able to clear the data. Try again");
            } catch (IOException ex) {
                Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
                UIObject.handleError("Not able to clear the data. Try again");
            }
        } else {
            UIObject.handleError("Failed to communicated with sensor. Try again");
        }
    }

    void generateCSV(File file) throws FileNotFoundException, Exception {
        try {
            FileWriter fw = new FileWriter();
            FileInputStream inputStream = new FileInputStream(DATA_LOG_FILE);
            fw.writeCSV(inputStream, file);
        } catch (Exception ex) {
            Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public void newSensorAdded(Package newPackage) {
        UIObject.setSesnorId(String.valueOf(newPackage.getSensorId()));
        UIObject.updateSensorDetail(newPackage);
        UIObject.txtLog.append("Sensor " + newPackage.getSensorId() + " is using ShortId:" + newPackage.getShortId() + "\n");
    }

    @Override
    public void handleNewPacket(Packet packet) {
        if (packet != null) {
            
            PackageInformation sensorInfo = packet.getBlackBoxMessage();
            String message = sensorInfo.getSensorId() + "," + sensorInfo.getTimeStamp() + ",";
            //UIObject.txtLog.append(message);
            for (PackageInformation.Sensor sensor : sensorInfo.getSensorsList()) {

                message = sensor.getSensorType().name() + " : " + sensor.getSensorValue() + " " + sensor.getSensorUnit();
                UIObject.txtLog.append(message + "\n");
            }
            bar.setProgress(100, "Reading "+message);
            try {
                if(sensorClient.isIsServerAvaialable() && pushToServer)
                    sensorClient.send(sensorInfo);
                else
                    sensorInfo.writeDelimitedTo(datalog);
                
            } catch (IOException ex) {
                Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void writeConfigData() throws IOException {
        try {

            Package pack = PackageList.getPackage(0);
            if (pack != null) {
                String CONFIG_LOG_FILE = CONFIG_FOLDER + pack.getUniqueId();
                File file = new File(CONFIG_LOG_FILE);
                file.deleteOnExit();
                file.createNewFile();
                FileOutputStream configdatalog = new FileOutputStream(file);
                pack.getConfigMessage(false).writeDelimitedTo(configdatalog);
                configdatalog.close();
                if(sensorClient.isIsServerAvaialable())
                sensorClient.send(pack.getConfigMessage(false));
            }

        } catch (IOException ex) {
            Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    void populateLocalData() {
        try {
            ArrayList<File> fileList = FileReader.listFilesForFolder(new File(FOLDER));

            DefaultTableModel model = (DefaultTableModel) UIObject.jtblLocalData.getModel();

            for (File file : fileList) {
                String[] fieldArray = file.getName().split(Constants.SEPERATOR);
                String column1 = fieldArray[0];
                String column2 = fieldArray[1];
                String column3 = fieldArray[2];
                Date date = new Date(file.lastModified());

                String column4 = date.toString();
                model.addRow(new Object[]{column1, column2, column3, column4});
            }

        } catch (Exception ex) {
            Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void pushToServer() {
        
        pushToServer = true;
        if (isSensorConnected) {
            bar = new ProgressBar(100, "Starting to read data",UIObject);
            try {
                Package pack = PackageList.getPackage(0);
                if (pack != null) {
                    writeConfigData();
                    DATA_LOG_FILE = FOLDER + pack.getUniqueId();
                    File file = new File(DATA_LOG_FILE);
                    file.deleteOnExit();
                    file.createNewFile();
                    datalog = new FileOutputStream(file);
                    packetReader.readPacket();
                    datalog.close();
                    System.out.println("File length "+file.length()+" last modified "+ file.lastModified());
                    if(file.length()==0)
                    {
                        file.delete();
                        Files.delete(Paths.get(CONFIG_FOLDER+pack.getUniqueId()));
                    }
                    UIObject.handleError("Sensor data read successfully");
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
                UIObject.handleError("Problem with connecting to sensor.Try again");
            }
            catch (Exception ex) {
                Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
                UIObject.handleError("Problem with connecting to sensor.Try again");
            }
            finally{
                bar.done();
            }
        } else {
            UIObject.handleError("Connect the sensor and try again");
        }
        }

    void pushLocalFilesToServer() {
        ProgressBar bar1 = new ProgressBar(100, "Starting to send files",UIObject);
         try {
             
             if(sensorClient.isIsServerAvaialable())
             {
            ArrayList<File> fileList = FileReader.listFilesForFolder(new File(FOLDER));
            int length = 0;
            int totalFiles = fileList.size();
            int count = 1;
            
            for (File file : fileList) {
                bar1.setProgress(100, "Sending "+count+"/"+totalFiles+" files");
                count++;
                String fileName = file.toString().substring(file.toString().indexOf("\\")+1);
                File configFile = new File(CONFIG_FOLDER+fileName);
                
                FileInputStream fis = new FileInputStream(file);
		DataInputStream dis = new DataInputStream(fis);
                
                for(;;){
                    PackageInformation sensorInfo;
                    sensorInfo = PackageInformation.parseDelimitedFrom(dis);
                    
                    if (sensorInfo == null) {
                        break;
                    }
                    
                    sensorClient.send(sensorInfo);
            
                }
                length = dis.available();
                fis.close();
                dis.close();
                if(length == 0)
                    file.delete();
                
                FileInputStream configFis = new FileInputStream(configFile);
                DataInputStream configDis = new DataInputStream(configFis);
                
                for(;;){
                    PackageInformation sensorInfo;
                    
                    sensorInfo = PackageInformation.parseDelimitedFrom(configDis);
                    if (sensorInfo == null) {
                        break;
                    }
                    
                    sensorClient.send(sensorInfo);
            
                }
                configDis.close();
                configFis.close();
                if(length == 0)
                    configFile.delete();
                UIObject.clearLocalDataTable();
                populateLocalData();
                UIObject.jtblLocalData.repaint(UIObject.jtblLocalData.getBounds());
                
            }
            
             }
            else
            {
                    UIObject.handleError("Server not Connected.Try again");
            }
                
                       
        } catch (Exception ex) {
            Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
         finally{
             bar1.done();
         }
    }

    void saveAsCSV(String path) {
        try {
            ArrayList<File> fileList = FileReader.listFilesForFolder(new File(FOLDER));
            for (File file : fileList) {
                String fileName = file.toString().substring(file.toString().indexOf("\\")+1);
                String csvFile = path+"\\"+fileName+".csv";
                System.out.println(csvFile);
                 try {
            FileWriter fw = new FileWriter();
            FileInputStream inputStream = new FileInputStream(file);
            fw.writeCSV(inputStream, new File(csvFile));
        } catch (Exception ex) {
            Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
                 UIObject.handleError("CSV created");
               
            }
        } catch (Exception ex) {
            UIObject.handleError("CSV not created");
            Logger.getLogger(UIEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
