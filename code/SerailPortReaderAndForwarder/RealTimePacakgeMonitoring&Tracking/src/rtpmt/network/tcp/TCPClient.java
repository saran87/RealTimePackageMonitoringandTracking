/**
 * TCP Client 
 * and open the template in the editor.
 */
package rtpmt.network.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 *
 * @author kumar
 * @author Rochester Institute of Technology
 */
public class TCPClient {
    
    private Socket clientSocket;
    DataOutputStream outPutStream;
    BufferedReader inputStream ;
    
    
    public void connect(String hostName,int portNumber)throws IOException,UnknownHostException{
        clientSocket = new Socket(hostName,portNumber);
        outPutStream = new DataOutputStream(clientSocket.getOutputStream());
        inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    
    public void disconnect()
    {
        try{
            inputStream.close();
            outPutStream.close();
            clientSocket.close();
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    public boolean sendData(String data)
    {
        try
        {
            outPutStream.writeBytes(data);
            return true;
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public String readData(){
        
        StringBuilder dataArray = new StringBuilder();
        
        try
        {
            while(inputStream.ready())
                dataArray.append(inputStream.readLine());
        }
        catch(IOException ex){
            return dataArray.toString();
        }
        
        return dataArray.toString();
    }
}
