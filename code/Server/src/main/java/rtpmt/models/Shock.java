/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import rtpmt.database.access.DBConstants;
import rtpmt.database.access.IDataStore;
import rtpmt.network.packet.NetworkMessage;

/**
 *
 * @author Kumar
 */
public class Shock extends BaseInfo implements IDataStore {
    /**
     *
     * @param packageInformation
     */
    public Shock(NetworkMessage.PackageInformation packageInformation) {
        super(packageInformation);
        populateShockObject(packageInformation);
    }

    private void populateShockObject(NetworkMessage.PackageInformation packageInformation) {
        if (packageInformation.getMessageType() == NetworkMessage.PackageInformation.MessageType.SENSOR_INFO) {
            if (packageInformation.getSensorsCount()> 0) {
                for (NetworkMessage.PackageInformation.Sensor sensor : packageInformation.getSensorsList()) {
                    char charAt = sensor.getSensorType().toString().charAt(sensor.getSensorType().toString().length()-1);
                    this.put(DBConstants.VALUE + DBConstants.DOT + Character.toString(charAt).toLowerCase(),sensor.getSensorValue());
                } 
            }
        }
    }

    public Object get() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void save() {
        DBCollection packageColl = db.getCollection(DBConstants.SHOCK_COLLECTION);
        
        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.SENSOR_ID, this.get(DBConstants.SENSOR_ID));
        query.put(DBConstants.TRUCK_ID, this.get(DBConstants.TRUCK_ID));
        query.put(DBConstants.PACKAGE_ID,this.get(DBConstants.PACKAGE_ID));
        query.put(DBConstants.TIMESTAMP, this.get(DBConstants.TIMESTAMP));
        
        BasicDBObject set = new BasicDBObject();
        set.put("$set", this);
        packageColl.update(query, set, true, false);
        //packageColl.insert(this, WriteConcern.ACKNOWLEDGED);
    }
}