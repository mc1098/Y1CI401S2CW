package atm.Login;

import ATMFrame.ATM;
import ATMFrame.Screenable;
import atm.AccountInfo;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class LoginController
{
    private final AccountInfo model;
    private final ATM atm;
    private final Screenable next;
    
    public LoginController(AccountInfo model, ATM atm, Screenable nextPage)
    {
        this.model = model;
        this.atm = atm;
        this.next = nextPage;
    }
    
    public void onEnter(int accNum, long cardType, int accPass)
    {
        model.accNum = accNum;
        model.accPass = accPass;
        model.cardTypeId = cardType;
        atm.loadToScreen(next);
    }
}
