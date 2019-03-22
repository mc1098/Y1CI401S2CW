
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Derby specific implementation of the SQLDatabaseComponent.
 * 
 * Derby is an embedded file based SQL database.
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class DerbySQLDatabase implements SQLDatabaseComponent
{
    private final static Logger LOGGER = Logger.getLogger(DerbySQLDatabase.class.getName());
    
    private final static String URL = "jdbc:derby://localhost:1527/uni_mc1098_atm";
    //obviously a very insecure way of using the USER and PASS parameter for the database.
    //However this implementation is very simple example.
    private final static String USER = "atm";
    private final static String PASS = "atm";
    

    @Override
    public <T> T query(String query, RsToObject<T> rto) throws SQLException
    {
        //try-with-resource so that the close method is called automatically 
        //if an exception is thrown.
        try(Connection conn = DriverManager.getConnection(URL, USER, PASS); 
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 
                        ResultSet.CONCUR_READ_ONLY); 
                ResultSet rs = stmt.executeQuery(query))
        {
            //RsToObject for customised object initialisation while keeping 
            //ResultSet within the try-with-resource scope.
            return rto.initObject(rs);
        } catch(SQLException ex)
        {
            LOGGER.log(Level.WARNING, null, ex);
            throw ex;
        }
    }

    @Override
    public <T> void update(String prepared, ObjectToPs<T> otr, T obj) throws SQLException
    {
        try(Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = conn.prepareStatement(prepared);)
        {
            otr.mapToPs(ps, obj);
        } catch(SQLException ex)
        {
            LOGGER.log(Level.WARNING, null, ex);
            throw ex;
        }
    }
    
}
