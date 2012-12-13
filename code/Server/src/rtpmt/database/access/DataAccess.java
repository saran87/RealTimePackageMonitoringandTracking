/**
 * 
 */
package rtpmt.database.access;


import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Iterator;
import rtpmt.network.packet.SensorMessage.SensorInformation;


/**
 * @author Saravana Kumar
 *
 */
public class DataAccess  {
        
    private static Mongo connection = null ;  
    private static DB db = null;
    static {
        try{
            connection =  new Mongo("54.243.241.254") ;
            db = connection.getDB("RFID");
        }
        catch(UnknownHostException ex){
           System.out.println(ex.getMessage());
        }
    }
    /*
     * 
     */
    public int InsertPackageData(SensorInformation message){
       
 
        Package pack = getPackage(message);
        
        DBCollection packageColl = db.getCollection("Packages");
        WriteResult result =  packageColl.insert(pack);
       
        return result.getN();
                 
    }
    /*
     * 
     */
     private Package getPackage(SensorInformation message){
        
        Package packag = new Package();
        packag.put("packageId", message.getDeviceId());
        //to-do
        packag.put("truckId", message.getDeviceId());
       
        packag.put("timestamp",new Date(message.getTimeStamp()).getTime());
        for (Iterator<SensorInformation.Sensor> it = message.getSensorsList().iterator(); it.hasNext();) {
            SensorInformation.Sensor sensor = it.next();
            
            rtpmt.database.access.Sensor sen = new Sensor();
            sen.put("sensorId", sensor.getSensorId());
            sen.put("value",sensor.getSensorValue());

            packag.put(sensor.getSensorType().toString().toLowerCase(), sen);
        }
      
        Location loc = new Location();
            loc.put("latitude",message.getLocation().getLatitude());
            loc.put("longitude",message.getLocation().getLongitude());
        packag.put("location",loc);
        // packag.put("vibration", sensor2);
        return packag;
    }
}
