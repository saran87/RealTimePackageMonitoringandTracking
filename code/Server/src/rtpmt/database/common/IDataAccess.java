/*
 * Interface DataAccess 
 * Common methods for accessing database
 */
package rtpmt.database.common;
import java.sql.*;
/**
 *
 * @author Saravanakumar
 */
public interface IDataAccess {
    Connection GetConnection();
}
