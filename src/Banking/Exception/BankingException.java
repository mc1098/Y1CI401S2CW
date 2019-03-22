package Banking.Exception;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class BankingException extends Exception
{

    public BankingException(String string)
    {
        super(string);
    }

    public BankingException(String string, Throwable thrwbl)
    {
        super(string, thrwbl);
    }

    public BankingException(Throwable thrwbl)
    {
        super(thrwbl);
    }
    
}
