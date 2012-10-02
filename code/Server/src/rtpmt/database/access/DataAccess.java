/**
 * 
 */
package rtpmt.database.access;


import java.sql.*;
import java.util.ArrayList;
import rtpmt.database.common.ConnectionType;
import rtpmt.database.common.DataBase;

/**
 * @author Minh, Kumar, Jean
 *
 */
public class DataAccess {
        
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;    
    
    public int InsertSensorData(int id){
        DataBase db = new DataBase(ConnectionType.MYSQL);
        int result = 0; 
        
        try {
            Connection conn = db.getConnection();
            //to-do
            String query = "";
            statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            result = statement.executeUpdate();
            
            statement.close();
            conn.close();
        }
        catch(SQLException e) {
            System.err.println("SQL Error(s) as follows:");
            while (e != null) {
                System.err.println("SQL Return Code: " + e.getSQLState());
                System.err.println("  Error Message: " + e.getMessage());
                System.err.println(" Vendor Message: " + e.getErrorCode());
                e = e.getNextException();
            }	
        } 
        catch(Exception e) {
            System.err.println(e);
        }  
        
        return result;
                   
    }

    
    
}
