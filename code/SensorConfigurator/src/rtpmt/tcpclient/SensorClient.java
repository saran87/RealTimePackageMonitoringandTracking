/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.tcpclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
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
    private Channel ch;
     EventLoopGroup group;
       Bootstrap b;
       ClientHandler handler;
    private boolean isServerAvaialable = false;

    public SensorClient(String host, int port) throws Exception {
        this.host = host;
        this.port = port;
   }
    
    public void connect() throws Exception{
            /*
            mySocket = new Socket(this.host,this.port);
            os = mySocket.getOutputStream();
             */
        group = new NioEventLoopGroup();
        b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer());
        ch = b.connect(host, port).sync().channel();
        handler =
                ch.pipeline().get(ClientHandler.class);
        isServerAvaialable = true;
    }
    
    public void send(NetworkMessage.PackageInformation msg) throws IOException{
       if(isServerAvaialable)
           handler.send(msg);
       
    }
}
