package Banking.Exception;

import Banking.Exception.BankingException;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class InsufficientFunds extends BankingException
{

    public InsufficientFunds(String message)
    {
        super(message);
    }
    
}
