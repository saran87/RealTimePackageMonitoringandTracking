/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.database.common;
import java.sql.Connection;
/**
 *
 * @author Saravanakumar
 */
public class DataBase {
    
    private Connection connection = null;
    
    public DataBase(ConnectionType connectionType){
        
        switch(connectionType){
            
            case MYSQL:connection = new MySQLDataAccess().GetConnection();break;        
        } 
     }
    
    public Connection getConnection(){
        return connection;
    }
    
}
