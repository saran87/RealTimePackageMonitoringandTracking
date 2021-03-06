/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.motes.packet;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rtpmt.packages.Package;
import rtpmt.packages.PackageList;
import rtpmt.packages.SensorEventHandler;
import rtpmt.sensor.reader.SerialPortInterface;
import rtpmt.sensor.util.Constants;
import rtpmt.sensor.util.Packet;
import rtpmt.sensor.util.Utils;

/**
 *
 * @author Kumar
 */
public class BlackBoxReader extends AbstractSource {

    final static boolean DEBUG = true;
    private final SerialPortInterface port;
    private boolean inSync;
    private final LinkedList[] received;
    private final ConcurrentHashMap<String, Packet> partialData;

    /**
     * Packetizers are built using the makes methods in BuildSource
     *
     * @param name
     * @param _inputPort
     */
    public BlackBoxReader(String name, SerialPortInterface _inputPort) {
        super(name);
        this.port = _inputPort;
        inSync = false;
        received = new LinkedList[256];
        received[Constants.P_ACK] = new LinkedList<Packet>();
        received[Constants.P_UPDATE] = new LinkedList<Packet>();
        partialData = new ConcurrentHashMap<String, Packet>();
    }

    @Override
    protected void openSource() throws IOException, InterruptedException {
        port.open();
        getSensorInformation();
    }

    @Override
    public boolean configure(Package configPack) throws IOException, NullPointerException, InterruptedException {
        writeFramedPacket(Constants.FORMAT_SD_CARD, dummyPacket);
        writeFramedPacket(Constants.FORMAT_FLASH, dummyPacket);
        Thread.sleep(10000);
        writeFramedPacket(Constants.SET_ALL_CONFIG, configPack.getBlackBoxConfigPacket());
        writeFramedPacket(Constants.SAVE_ALL_CONFIG, dummyPacket);
        writeFramedPacket(Constants.SAVE_COMMENT, configPack.getNote().getBytes());
        Package updatedPack = getSensorInformation();
        sendTimeSyncPacket(0);
        return configPack.equals(updatedPack);
    }

