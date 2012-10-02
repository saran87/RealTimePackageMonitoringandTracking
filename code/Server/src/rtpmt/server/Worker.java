/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.server;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Saravanakumar
 */
public class Worker implements Runnable {

    private Socket server;
    private String line;

    Worker(Socket server) {
        this.server=server;
    }

    public void run () {

        try {
            // Get input from the client
            DataInputStream in = new DataInputStream (server.getInputStream());
            BufferedReader receiveRead = new BufferedReader(new InputStreamReader(in));

            List<String> Values = new ArrayList<String>();
            
            while((line = receiveRead.readLine()) != null && !line.equals("quit")) {
          
                boolean  b = line.startsWith("Connection");

                if(b==true){
                    System.out.println(line);
                    continue;
                }
                
                String splitValues[] = line.split(",");
                
                for(int i=0;i<splitValues.length;i++){
                    Values.add(splitValues[i]); 
                }
   
                System.out.println(Values);
                Values.clear();
            }     
            server.close();
        } 
        catch (IOException ioe) {
            System.out.println("IOException on socket listen: " + ioe);
            ioe.printStackTrace();
        }
    }
}
