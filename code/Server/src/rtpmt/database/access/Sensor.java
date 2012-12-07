/*
 * Sensor - to store the sensor details
 */
package rtpmt.database.access;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.Map;
import java.util.Set;
import org.bson.BSONObject;

/**
 *
 * @author kumar
 */
public class Sensor implements DBObject{  

private  BasicDBObject doc = new BasicDBObject();

    @Override
    public boolean isPartialObject(){
       return doc.isPartialObject();
    }
    
    @Override
    public void markAsPartialObject(){
        doc.markAsPartialObject();
    }

    @Override
    public Object put(String string, Object o) {
       return doc.put(string, o);
    }

    @Override
    public void putAll(BSONObject bsono) {
        
        doc.putAll(bsono);
    }

    @Override
    public void putAll(Map map) {
       doc.putAll(map);
    }

    @Override
    public Object get(String string) {
       return doc.get(string);
    }

    @Override
    public Map toMap() {
       return doc.toMap();
    }

    @Override
    public Object removeField(String string) {
        return doc.removeField(string);
    }

    @Override
    public boolean containsKey(String string) {
        return doc.get(string) !=null ? true : false;
    }

    @Override
    public boolean containsField(String string) {
      return doc.containsField(string);
    }

    @Override
    public Set<String> keySet() {
        return doc.keySet();
    }
    
    
}