    /**
     * Reads the SD Card Data
     *
     * @return Packet object
     * @throws IOException
     */
    @Override
    public Packet readPacket() throws IOException {
        try {
            readSDCardData();
        } catch (InterruptedException ex) {
            Logger.getLogger(BlackBoxReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(BlackBoxReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    protected void closeSource() throws IOException {
        port.close();
    }

     @Override
    public void reset() throws InterruptedException, IOException {
        writeFramedPacket(Constants.RESET_BOARD, dummyPacket);
        getSensorInformation();
    }

    @Override
    public void resetConfig() throws InterruptedException, IOException {
        writeFramedPacket(Constants.RESET_CONFIG, dummyPacket);
        getSensorInformation();
    }

    @Override
    public void resetRadio() throws InterruptedException, IOException {
        writeFramedPacket(Constants.RESET_RADIO, dummyPacket);
        getSensorInformation();
    }

    @Override
    public void clearData() throws InterruptedException, IOException {
        writeFramedPacket(Constants.FORMAT_SD_CARD, dummyPacket);
        writeFramedPacket(Constants.FORMAT_FLASH, dummyPacket);
        Thread.sleep(10000);
        getSensorInformation();
    }
    
    @Override
    public void calibrateSensor() throws InterruptedException,IOException{
        writeFramedPacket(Constants.RUN_ACCELEROMETER_CALIBRATION, dummyPacket);
        Thread.sleep(3000);
        writeFramedPacket(Constants.SAVE_ALL_CONFIG, dummyPacket);
        getSensorInformation();
    }

    /**
     *
     * @return @throws IOException
     * @throws InterruptedException
     */
    public Package getSensorInformation() throws IOException, InterruptedException {
        String sensorId;
        sensorId = getBoardId();
        Packet packet = getConfiguration();
        String note = getNote();
        int batteryLevel =  getBatteryLevel();
        Package pack = getPackage(sensorId, packet, note,batteryLevel);
        //for blackbox only one sensor is used;
        PackageList.addPackage(0, pack);
        publishNewSensor(pack);
        return pack;
    }

    /**
     *
     * @return @throws IOException
     * @throws InterruptedException
     */
    private Packet getConfiguration() throws IOException, InterruptedException {
        writeFramedPacket(Constants.GET_ALL_CONFIG, dummyPacket);
        Packet packet = getSerialPacket(false);
        return packet;
    }

    /**
     *
     * @return @throws IOException
     * @throws InterruptedException
     */
    private String getBoardId() throws IOException, InterruptedException {

        writeFramedPacket(Constants.GET_BOARD_ID, dummyPacket);
        Packet packet = getSerialPacket(false);
        return packet.sensorId();
    }

    /**
     *
     * @return @throws IOException
     * @throws InterruptedException
     */
    private String getNote() throws IOException, InterruptedException {
        writeFramedPacket(Constants.GET_NOTE, dummyPacket);
        Packet packet = getSerialPacket(true);
        Byte[] byteArray = packet.getData();
        char[] charArray = new char[byteArray.length];
        for (int i = 0; i < charArray.length; i++) {
            charArray[i] = (char) byteArray[i].byteValue();
        }

        return String.valueOf(charArray);
    }
    
   
    /**
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private int getBatteryLevel() throws IOException, InterruptedException  {
         writeFramedPacket(Constants.GET_BATTERY_LEVEL, dummyPacket);
         Packet packet =  getSerialPacket(true);
         return packet.getBatteryLevel();
    }


    /**
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void readSDCardData() throws IOException, InterruptedException, TimeoutException {
        Package pack = PackageList.getPackage(0);
        if (pack.isFlashDataAvailable()) {
            System.out.print("Reading flash data");
            writeFramedPacket(Constants.GET_FLASH, dummyPacket);
            processSDCardPacket();
        }
        System.out.println("Reading Temperature");
        writeFramedPacket(Constants.GET_TEMPERATURE, dummyPacket);
        processSDCardPacket();
        System.out.println("Reading Humididty");
        writeFramedPacket(Constants.GET_HUMIDITY, dummyPacket);
        processSDCardPacket();
        System.out.println("Reading Vibration");
        writeFramedPacket(Constants.GET_VIBRATION, dummyPacket);
        processSDCardPacket();
        System.out.println("Reading Shock");
        writeFramedPacket(Constants.GET_SHOCK, dummyPacket);
        processSDCardPacket();
        writeFramedPacket(Constants.FORMAT_FLASH, dummyPacket);
    }

    // Class to build a framed, escaped and crced packet byte stream
    private void sendTimeSyncPacket(int nodeId) throws IOException {
        try {
            long currentTime = System.currentTimeMillis() / 1000;
            System.out.println("SendTime: " + currentTime);
            byte timePacket[] = (new BigInteger(Long.toHexString(currentTime), 16)).toByteArray();//ByteBuffer.allocate(8). array();
            writeFramedPacket(Constants.P_TIME_SYNC, nodeId, timePacket);
        } catch (InterruptedException ex) {
            Logger.getLogger(BlackBoxReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    /**
     *
     * @throws IOException
     * @throws java.util.concurrent.TimeoutException
     * @throws java.lang.InterruptedException
     */
    public void processSDCardPacket() throws IOException, TimeoutException, InterruptedException {
        int count = 0;
        boolean isLength = false;
        int payLoad = 0;
        inSync = false;
        byte[] syncFrame = new byte[Constants.FRAME_SYNC.length];
        byte[] receiveBuffer = new byte[Constants.MTU];
        int notInSyncCount = 0;
        //long timeout = 100000;
        for (;;) {
            if (!inSync) {
                long now = System.currentTimeMillis();
                int b = 0;
                /*while (!port.isAvailable()) {
                 timeout = timeout - 10;
                 if (timeout <= 0) {
                 System.out.print("Timeout = "+timeout);
                 throw new TimeoutException("No data from sensor.Time out");
                 }
                 }*/
                while (b != 170) {
                    b = port.read() & 0xff;
                    notInSyncCount++;
                }
                if (count >= Constants.MTU) {
                    // PacketHelper too long, give up and try to resync
                    message(name + ": packet too long");
                    inSync = false;
                    count = 0;
                    continue;
                }
                if (b == 170) {
                    syncFrame[count++] = (byte) (b & 0xff);

                    while (count < Constants.FRAME_SYNC.length && notInSyncCount < Constants.MTU) {
                        b = port.read();
                        syncFrame[count++] = (byte) (b & 0xff);
                        notInSyncCount++;
                    }
                }

                if (DEBUG) {
                    // Dump.printPacket(System.out, syncFrame);
                }

                if (Utils.compare(syncFrame, Constants.FRAME_SYNC)) {
                    inSync = true;
                } else if (notInSyncCount >= Constants.MTU) {
                    throw new TimeoutException("No data from sensor.Time out");
                }
                count = 0;
            } else {
                byte b;
                if (!isLength) {
                    byte command = port.read();
                    receiveBuffer[count++] = command;

                    receiveBuffer[count++] = port.read();
                    receiveBuffer[count++] = port.read();
                    receiveBuffer[count++] = port.read();
                    receiveBuffer[count++] = port.read();
                    int length;

                    length = (receiveBuffer[count - 1] & 0xff) | (receiveBuffer[count - 2] & 0xff) << 8
                            | (receiveBuffer[count - 3] & 0xff) << 16 | (receiveBuffer[count - 4] & 0xff) << 24;

                    payLoad = length;
                    isLength = true;
                    Dump.dump("Recieved File Response", receiveBuffer);
                    break;
                }
            }
        }

        if (isLength) {
            TestReading reader = new TestReading(port, payLoad);
            reader.start();

            Thread.sleep(100);
            startProcessingData(payLoad, reader);
            // printData(reader);
        }
    }

    private void startProcessingData(int length, TestReading reader) throws IOException, InterruptedException {
        if (DEBUG) {
            System.err.println("Initial Length " + length);
        }
        int counter = 0;

        while (!reader.isComplete) {
            Thread.sleep(1000);
        }
        FileInputStream input = new FileInputStream(reader.dataFile);
        int timeOut = 0;

        while (length > 2) {
            byte[] rawPacket = readFramedPacket(false, input);

            if (rawPacket != null) {
                //Add plus 5 to normal packet, 5 is for adding Frame packet(3) and CRC(2)
                counter++;
                timeOut = 0;
                length = length - (rawPacket.length + 5);
                Packet pack = new Packet(rawPacket);
                if (DEBUG) {
                    if (counter >= 469) {
                        System.err.println("Length " + length + " counter " + counter);
                    }
                }
                if (counter >= 468) {
                    System.out.println("counter : " + counter);
                }
                if (pack.isPartialPacket()) {
                    handlePartialPackets(pack);
                } else {
                    publishNewPacket(pack);
                }
            }
            else{
                timeOut++;
            }
            if(timeOut > Constants.MTU)
                break;
        }
        input.close();
        reader.dataFile.delete();
    }

    /**
     *
     * @param isSDCardData
     * @return
     * @throws IOException
     */
    public Packet getSerialPacket(boolean isSDCardData) throws IOException {
        byte[] packet = readFramedPacket(isSDCardData);
        Packet packetHelper = new Packet(packet);

        return packetHelper;
    }

    /**
     *
     * @param newPackage
     */
    private void publishNewSensor(Package newPackage) {
        if (eventListenerObjects != null) {
            for (SensorEventHandler iSensorEventHandler : eventListenerObjects) {
                iSensorEventHandler.newSensorAdded(newPackage);
            }
        }
    }

    /**
     *
     * @param newPackage
     */
    private void publishNewPacket(Packet packet) {

        if (eventListenerObjects != null) {
            for (SensorEventHandler iSensorEventHandler : eventListenerObjects) {
                iSensorEventHandler.handleNewPacket(packet);
            }
        }
    }
    /*
     * Packet Reader
     */

    // Read system-level packet. If inSync is false, we currently don't
    // have sync
    public byte[] readFramedPacket(boolean readSDCard) throws IOException {

        int count = 0;
        boolean isLength = false;
        int payLoad = 0;
        inSync = false;
        byte[] syncFrame = new byte[Constants.FRAME_SYNC.length];
        byte[] receiveBuffer = new byte[Constants.MTU];
        int notInSyncCount = 0;
        long timeout = 100000;
        for (;;) {
            if (!inSync) {
                long now = System.currentTimeMillis();
                int b = 0;

                while (!port.isAvailable()) {
                    timeout = timeout - 10;
                    if (timeout <= 0) {
                        System.out.print(timeout);
                        return null;
                    }
                }

                while (b != 170) {
                    b = port.read() & 0xff;
                    notInSyncCount++;
                    if (notInSyncCount > Constants.MTU) {
                        return null;
                    }
                }
                if (count >= Constants.MTU) {
                    // PacketHelper too long, give up and try to resync
                    message(name + ": packet too long");
                    inSync = false;
                    count = 0;
                    continue;
                }
                if (b == 170) {
                    syncFrame[count++] = (byte) (b & 0xff);

                    while (count < Constants.FRAME_SYNC.length && notInSyncCount < Constants.MTU) {
                        b = port.read();
                        syncFrame[count++] = (byte) (b & 0xff);
                        notInSyncCount++;
                    }
                }

                if (DEBUG) {
                    Dump.dump(System.out, "Sync frame", syncFrame);
                }

                if (Utils.compare(syncFrame, Constants.FRAME_SYNC)) {
                    inSync = true;
                } else if (notInSyncCount >= Constants.MTU) {
                    return null;
                }
                count = 0;
            } else {
                byte b;
                if (!isLength) {
                    byte command = port.read();
                    receiveBuffer[count++] = command;

                    receiveBuffer[count++] = port.read();
                    receiveBuffer[count++] = port.read();
                    receiveBuffer[count++] = port.read();
                    receiveBuffer[count++] = port.read();
                    int length;
                    if (!readSDCard) {
                        length = (receiveBuffer[count - 1] & 0xff) | (receiveBuffer[count - 2] & 0xff) << 8;
                    } else {

                        length = (receiveBuffer[count - 1] & 0xff) | (receiveBuffer[count - 2] & 0xff) << 8
                                | (receiveBuffer[count - 3] & 0xff) << 16 | (receiveBuffer[count - 4] & 0xff) << 24;
                    }

                    payLoad = count + length;
                    isLength = true;
                    if (payLoad > 600) {
                        Dump.dump(System.out, "Exceeded payload length", receiveBuffer);
                    }
                    System.out.println("Payload length :" + payLoad);
                    continue;
                } else if (count < payLoad) {
                    b = (byte) (port.read() & 0xff);
                } else {
                    byte[] packet = new byte[count - 2];
                    System.arraycopy(receiveBuffer, 0, packet, 0, count - 2);

                    int readCrc = (receiveBuffer[count - 1] & 0xff)
                            | (receiveBuffer[count - 2] & 0xff) << 8;
                    int computedCrc = Crc.calc(packet, packet.length);

                    if (DEBUG) {
                        Dump.printPacket(System.err, packet);
                        System.err.println(" rcrc: " + Integer.toHexString(readCrc)
                                + " ccrc: " + Integer.toHexString(computedCrc));
                    }

                    //if (readCrc != computedCrc) {
                    return packet;
                    /* } else {
                     message(name + ": bad packet");
                     /*
                     * We don't lose sync here. If we did, garbage on the line at startup
                     * will cause loss of the first packet.
                     *
                     count = 0;
                     inSync = false;
                     continue;
                     }*/
                }
                receiveBuffer[count++] = b;
            }
        }
    }

    /**
     *
     * @param packetType
     * @param deadline
     * @return
     * @throws IOException
     */
    protected Packet readProtocolPacket(int packetType, long deadline)
            throws IOException {
        LinkedList inPackets;
        inPackets = received[packetType];

        // Wait for a packet on inPackets
        synchronized (inPackets) {
            while (inPackets.isEmpty()) {
                long now = System.currentTimeMillis();
                if (deadline != 0 && now >= deadline) {
                    return null;
                }
                try {
                    inPackets.wait(deadline != 0 ? deadline - now : 0);
                } catch (InterruptedException e) {
                    throw new IOException("interrupted");
                }
            }
            return (Packet) inPackets.removeFirst();
        }
    }

    /**
     * Place a packet in its packet queue, or reject unknown packet types (which
     * don't have a queue)
     *
     * @param packetType
     * @param packet
     */
    @SuppressWarnings("unchecked")
    protected void pushProtocolPacket(int packetType, Packet packet) {
        LinkedList<Packet> inPackets;
        inPackets = received[packetType];

        if (inPackets != null) {

            synchronized (inPackets) {
                inPackets.add(packet);
                inPackets.notify();
            }

        } else if (packetType != Constants.P_UNKNOWN) {
            message(name + ": ignoring unknown packet type 0x"
                    + Integer.toHexString(packetType));
        }
    }

    /**
     * TO-DO check the working of combining packets
     *
     * @param packet
     */
    private void handlePartialPackets(Packet packet) {
        if (packet.isPartialPacket()) {
            String key = packet.uniqueId();
            System.out.println("Key:" + key);
            if (partialData.containsKey(key)) {
                Packet partialpacket = partialData.get(key);
                if (partialpacket.isCompletePacket(packet)) {
                    partialpacket.combinePacket(packet);
                    publishNewPacket(partialData.remove(key));
                } else {
                    System.out.println("mismatch packet");
                    Dump.dump(packet);
                }

            } else {
                partialData.put(key, packet);
            }
        }
    }

    private static final byte dummyPacket[] = new byte[2];

    /**
     * Write a packet of type 'packetType', first byte 'firstByte and bytes
     * 2..'count'+1 in 'packet'
     *
     * @param packet
     * @throws IOException
     */
    private synchronized boolean writeFramedPacket(byte[] command,
            byte[] packet) throws IOException, InterruptedException {

        SensorPacket buffer = new SensorPacket(packet.length + command.length + 7);
        int packetType = Constants.P_BLACKBOX_REQUEST;
        buffer.nextByte(packetType);

        //Node Id is 16 bit
        int nodeId = 0;
        buffer.nextByte(nodeId >> 8);
        buffer.nextByte(nodeId & 0xff);

        //+2 for crc //length 
        int length = packet.length + command.length + 2;
        buffer.nextByte(length >> 8);
        buffer.nextByte(length & 0xff);

        //Add command
        for (int i = 0; i < command.length; i++) {
            buffer.nextByte(command[i]);
        }

        //Add values
        for (int i = 0; i < packet.length; i++) {
            buffer.nextByte(packet[i]);
        }

        buffer.terminate();
        
        return _write(buffer);
        
    }

    private synchronized boolean writeFramedPacket(int packetType, int nodeId,
            byte[] packet) throws IOException, InterruptedException {

        SensorPacket buffer = new SensorPacket(packet.length + 7);

        buffer.nextByte(packetType);

        //Node Id is 16 bit
        buffer.nextByte(nodeId >> 8);
        buffer.nextByte(nodeId & 0xff);

        //+2 for crc
        int length = packet.length + 2;
        //length 
        buffer.nextByte(length >> 8);
        buffer.nextByte(length & 0xff);

        for (int i = 0; i < length - 2; i++) {
            buffer.nextByte(packet[i]);
            System.out.println(i);
        }

        buffer.terminate();
        
        return _write(buffer);
    }
    
    private boolean _write(SensorPacket buffer) throws IOException, InterruptedException{
        
        byte[] realPacket = new byte[buffer.escapePtr];
        System.arraycopy(buffer.escaped, 0, realPacket, 0, buffer.escapePtr);

        port.flush();
        boolean write = port.write(realPacket);
        port.flush();
        if (DEBUG) {
            Dump.dump(System.err, "Sending :", realPacket);
            System.err.println();
        }
        Thread.sleep(50);
        return write;
    }
    
    private Package getPackage(String sensorId, Packet packet, String note,int batteryLevel) {
        Package pack = new Package(0, sensorId);
        pack.setNote(note);
        int i = -1;
        int rawThreshold = (packet.getData(++i) & 0xff) | (packet.getData(++i) & 0xff) << 8;
        System.err.println("public final static int TEMP_MAX_THRES_INDEX = " + (i - 1));
        i += 2;
        int time = (packet.getData(++i) & 0xff) | (packet.getData(++i) & 0xff) << 8;
        System.err.println("public final static int TEMP_TIME_INDEX = " + (i - 1));
        int overtime = (packet.getData(++i) & 0xff) | (packet.getData(++i) & 0xff) << 8;
        System.err.println("public final static int TEMP_TIME_THRES_INDEX = " + (i - 1));
        pack.setMaxTemperatureThreshold(rawThreshold);
        pack.setTemperatureTimePeriod(time);
        pack.setTemperatureAfterThresholdTimePeriod(overtime);

        i += 5;
        rawThreshold = (packet.getData(++i) & 0xff) | (packet.getData(++i) & 0xff) << 8;
        System.err.println("public final static int HUMD_MAX_THRES_INDEX = " + (i - 1));
        i = i + 2;
        time = (packet.getData(++i) & 0xff) | (packet.getData(++i) & 0xff) << 8;
        System.err.println("public final static int HUMD_TIME_INDEX = " + (i - 1));
        overtime = (packet.getData(++i) & 0xff) | (packet.getData(++i) & 0xff) << 8;
        System.err.println("public final static int HUMD_TIME_THRES_INDEX = " + (i - 1));
        pack.setMaxHumidtyThreshold(rawThreshold);
        pack.setHumididtyTimePeriod(time);
        pack.setHumididtyAfterThresholdTimePeriod(overtime);

        i += 5;
        rawThreshold = (packet.getData(++i) & 0xff) | (packet.getData(++i) & 0xff) << 8;
        System.err.println("public final static int SHOCK_MAX_THRES_INDEX = " + (i - 1));
        pack.setMaxShockThreshold(rawThreshold);

        i += 22;
        rawThreshold = (packet.getData(++i) & 0xff) | (packet.getData(++i) & 0xff) << 8;
        System.err.println("public final static int VIB_MAX_THRES_INDEX = " + (i - 1));
        i += 4;
        time = (packet.getData(++i) & 0xff) | (packet.getData(++i) & 0xff) << 8;
        System.err.println("public final static int VIB_TIME_INDEX = " + (i - 1));
        i += 4;
        overtime = (packet.getData(++i) & 0xff) | (packet.getData(++i) & 0xff) << 8;
        System.err.println("public final static int VIB_TIME_THRES_INDEX = " + (i - 1));

        pack.setVibrationTimePeriod(time);
        pack.setVibrationAfterThresholdTimePeriod(overtime);
        pack.setMaxVibrationThreshold(rawThreshold);
        pack.setIsFlashDataAvailable((packet.getData(Constants.DATA_IN_FLASH_INDEX) & 0xff));
        int value = (packet.getData(Constants.VIB_X_MAXIMUM_INDEX) & 0xff) | (packet.getData(Constants.VIB_X_MAXIMUM_INDEX+1) & 0xff) << 8;
        pack.setMaxVibXaxis(value);
        value = (packet.getData(Constants.VIB_Y_MAXIMUM_INDEX) & 0xff) | (packet.getData(Constants.VIB_Y_MAXIMUM_INDEX+1) & 0xff) << 8;
        pack.setMaxVibYaxis(value);
        value = (packet.getData(Constants.VIB_Z_MAXIMUM_INDEX) & 0xff) | (packet.getData(Constants.VIB_Z_MAXIMUM_INDEX+1) & 0xff) << 8;
        pack.setMaxVibZaxis(value);
        value = (packet.getData(Constants.VIB_X_MINIMUM_INDEX) & 0xff) | (packet.getData(Constants.VIB_X_MINIMUM_INDEX+1) & 0xff) << 8;
        pack.setMinVibXaxis(value);
        value = (packet.getData(Constants.VIB_Y_MINIMUM_INDEX) & 0xff) | (packet.getData(Constants.VIB_Y_MINIMUM_INDEX+1) & 0xff) << 8;
        pack.setMinVibYaxis(value);
        value = (packet.getData(Constants.VIB_Z_MINIMUM_INDEX) & 0xff) | (packet.getData(Constants.VIB_Z_MINIMUM_INDEX+1) & 0xff) << 8;
        pack.setMinVibZaxis(value);
        pack.setOffsetVibXaxis((packet.getData(Constants.VIB_X_OFFSET_INDEX) & 0xff));
        pack.setOffsetVibYaxis((packet.getData(Constants.VIB_Y_OFFSET_INDEX) & 0xff));
        pack.setOffsetVibZaxis((packet.getData(Constants.VIB_Z_OFFSET_INDEX) & 0xff));
        pack.setBatteryLevel(batteryLevel);
        return pack;
    }

    private void printData(TestReading reader) throws IOException, InterruptedException {
        // byte[] data = new byte[payLoad];
        int i = 0;
        while (!reader.isComplete) {
            Thread.sleep(1000);
        }
        FileInputStream input = new FileInputStream(reader.dataFile);
        int tempData = input.read();
        while (tempData != -1) {

            System.out.print(" " + Integer.toHexString(tempData & 0xff));
            tempData = input.read();
        }
        System.out.println("");
        input.close();
        reader.dataFile.delete();
        //Dump.dump(System.out,"Data from SD :",data);
    }

    public byte[] readFramedPacket(boolean readSDCard, FileInputStream input) throws IOException, InterruptedException {

        int count = 0;
        boolean isLength = false;
        int payLoad = 0;
        inSync = false;
        byte[] syncFrame = new byte[Constants.FRAME_SYNC.length];
        byte[] receiveBuffer = new byte[Constants.MTU];
        int notInSyncCount = 0;

        for (;;) {
            if (!inSync) {

                int b = 0;

                while (b != 170) {
                    b = input.read() & 0xff;
                    notInSyncCount++;
                    if (notInSyncCount > Constants.MTU) {
                        return null;
                    }
                }
                if (count >= Constants.MTU) {
                    // PacketHelper too long, give up and try to resync
                    message(name + ": packet too long");
                    inSync = false;
                    count = 0;
                    continue;
                }
                if (b == 170) {
                    syncFrame[count++] = (byte) (b & 0xff);

                    while (count < Constants.FRAME_SYNC.length && notInSyncCount < Constants.MTU) {
                        b = input.read();
                        syncFrame[count++] = (byte) (b & 0xff);
                        notInSyncCount++;
                    }
                }

                if (DEBUG) {
                    Dump.dump(System.out, "Sync frame", syncFrame);
                }

                if (Utils.compare(syncFrame, Constants.FRAME_SYNC)) {
                    inSync = true;
                } else if (notInSyncCount >= Constants.MTU) {
                    return null;
                }
                count = 0;
            } else {
                byte b;
                if (!isLength) {
                    byte command = (byte) input.read();
                    receiveBuffer[count++] = command;

                    receiveBuffer[count++] = (byte) input.read();
                    receiveBuffer[count++] = (byte) input.read();
                    receiveBuffer[count++] = (byte) input.read();
                    receiveBuffer[count++] = (byte) input.read();
                    int length;
                    if (!readSDCard) {
                        length = (receiveBuffer[count - 1] & 0xff) | (receiveBuffer[count - 2] & 0xff) << 8;
                    } else {

                        length = (receiveBuffer[count - 1] & 0xff) | (receiveBuffer[count - 2] & 0xff) << 8
                                | (receiveBuffer[count - 3] & 0xff) << 16 | (receiveBuffer[count - 4] & 0xff) << 24;
                    }

                    payLoad = count + length;
                    isLength = true;
                    if (payLoad > 600) {
                        Dump.dump(System.out, "Exceeded payload length", receiveBuffer);
                    }
                    System.out.println("Payload length :" + payLoad);
                    continue;
                } else if (count < payLoad) {
                    b = (byte) (input.read() & 0xff);
                } else {
                    byte[] packet = new byte[count - 2];
                    System.arraycopy(receiveBuffer, 0, packet, 0, count - 2);

                    int readCrc = (receiveBuffer[count - 1] & 0xff)
                            | (receiveBuffer[count - 2] & 0xff) << 8;
                    int computedCrc = Crc.calc(packet, packet.length);

                    if (DEBUG) {
                        Dump.printPacket(System.err, packet);
                        System.err.println(" rcrc: " + Integer.toHexString(readCrc)
                                + " ccrc: " + Integer.toHexString(computedCrc));
                    }

                    //if (readCrc != computedCrc) {
                    return packet;
                    /* } else {
                     message(name + ": bad packet");
                     /*
                     * We don't lose sync here. If we did, garbage on the line at startup
                     * will cause loss of the first packet.
                     *
                     count = 0;
                     inSync = false;
                     continue;
                     }*/
                }
                receiveBuffer[count++] = b;
            }
        }
    }
}
