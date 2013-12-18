/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.models;

import com.mongodb.DBCollection;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import rtpmt.database.access.DBConstants;
import rtpmt.database.access.IDataStore;
import rtpmt.network.packet.NetworkMessage;

/**
 *
 * @author Kumar
 */
public class Humidity extends BaseInfo implements IDataStore {

    public Humidity(NetworkMessage.PackageInformation packageInformation) {
        super(packageInformation);
        populateHumidityObject(packageInformation);
    }

    private void populateHumidityObject(NetworkMessage.PackageInformation packageInformation) {
        if (packageInformation.getMessageType() == NetworkMessage.PackageInformation.MessageType.SENSOR_INFO) {
            if (packageInformation.getSensorsCount()> 0) {
                for (NetworkMessage.PackageInformation.Sensor sensor : packageInformation.getSensorsList()) {
                    this.put(DBConstants.UNIT, sensor.getSensorUnit());
                    this.put(DBConstants.VALUE, sensor.getSensorValue());
                }               
            }
        }
    }

    public Object get() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void save() {
        DBCollection packageColl = db.getCollection(DBConstants.HUMIDITY_COLLECTION);
        WriteResult insert = packageColl.insert(this, WriteConcern.ACKNOWLEDGED);
    }
}
