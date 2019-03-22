package atm.Choice;

import ATMFrame.ATM;
import ATMFrame.Screenable;
import PaymentProcessing.BankingComponent;
import atm.AccountInfo;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class ServiceChoiceController implements ChoiceController
{
    private final ATM atm;
    private final AccountInfo model;
    private final BankingComponent component;
    private final String[] options;
    
    private final Screenable withdrawPage;
    private final Screenable depositPage;
    
    public ServiceChoiceController(ATM atm, AccountInfo model, 
            BankingComponent component, Screenable withdrawPage, 
            Screenable depositPage)
    {
        this.atm = atm;
        this.model = model;
        this.component = component;
        this.options = new String[]{"Balance", "Withdraw", "", "Transactions", 
            "Deposit", ""};
        this.withdrawPage = withdrawPage;
        this.depositPage = depositPage;
    }
    
    @Override
    public String getMenuOption(int n) 
    {
        if(n >= 0 && n < options.length)
            return options[n];
        return "";
    }

    @Override
    public void onOption1()
    {
        atm.processingAd();
        component.balance(model.accNum, model.cardTypeId, model.accPass);
    }

    @Override
    public void onOption2()
    {
        atm.loadToScreen(withdrawPage);
    }

    @Override
    public void onOption3()
    {
        //no option here so don't do anything.
    }

    @Override
    public void onOption4()
    {
        atm.processingAd();
        component.transactions(model.accNum, model.cardTypeId, model.accPass);
    }

    @Override
    public void onOption5()
    {
        atm.loadToScreen(depositPage);
    }

    @Override
    public void onOption6()
    {
        //no option here so don't do anything.
    }

    @Override
    public void back()
    {
        atm.loadPreviousScreen();
    }
    
    
    
    
}
