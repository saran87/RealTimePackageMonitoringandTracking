/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.packages;

import rtpmt.sensor.util.Packet;

/**
 *
 * @author Kumar
 */
public interface SensorEventHandler {
    
   void newSensorAdded(Package newPackage);
   void handleNewPacket(Packet packet);
}
