/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.tcpclient;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import rtpmt.network.packet.NetworkMessage.PackageInformation;
/**
 *
 * @author Kumar
 */
class ClientHandler extends SimpleChannelInboundHandler<PackageInformation> {

    private static final Logger logger = Logger.getLogger(
            ClientHandler.class.getName());

    private static final Pattern DELIM = Pattern.compile("/");
    // Stateful properties
    private volatile Channel channel;

    public ClientHandler() {
        super(false);
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
        System.out.println("hello");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.", cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext chc, PackageInformation msg) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean send(PackageInformation msg) {
        ChannelFuture write = channel.writeAndFlush(msg);
        return write.isSuccess();
    }
}
