package CardAssociation;

import FinancialMessaging.BalanceRequest;
import FinancialMessaging.BalanceResponse;
import Banking.BankAccount;
import Banking.Transaction;
import Banking.Exception.AccountNotFoundException;
import Banking.Exception.BankingException;
import Database.ConnectionException;
import Banking.Exception.InsufficientFunds;
import FinancialMessaging.DepositRequest;
import FinancialMessaging.DepositResponse;
import FinancialMessaging.TransactionsRequest;
import FinancialMessaging.TransactionsResponse;
import FinancialMessaging.WithdrawalRequest;
import FinancialMessaging.WithdrawalResponse;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the CardAssociation interface controlling requests 
 * validating the account information before processing the request with the 
 * respective {@link BankAccount}. 
 * 
 * This implementation does not record requests or provide any additional checks
 * of it's own regarding fraudulent behaviour.
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class DebitCardAssociation implements CardAssociation
{
    private final static Logger LOGGER = Logger.getLogger(DebitCardAssociation.class.getName());
    
    private final CardBankAccountRepository accountRepository;
    private final long id;
    private final String name;
    
    public DebitCardAssociation(CardBankAccountRepository repository, long id, 
            String name)
    {
        this.accountRepository = repository;
        this.id = id;
        this.name = name;
    }

    @Override
    public String getName() {return name;}

    @Override
    public BalanceResponse processRequest(BalanceRequest request)
    {
        try
        {
            BankAccount account = accountRepository
                    .getByCardId(request.cardTypeId, request.accoundNumber, 
                            request.accountPass);
            int balance = account.getBalance();
            return BalanceResponse.success(balance);
        } catch(ConnectionException ex)
        {
            LOGGER.log(Level.WARNING, null, ex);
            return BalanceResponse.UNABLE_TO_GET_INFO;
        } catch(AccountNotFoundException ex)
        {
            LOGGER.log(Level.INFO, ex.getMessage(), ex);
            return BalanceResponse.INVALID_ACCOUNT_INFO;
        }
    }

    @Override
    public TransactionsResponse processRequest(TransactionsRequest request)
    {
        try
        {
            List<Transaction> transactions = accountRepository
                    .getTransactionsByCardId(request.cardTypeId, 
                            request.accoundNumber, request.accountPass);
            return TransactionsResponse.success(transactions
                    .toArray(new Transaction[transactions.size()]));
        } catch(ConnectionException ex)
        {
            LOGGER.log(Level.WARNING, null, ex);
            return TransactionsResponse.UNABLE_TO_GET_INFO;
        } catch(AccountNotFoundException ex)
        {
            LOGGER.log(Level.INFO, ex.getMessage(), ex);
            return TransactionsResponse.INVALID_ACCOUNT_INFO;
        }
    }

    @Override
    public WithdrawalResponse processRequest(WithdrawalRequest request)
    {
        try
        {
            BankAccount account = accountRepository
                    .getByCardId(request.cardTypeId, request.accoundNumber, 
                            request.accountPass);
            account.processAmountTransaction(request.location, request.amount *-1);
            accountRepository.save(account);
            return WithdrawalResponse.success(request.amount);
            
        } catch(ConnectionException ex)
        {
            LOGGER.log(Level.WARNING, null, ex);
            return WithdrawalResponse.UNABLE_TO_GET_INFO;
        } catch(AccountNotFoundException ex)
        {
            LOGGER.log(Level.INFO, ex.getMessage(), ex);
            return WithdrawalResponse.INVALID_ACCOUNT_INFO;
        } catch(InsufficientFunds ex)
        {
            LOGGER.log(Level.INFO, ex.getMessage(), ex);
            return WithdrawalResponse.INSUFFICIENT_FUNDS;
        }
    }

    @Override
    public DepositResponse processRequest(DepositRequest request)
    {
        try
        {
            BankAccount account = accountRepository
                    .getByCardId(request.cardTypeId, request.accoundNumber, 
                            request.accountPass);
            account.processAmountTransaction(request.location, request.amount);
            accountRepository.save(account);
            return DepositResponse.success(request.amount);
        } catch(ConnectionException ex)
        {
            LOGGER.log(Level.WARNING, null, ex);
            return DepositResponse.UNABLE_TO_GET_INFO;
        } catch(BankingException ex)
        {
            LOGGER.log(Level.INFO, ex.getMessage(), ex);
            return DepositResponse.INVALID_ACCOUNT_INFO;
        }
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof DebitCardAssociation))
            return false;
        
        DebitCardAssociation bca = (DebitCardAssociation) o;
        
        return (this.id == bca.id && 
                this.name.equals(bca.name) && 
                this.accountRepository.equals(bca.accountRepository));
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.accountRepository);
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 53 * hash + Objects.hashCode(this.name);
        return hash;
    }
    
    
}
