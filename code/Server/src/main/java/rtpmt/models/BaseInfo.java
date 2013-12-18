/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.models;

import java.util.Date;
import rtpmt.database.access.DBConstants;
import rtpmt.database.access.DataAccess;
import rtpmt.network.packet.NetworkMessage;

/**
 *
 * @author Kumar
 */
public abstract class BaseInfo extends DataAccess {
    
    public BaseInfo(NetworkMessage.PackageInformation packageInformation){
        
        populateBaseInfo(packageInformation);
    }

    private void populateBaseInfo(NetworkMessage.PackageInformation packageInformation) {
       this.put(DBConstants.SENSOR_ID, packageInformation.getSensorId());
       this.put(DBConstants.TRUCK_ID, packageInformation.getTruckId());
       this.put(DBConstants.PACKAGE_ID,packageInformation.getPackageId());
       this.put(DBConstants.TIMESTAMP, packageInformation.getTimeStamp());
       if(packageInformation.hasComments()){
           this.put(DBConstants.COMMENTS, packageInformation.getComments());
       }
    }
}
