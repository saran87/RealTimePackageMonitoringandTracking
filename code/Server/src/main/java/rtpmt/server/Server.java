/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 * @author Kumar
 */
public class Server {
    
    private final int port;
    
    public  Server(int _port){
        this.port = _port;
    }
    
    //Creates required Event Loops and starts the server
    public void run() throws Exception{
        //boss Group is to accept connections
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //worker group is to process data from each connection
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try{
            /*
            ServerBootstrap bootStrap = new ServerBootstrap();
            bootStrap.group(bossGroup, workerGroup)
                     .channel(NioServerSocketChannel.class)
                     .childHandler(new ServerInitializer())
                     .option(ChannelOption.SO_BACKLOG,128)
                     .childOption(ChannelOption.SO_KEEPALIVE,true);
             bootStrap.bind(port).sync().channel().closeFuture().sync();*/
             // Bind and start to accept incoming connections.
           // ChannelFuture f = bootStrap.bind(port).sync(); // (7)
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            //ChannelFuture sync = f.channel().closeFuture().sync();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ServerInitializer());

            b.bind(port).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
                     
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        
        
    }
}
