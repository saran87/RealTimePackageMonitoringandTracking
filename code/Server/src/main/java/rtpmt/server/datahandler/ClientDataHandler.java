/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.server.datahandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import rtpmt.network.packet.NetworkMessage.PackageInformation;
import java.util.logging.Level;
import java.util.logging.Logger;
import rtpmt.database.access.IDataStore;
import rtpmt.models.DataStore;
/**
 *
 * @author Kumar
 */
public class ClientDataHandler extends SimpleChannelInboundHandler<PackageInformation>{
      private static final Logger logger = Logger.getLogger(
            ClientDataHandler.class.getName());

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Connected to :" + ctx.channel().remoteAddress());
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.", cause);
        ctx.close();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PackageInformation msg) throws Exception {
        try{
            System.out.println(   msg.getSensorId());
            IDataStore dataStore = DataStore.getDataObject(msg);
            dataStore.save();
        }catch(Exception ex){
             logger.log(
                Level.SEVERE,
                "Unexpected exception from downstream.", ex);
        }
    }
    
}
