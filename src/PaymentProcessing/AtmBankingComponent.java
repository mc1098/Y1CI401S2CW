package PaymentProcessing;

import FinancialMessaging.TransactionsRequest;
import FinancialMessaging.DepositRequest;
import FinancialMessaging.WithdrawalRequest;
import FinancialMessaging.BalanceRequest;

/**
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class AtmBankingComponent implements BankingComponent
{
    
    private final PaymentProcessor in;
    private final String location;
    
    public AtmBankingComponent(PaymentProcessor in, String location)
    {
        this.in = in;
        this.location = location;
    }

    @Override
    public void balance(int accNum, long cardType, int accPass)
    {
        in.accept(new BalanceRequest(accNum, cardType, accPass));
    }

    @Override
    public void transactions(int accNum, long cardType, int accPass)
    {
        in.accept(new TransactionsRequest(accNum, cardType, accPass));
    }

    @Override
    public void withdraw(int accNum, long cardType, int accPass, int amount)
    {
        in.accept(new WithdrawalRequest(accNum, cardType, accPass, location, amount));
    }

    @Override
    public void deposit(int accNum, long cardType, int accPass, int amount)
    {
        in.accept(new DepositRequest(accNum, cardType, accPass, location, amount));
    }
    
}
