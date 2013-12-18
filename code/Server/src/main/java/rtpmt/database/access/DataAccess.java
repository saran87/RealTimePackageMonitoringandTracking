/**
 * 
 */
package rtpmt.database.access;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import org.bson.BSONObject;


/**
 * @author Saravana Kumar
 *
 */
public class DataAccess implements DBObject {
        
    protected static MongoClient connection = null ;  
    protected static DB db = null;
    private  BasicDBObject doc = new BasicDBObject();
    static {
        try{
            connection =  new MongoClient(DBConstants.DB_ADDRESS) ;
            db = connection.getDB(DBConstants.DB_NAME);
        }
        catch(UnknownHostException ex){
           System.out.println(ex.getMessage());
        }
    }

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
