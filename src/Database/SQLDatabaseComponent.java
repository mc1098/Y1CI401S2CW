package Database;

import java.sql.SQLException;

/**
 * Interface for SQL Database Components. Implementations of this 
 * interface can then define vendor specific code without the need of callers
 * required to know. 
 * SQL queries given to this interface should all be vendor independent in 
 * order to avoid exceptions.
 * 
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface SQLDatabaseComponent
{
    /**
     * Returns the template object, as defined by the RsToObject type parameter,
     * from an SQL string query.
     * 
     * The SQL Query is processed by the SQL database returning 
     * the resultSet which is then passed to the defined {@link RsToObject}.
     * @param <T> The expected object to be initialised by the given RsToObject
     * parameter.
     * @param query String SQL query, which should be vendor independent.
     * @param rto implementation of interface used to initialise the template 
     * parameter.
     * @return Retrieves the template object, as defined by the RsToObject type 
     * parameter, from an SQL string query.
     * @throws SQLException Thrown if a malformed query was passed in or if there 
     * was an error connecting to the database.
     */
    public <T> T query(String query, RsToObject<T> rto) throws SQLException;
    
    /**
     * Updates SQL tables by using the string prepared statement string, then 
     * applying the Obj parameters values to the prepared statement using the 
     * ObjectToPs defined object.
     * @param <T> Object type to be updated to the database.
     * @param prepared String prepared statement.
     * @param otr implementation that takes the prepared statement and fills it
     * with the objects data.
     * @param obj Object to be updated to the database.
     * @throws SQLException 
     */
    public <T> void update(String prepared, ObjectToPs<T> otr, T obj) throws SQLException;
}
