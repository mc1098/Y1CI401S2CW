package FinancialMessaging;

import Banking.Transaction;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 *
 * Response object returned from a Transactions request. 
 * 
 * This object will contain whether the request was successful and then respective
 * data in either case.
 * 
 * When the request is successful then the Transactions array will contain the 
 * history of transactions for the account.
 * 
 * When the request is unsuccessful then the Transactions array will be empty, but
 * one of the error flags will be true indicating what caused the request to be
 * denied.
 * 
 * @see BankingResultHandler
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class TransactionsResponse implements BankingResponse
{
    public static TransactionsResponse INVALID_ACCOUNT_INFO = new TransactionsResponse(true, false);
    public static TransactionsResponse UNABLE_TO_GET_INFO = new TransactionsResponse(false, true);
    
    public static TransactionsResponse success(Transaction[] transactions)
    {
        return new TransactionsResponse(false, false, transactions);
    }
    
    public final boolean invalidAccountInfo;
    public final boolean unableToGetBankingInfo;
    public final TransactionDO[] transactions;
    
    public TransactionsResponse(boolean invalidAccountInfo, 
            boolean unableToGetBankingInfo, Transaction... transactions)
    {
        this.invalidAccountInfo = invalidAccountInfo;
        this.unableToGetBankingInfo = unableToGetBankingInfo;
        this.transactions = new TransactionDO[transactions.length];
        
        //Convert Banking.Transactions to internal class version 
        for (int i = 0; i < transactions.length; i++)
        {
            TransactionDO t = new TransactionDO();
            t.accountId = transactions[i].getAccountId();
            t.isDebit = transactions[i].isDebit();
            t.occurred = transactions[i].getOccurred();
            t.location = transactions[i].getLocation();
            t.amount = transactions[i].getAmount();
            t.resultingBalance = transactions[i].getBalance();
        }
    }
    
    public boolean sucess() {return !invalidAccountInfo && !unableToGetBankingInfo;}
    
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof TransactionsResponse))
            return false;
        
        TransactionsResponse tr = (TransactionsResponse) o;
        
        return (Arrays.equals(this.transactions, tr.transactions) && 
                this.invalidAccountInfo == tr.invalidAccountInfo && 
                this.unableToGetBankingInfo == tr.unableToGetBankingInfo);
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 89 * hash + (this.invalidAccountInfo ? 1 : 0);
        hash = 89 * hash + (this.unableToGetBankingInfo ? 1 : 0);
        hash = 89 * hash + Arrays.deepHashCode(this.transactions);
        return hash;
    }
    
    //Dependent classes on PaymentProcessing package can depend on this 
    //internal class instead of Banking package directly.
    public class TransactionDO
    {
        public long accountId;
        public boolean isDebit;
        public LocalDateTime occurred;
        public String location;
        public int amount;
        public int resultingBalance;
        
    }
    
}
