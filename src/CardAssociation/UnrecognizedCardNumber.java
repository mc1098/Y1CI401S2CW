package CardAssociation;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class UnrecognizedCardNumber extends Exception
{

    public UnrecognizedCardNumber(String string)
    {
        super(string);
    }

    public UnrecognizedCardNumber(String string, Throwable thrwbl)
    {
        super(string, thrwbl);
    }

    public UnrecognizedCardNumber(Throwable thrwbl)
    {
        super(thrwbl);
    }
    
}
