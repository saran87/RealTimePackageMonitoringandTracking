/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.models;

import com.mongodb.BasicDBList;
import rtpmt.database.access.DBConstants;
import rtpmt.database.access.DataAccess;
import rtpmt.network.packet.NetworkMessage;
import rtpmt.network.packet.NetworkMessage.PackageInformation.LocationInformation;

/**
 *
 * @author Kumar
 */
public abstract class BaseInfo extends DataAccess {

    public BaseInfo(NetworkMessage.PackageInformation packageInformation) {

        populateBaseInfo(packageInformation);
    }

    private void populateBaseInfo(NetworkMessage.PackageInformation packageInformation) {
        this.put(DBConstants.SENSOR_ID, packageInformation.getSensorId());
        this.put(DBConstants.TRUCK_ID, packageInformation.getTruckId());
        this.put(DBConstants.PACKAGE_ID, packageInformation.getPackageId());
        this.put(DBConstants.TIMESTAMP, packageInformation.getTimeStamp());
        if (packageInformation.hasIsAboveThreshold()){
            this.put(DBConstants.IS_ABOVE_THRESHOLD, packageInformation.getIsAboveThreshold());
        }
        if (packageInformation.hasComments()) {
            this.put(DBConstants.COMMENTS, packageInformation.getComments());
        }
        if(packageInformation.hasIsInstantaneous()){
            this.put(DBConstants.IS_INSTANTANEOUS, packageInformation.getIsInstantaneous());
        }
      
        if (packageInformation.hasLocation()) {
            BasicDBList loc = new BasicDBList();
            LocationInformation location = packageInformation.getLocation();
            loc.add(location.getLatitude());
            loc.add(location.getLongitude());
            this.put(DBConstants.LOCATION, loc);
        }
    }
}
