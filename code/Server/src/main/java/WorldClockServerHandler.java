/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.example.worldclock.WorldClockProtocol.Continent;
import io.netty.example.worldclock.WorldClockProtocol.DayOfWeek;
import io.netty.example.worldclock.WorldClockProtocol.LocalTime;
import io.netty.example.worldclock.WorldClockProtocol.LocalTimes;
import io.netty.example.worldclock.WorldClockProtocol.Location;
import io.netty.example.worldclock.WorldClockProtocol.Locations;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Calendar.*;
import rtpmt.network.packet.NetworkMessage.PackageInformation;

public class WorldClockServerHandler extends SimpleChannelInboundHandler<PackageInformation> {

    private static final Logger logger = Logger.getLogger(
            WorldClockServerHandler.class.getName());

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

    private static String toString(Continent c) {
        return c.name().charAt(0) + c.name().toLowerCase().substring(1);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PackageInformation msg) throws Exception {
        
            System.out.println(   msg.getSensorId());
    
    }
    
    
}
