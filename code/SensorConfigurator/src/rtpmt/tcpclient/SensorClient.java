/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.tcpclient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import rtpmt.network.packet.NetworkMessage;
/**
 *
 * @author Kumar
 */
public final class SensorClient {
    
    private final String host;
    private final int port;
    private Socket mySocket;
    private OutputStream os;


    public SensorClient(String host, int port) throws Exception {
        this.host = host;
        this.port = port;
        connect();
   }
    
    public void connect() throws Exception{
        mySocket = new Socket(this.host,this.port);
        os = mySocket.getOutputStream();   
    }
    
    public void send(NetworkMessage.PackageInformation msg) throws IOException{
          msg.writeTo(os);
    }
}
