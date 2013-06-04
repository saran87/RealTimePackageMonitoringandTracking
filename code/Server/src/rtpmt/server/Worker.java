/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import rtpmt.database.access.DataAccess;
import rtpmt.network.packet.SensorMessage;
/**
 *
 * @author Saravanakumar
 */
public class Worker implements Runnable {

    private Socket server;
    private DataAccess dataAccess;
    Worker(Socket server) {
        this.server=server;
    }

    @Override
    public void run () {

        try {
            // Get input from the client
            DataInputStream in = new DataInputStream (server.getInputStream());
          
            dataAccess = new DataAccess();
            
            for(;;){
               if(in.available()>0){
                 
                SensorMessage.SensorInformation message = SensorMessage.SensorInformation.parseDelimitedFrom(in);
                //end message indentifier
                  if("-1".equals(message.getDeviceId())) {
                        break;
                   } 
                   dataAccess.InsertPackageData(message);
                   System.out.println(message.getSensorsCount());
                  
                }
               else{
                   //sleep for 1000 miliseconds if no data in input stream
                   Thread.sleep(1000);
               }
            }
            server.close();
        } 
        catch (IOException ioe) {
            System.out.println("IOException on socket listen: " + ioe);
            ioe.printStackTrace();
        }
         catch (InterruptedException ie) {
            System.out.println("IOException on socket listen: " + ie);
            ie.printStackTrace();
        }
    }
}
