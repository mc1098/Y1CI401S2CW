package Database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This interface is used with the {@link SQLDatabaseComponent} in order to 
 * initialise Objects from the a {@link ResultSet}. 
 * 
 * @see SQLDatabaseComponent
 * @see ResultSet
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface RsToObject<T>
{
    /**
     * Returns an object of the type parameter T from the given {@link ResultSet}.
     * 
     * Implementations may provide different default results on an empty ResultSet
     * however users of this interface should be aware that a null return type 
     * is possible.
     * 
     * @param <T> Type of object to be returned.
     * @param rs ResultSet to initialise the object from.
     * @return Retrieve an object of the type parameter T from the given 
     * {@link ResultSet}.
     * @throws SQLException thrown if the connection to the database is 
     * interrupted, or a call to the ResultSet causes an SQLException.
     * 
     * @see ResultSet
     * @see SQLException
     */
    public <T> T initObject(ResultSet rs) throws SQLException;
}
