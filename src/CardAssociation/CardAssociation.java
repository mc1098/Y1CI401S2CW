package CardAssociation;

import FinancialMessaging.BalanceRequest;
import FinancialMessaging.BalanceResponse;
import FinancialMessaging.DepositRequest;
import FinancialMessaging.DepositResponse;
import FinancialMessaging.TransactionsRequest;
import FinancialMessaging.TransactionsResponse;
import FinancialMessaging.WithdrawalRequest;
import FinancialMessaging.WithdrawalResponse;


/**
 * This interface represents the Card Association that manages the Card, they 
 * provide a buffer between a Payment Processor and the account holders bank 
 * account.
 * 
 * Implementations of this interface may review the request information against
 * a database of other requests made to evaluate whether this could be fraudulent.
 * 
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface CardAssociation
{
    /**
     * @return Return name of Card Association.
     */
    public String getName();
    
    /**
     * Processes the {@link BalanceRequest} and uses the {@link BankAccount}, if
     * valid, to fulfil the request returning the response.
     * 
     * @param request to be processed.
     * @return {@link BalanceResponse} indicating whether the request was 
     * successful.
     * 
     * @see BalanceRequest
     * @see BankAccount
     * @see BalanceResponse
     * 
     */
    public BalanceResponse processRequest(BalanceRequest request);
    
    /**
     * Processes the {@link TransactionsRequest} and uses the {@link BankAccount},
     * if valid, to fulfil the request returning the response.
     * 
     * @param request to be processed.
     * @return {@link TransactionsResponse} indicating whether the request was 
     * successful.
     * 
     * @see TransactionsRequest
     * @see BankAccount
     * @see TransactionsResponse
     * 
     */
    public TransactionsResponse processRequest(TransactionsRequest request);
    
    /**
     * Processes the {@link WithdrawalRequest} and uses the {@link BankAccount},
     * if valid, to fulfil the request returning the response.
     * 
     * @param request to be processed.
     * @return {@link WithdrawalResponse} indicating whether the request was 
     * successful.
     * 
     * @see WithdrawalRequest
     * @see BankAccount
     * @see WithdrawalResponse
     * 
     */
    public WithdrawalResponse processRequest(WithdrawalRequest request);
    
    /**
     * Processes the {@link DepositRequest} and uses the {@link BankAccount},
     * if valid, to fulfil the request returning the response.
     * 
     * @param request to be processed.
     * @return {@link DepositResponse} indicating whether the request was 
     * successful.
     * 
     * @see DepositRequest
     * @see BankAccount
     * @see DepositResponse
     * 
     */
    public DepositResponse processRequest(DepositRequest request);
}
