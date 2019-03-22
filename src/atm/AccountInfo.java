package atm;


/**
 * A POJO for mutable changes to account information provided at login. 
 * This information is provided at login and stored while the user chooses 
 * the service, it is then passed on to that service handler.
 * 
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class AccountInfo
{
    public int accNum;
    public int accPass;
    public long cardTypeId;
}
