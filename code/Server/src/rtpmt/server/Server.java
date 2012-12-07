/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author Saravanakumar
 */
public class Server {
  
    private static int port=3000, maxConnections=10;
    
    // Listen for incoming connections and handle them
    public static void main(String[] args) {
        int workerCount=0;

        try{
                ServerSocket listener = new ServerSocket(port);
                Socket server;
                System.out.println("Server started");
                while((workerCount++ < maxConnections) || (maxConnections == 0)){
                    server = listener.accept();
                    System.out.println("Connection" + workerCount + "accepted");
                    Worker conn_c= new Worker(server);
                    Thread t = new Thread(conn_c);
                    t.start();
                }
            }
        catch (IOException ioe) {
            System.out.println("IOException on socket listen: " + ioe);
            ioe.printStackTrace();   
        }
    }
}
