package atm.Choice;

import ATMFrame.ATM;
import PaymentProcessing.BankingComponent;
import atm.AccountInfo;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class DepositChoiceController implements AmountChoiceController
{
    private final ATM atm;
    private final AccountInfo model;
    private final BankingComponent component;
    private final String[] options;
    
    public DepositChoiceController(ATM atm, AccountInfo model, 
            BankingComponent component)
    {
        this.atm = atm;
        this.model = model;
        this.component = component;
        this.options = new String[]{"£5", "£20", "£100", "£10", "£50", "£200"};
    }

    @Override
    public void customAmount(int amount)
    {
        atm.loadFirstScreen();
        component.deposit(model.accNum, model.cardTypeId, model.accPass, 
                amount);
    }

    @Override
    public String getMenuOption(int n)
    {
        if(n >= 0 && n < options.length)
            return options[n];
        return "";
    }

    @Override
    public void onOption1() {customAmount(5);}

    @Override
    public void onOption2() {customAmount(20);}

    @Override
    public void onOption3() {customAmount(100);}

    @Override
    public void onOption4() {customAmount(10);}

    @Override
    public void onOption5() {customAmount(50);}

    @Override
    public void onOption6() {customAmount(200);}

    @Override
    public void back() {atm.loadPreviousScreen();}
    
}
