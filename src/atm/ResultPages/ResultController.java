package atm.ResultPages;

import ATMFrame.ATM;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class ResultController
{
    private final ATM atm;
    
    public ResultController(ATM atm)
    {
        this.atm = atm;
    }
    
    public void onEnter(boolean result)
    {
        atm.loadFirstScreen();
    }
    
    public void onCancel() 
    {
        atm.idleAd();
    }
    
    public void onClear() 
    {
        atm.idleAd();
    }
    
    
}
