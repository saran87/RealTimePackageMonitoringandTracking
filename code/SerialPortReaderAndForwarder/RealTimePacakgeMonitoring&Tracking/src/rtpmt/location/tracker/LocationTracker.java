/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.location.tracker;
import gnu.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 *
 * @author kumar
 */
public class LocationTracker implements SerialPortEventListener, Runnable{
    
    //location stack to store the location collected over time
    static LinkedList<Location>  locStack = new LinkedList();
    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;
    //port name
    final static String portName = "COM3";
    //this is the input stream of the opened port
    private static BufferedReader streamReader = null;
   
    public static boolean startTracking(){
        
        boolean isStarted = false;
        try{
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            if ( portIdentifier.isCurrentlyOwned() )
            {
                System.out.println("Error: Port is currently in use");
            }
            else
            {
                CommPort commPort = portIdentifier.open("Location Tracking",TIMEOUT);

                if ( commPort instanceof SerialPort )
                {
                    SerialPort serialPort = (SerialPort) commPort;
                    serialPort.setSerialPortParams(4800,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

                    streamReader = new BufferedReader( new InputStreamReader(serialPort.getInputStream()));
                    isStarted = true;
                    serialPort.addEventListener(new LocationTracker());
                    serialPort.notifyOnDataAvailable(true);
                }
                else
                {
                    System.out.println("Error: No serial port available.");
                }
            }
        }
        catch (PortInUseException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        } 
        return isStarted;
        
    }
    
    //Get the latest location
    public static Location getLocation(){
        
        Location loc = null;
        
        if(!locStack.isEmpty()){
            
            if(locStack.size()>1){
                
                loc = locStack.removeLast();
            }
            else{
                loc = locStack.peekLast();
            }            
        }
        
        return loc;
    }
    @Override
    public void serialEvent(SerialPortEvent spe) {
       
        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {
            try
            {
                //String dummydata = "#:367:N:43.1049:W:77.6233:ZBBBB00000849";
                if(streamReader.ready()){
                    String line = streamReader.readLine();
                    while(line != null){
                       System.out.println("tracking");
                        //System.out.println(str);

                        if(line.contains("GPGGA")){

                                    String[] linearr = line.split(",");
                                    if(linearr.length>4){
                                        Location location = new Location();

                                        //m_latitude
                                        double dlat = Double.parseDouble(linearr[2]);
                                        dlat = dlat / 100;
                                        double fraction_dlat = dlat - (int)dlat;
                                        fraction_dlat = fraction_dlat * 100 / 60;
                                    // fraction_dlat = Mathfraction_dlat);
                                        double real_dlat = (int)dlat + fraction_dlat;
                                        String dlat_str = String.format("{0:0.0000}", real_dlat);
                                    //m_latitude = linearr[3].toString() + ":" + dlat_str;
                                        
                                        if(linearr[3].equalsIgnoreCase("N")){
                                            
                                            if(real_dlat<0){
                                                real_dlat = real_dlat * -1;
                                            }  
                                        }
                                        else{
                                            
                                            if(real_dlat>0){
                                                real_dlat = real_dlat * -1;
                                            } 
                                        }
                                           
                                        
                                        location.setLatitude(real_dlat);
                                        //m_longitude
                                        double dlon = Double.parseDouble(linearr[4]);
                                        dlon = dlon / 100;
                                        double fraction_dlon = dlon - (int)dlon;
                                        fraction_dlon = fraction_dlon * 100 / 60;
                                        //fraction_dlon = Math.round(fraction_dlon);
                                        double real_dlon = (int)dlon + fraction_dlon;
                                        String dlon_str = String.format("{0:0.0000}", real_dlon);
                                        if(linearr[5].equalsIgnoreCase("E")){
                                            
                                            if(real_dlon<0){
                                                real_dlon = real_dlon * -1;
                                            }  
                                        }
                                        else{
                                            
                                            if(real_dlon>0){
                                                real_dlon = real_dlon * -1;
                                            } 
                                        }
                                        location.setLongitude(real_dlon);
                                        //m_longitude = linearr[5].toString() + ":" + dlon_str.ToString();
                                    //m_position = ":" + m_m_gps_count_str + ":" + m_latitude + ":" + m_longitude + ":";
                                        locStack.addLast(location);
                                    }
                        }
                        if(streamReader.ready())
                        line = streamReader.readLine();
                        else
                            break;
                    } 
                }
            }
            catch (Exception ex)
            {
              System.out.println("My Error" + ex.getMessage());
            }
        }
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
