package Database;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class ConnectionException extends Exception
{

    public ConnectionException(String string)
    {
        super(string);
    }

    public ConnectionException(String string, Throwable thrwbl)
    {
        super(string, thrwbl);
    }

    public ConnectionException(Throwable thrwbl)
    {
        super(thrwbl);
    }
    
}
