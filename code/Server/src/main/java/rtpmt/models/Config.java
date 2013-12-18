/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteConcern;
import java.util.Iterator;
import rtpmt.database.access.DBConstants;
import rtpmt.database.access.IDataStore;
import rtpmt.network.packet.NetworkMessage;
import rtpmt.network.packet.NetworkMessage.PackageInformation;
import rtpmt.network.packet.NetworkMessage.PackageInformation.MessageType;

/**
 *
 * @author Kumar
 */
public class Config extends BaseInfo implements IDataStore {

    public Config(NetworkMessage.PackageInformation packageInformation) {
        super(packageInformation);
        populateConfigObject(packageInformation);
    }

    private void populateConfigObject(NetworkMessage.PackageInformation packageInformation) {
        if (packageInformation.getMessageType() == MessageType.CONFIG) {
            if (packageInformation.getConfigsCount() > 0) {
                BasicDBObject configObject = new BasicDBObject();
                for (PackageInformation.Config config : packageInformation.getConfigsList()) {
                    BasicDBObject dbObject = new BasicDBObject();
                    dbObject.append(DBConstants.MAX_THRESHOLD, config.getMaxThreshold());
                    dbObject.append(DBConstants.MAX_THRESHOLD, config.getMinThreshold());
                    dbObject.append(DBConstants.TIME_PERIOD, config.getTimePeriod());
                    dbObject.append(DBConstants.TIME_PERIOD_AFTER_THRESHOLD, config.getTimePeriodAfterThreshold());

                    configObject.append(config.getSensorType().toString().toLowerCase(), dbObject);
                }

                this.put(DBConstants.CONFIG, configObject);
            }
        }
    }

    public Object get() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void save() {
        DBCollection packageColl = db.getCollection(DBConstants.CONFIG_COLLECTION);
        
        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.SENSOR_ID, this.get(DBConstants.SENSOR_ID));
        query.put(DBConstants.TRUCK_ID, this.get(DBConstants.TRUCK_ID));
        query.put(DBConstants.PACKAGE_ID,this.get(DBConstants.PACKAGE_ID));
        query.put(DBConstants.TIMESTAMP, this.get(DBConstants.TIMESTAMP));
    
        packageColl.update(query,this,true,false, WriteConcern.FSYNCED);
    }

}
