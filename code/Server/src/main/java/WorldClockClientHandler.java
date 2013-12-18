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


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.example.worldclock.WorldClockProtocol.Continent;
import io.netty.example.worldclock.WorldClockProtocol.LocalTime;
import io.netty.example.worldclock.WorldClockProtocol.LocalTimes;
import io.netty.example.worldclock.WorldClockProtocol.Location;
import io.netty.example.worldclock.WorldClockProtocol.Locations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import rtpmt.network.packet.NetworkMessage.PackageInformation;

public class WorldClockClientHandler extends SimpleChannelInboundHandler<PackageInformation> {

    private static final Logger logger = Logger.getLogger(
            WorldClockClientHandler.class.getName());

    private static final Pattern DELIM = Pattern.compile("/");

    // Stateful properties
    private volatile Channel channel;
    private final BlockingQueue<LocalTimes> answer = new LinkedBlockingQueue<LocalTimes>();

    public WorldClockClientHandler() {
        super(false);
    }

    public List<String> getLocalTimes(Collection<String> cities) {
        channel.writeAndFlush(getPackageInformation());
        /*
        LocalTimes localTimes;
        boolean interrupted = false;
        for (;;) {
            try {
                localTimes = answer.take();
                break;
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }

        if (interrupted) {
            Thread.currentThread().interrupt();
        }*/

        List<String> result = new ArrayList<String>();
        /*
        for (LocalTime lt: localTimes.getLocalTimeList()) {
            result.add(
                    new Formatter().format(
                            "%4d-%02d-%02d %02d:%02d:%02d %s",
                            lt.getYear(),
                            lt.getMonth(),
                            lt.getDayOfMonth(),
                            lt.getHour(),
                            lt.getMinute(),
                            lt.getSecond(),
                            lt.getDayOfWeek().name()).toString());
        }
        */
        return result;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Object getPackageInformation() {
        PackageInformation.Builder message = PackageInformation.newBuilder();
            //compulsary information
            message.setIsRealTime(true);
            message.setMessageType(PackageInformation.MessageType.SENSOR_INFO);
            long date =  1372707120000L;
            message.setTimeStamp(date);
            
            message.setSensorId("1");
            message.setPackageId("hello");
            message.setTruckId("Test") ;
            message.setComments("Testing the sensors");
            
            PackageInformation.Sensor.Builder sensor = PackageInformation.Sensor.newBuilder();
            /*
            sensor.setSensorUnit("F");
            sensor.setSensorType(PackageInformation.SensorType.TEMPERATURE);
            sensor.setSensorValue(String.valueOf(24));
            //sensor.setSensorValue("80");
            message.addSensors(sensor);

            sensor = PackageInformation.Sensor.newBuilder();
     
            sensor.setSensorUnit("%RH");
            sensor.setSensorType(PackageInformation.SensorType.HUMIDITY);

            sensor.setSensorValue("90");
            message.addSensors(sensor);
                    
            sensor = PackageInformation.Sensor.newBuilder();
            */     
            sensor.setSensorUnit("g");
            System.out.println("X=");
            sensor.setSensorType(PackageInformation.SensorType.VIBRATIONX);

            sensor.setSensorValue("asdasd");
            message.addSensors(sensor);
           /*
            sensor = PackageInformation.Sensor.newBuilder();

            sensor.setSensorUnit("g");
            sensor.setSensorType(PackageInformation.SensorType.SHOCKX);

            sensor.setSensorValue("asdasdas");
            message.addSensors(sensor);
*/
            PackageInformation.LocationInformation.Builder location = PackageInformation.LocationInformation.newBuilder();
            
            location.setLatitude(43.084603);
            location.setLongitude(-77.680312);
            message.setLocation(location);
            return message.build();
    }
}
