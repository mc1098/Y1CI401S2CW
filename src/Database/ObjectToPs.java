package Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This interface is used with the {@link SQLDatabaseComponent} in order to 
 * map an object data to a {@link PreparedStatement}.
 * 
 * @see SQLDatabaseComponent
 * @see PreparedStatement
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface ObjectToPs<T>
{
    public void mapToPs(PreparedStatement ps, T t) throws SQLException;
}
