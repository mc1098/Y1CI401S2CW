package atm;

import ATMFrame.ATM;
import FinancialMessaging.BalanceResponse;
import FinancialMessaging.DepositResponse;
import FinancialMessaging.TransactionsResponse;
import FinancialMessaging.WithdrawalResponse;
import atm.ResultPages.ResultPage;
import atm.ResultPages.TransactionResultPage;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import PaymentProcessing.BankingResultHandler;

/**
 * This class implements the {@link BankingResultHandler} locally 
 * resolving the responses loading the respective {@link ResultPage}. 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class Presenter implements BankingResultHandler
{
    private final ATM atm;
    private final ResultPage resultPage;
    private final TransactionResultPage transactionResultPage;
    
    public Presenter(ATM atm, ResultPage resultPage, 
            TransactionResultPage transactionResultPage)
    {
        this.atm = atm;
        this.resultPage = resultPage;
        this.transactionResultPage = transactionResultPage;
    }

    @Override
    public void receive(BalanceResponse response)
    {
        if(response.sucess())
        {
            resultPage.update(true, String.format("Your current balance is £%d", 
                    response.amount));
            atm.loadToScreen(resultPage);
        }
        else 
        {
            //request failed - So check error flags and report the error to the user.
            String failure = "";
            if(response.invalidAccountInfo)
                failure = "Unable to retrieve the balance due to the incorrect account information given.";
            else if(response.unableToGetBankingInfo)
                failure = "Unable to connect to the network\n Our apologies for the inconvenience.";
            
            resultPage.update(false, failure);
            atm.loadToScreen(resultPage);
        }
    }

    @Override
    public void receive(TransactionsResponse response)
    {
        
        boolean result = response.sucess();
        //String[] for the table columns in the TransactionResultPage.
        ObservableList<String[]> list = FXCollections.observableArrayList();
        
        if(result)
        {
            //load transactions to observableList
            for (TransactionsResponse.TransactionDO transaction : response.transactions)
            {
                String[] tInfo = new String[5];
                tInfo[0] = transaction.occurred.format(DateTimeFormatter
                        .ofPattern("dd MMM"));
                tInfo[1] = transaction.location;
                tInfo[2] = String.format("£%d", transaction.isDebit ? transaction.amount : 0);
                tInfo[3] = String.format("£%d", transaction.isDebit ? 0 : transaction.amount);
                tInfo[4] = String.format("£%d", transaction.resultingBalance);
                list.add(tInfo);
            }
            
            transactionResultPage.setItems(list);
            atm.loadToScreen(transactionResultPage);
            
        }
        else 
        {
            String text = "";
            if(response.invalidAccountInfo)
                text = "Unable to retrieve the balance due to the incorrect account information given.";
            else if(response.unableToGetBankingInfo)
                text = "Unable to connect to the network\n Our apologies for the inconvenience.";
            resultPage.update(result, text);
            atm.loadToScreen(resultPage);
        }
        
    }

    @Override
    public void receive(WithdrawalResponse response)
    {
        String text;
        boolean result = response.sucess();
        
        if(result)
            text = String.format("Withdrawal of £%d successful."
                    + "\n\nPlease press ENTER for another transaction or CANCEL "
                    + "to end.", response.withdrawn);
        else if(response.unableToGetBankingInfo)
            text = "Unable to connect to the network\n Our apologies for the inconvenience.";
        else if(response.invalidAccountInfo)
            text = "Unable to retrieve the balance due to the incorrect account information given.";
        else 
            text = "There are insufficient funds in your account to process the withdrawal.";
            
        resultPage.update(result, text);
        atm.loadToScreen(resultPage);
        
    }

    @Override
    public void receive(DepositResponse response)
    {
        String text;
        boolean result = response.sucess();
        
        if(result)
            text = String.format("Your account has been credited £%d successful."
                    + "\n\nPlease press ENTER for another transaction or CANCEL "
                    + "to end.", response.deposit);
        else if(response.unableToGetBankingInfo)
            text = "Unable to connect to the network\n Our apologies for the inconvenience.";
        else
            text = "Unable to retrieve the balance due to the incorrect account information given.";
            
        resultPage.update(result, text);
        atm.loadToScreen(resultPage);
    }


    
    
    
}
