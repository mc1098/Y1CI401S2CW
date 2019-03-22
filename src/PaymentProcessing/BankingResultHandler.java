package PaymentProcessing;

import FinancialMessaging.WithdrawalResponse;
import FinancialMessaging.TransactionsResponse;
import FinancialMessaging.DepositResponse;
import FinancialMessaging.BalanceResponse;

/**
 * This interface represents the handling of results from request made using 
 * the {@link BankingComponent}.
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface BankingResultHandler
{
    
    /**
     * This method handles the {@link BalanceResponse}. 
     * 
     * Implementations of this method may process the response however they please
     * either further deploying the response via a transfer language (Json, XML etc)
     * or it may locally use the response directly to populate a UI element.
     * @param response BalanceResponse received by this method.
     * 
     * @see BalanceResponse
     * @see BankingComponent
     * 
     */
    public void receive(BalanceResponse response);
    
    /**
     * This method handles the {@link TransactionsResponse}. 
     * 
     * Implementations of this method may process the response however they please
     * either further deploying the response via a transfer language (Json, XML etc)
     * or it may locally use the response directly to populate a UI element.
     * @param response TransactionsResponse received by this method.
     * 
     * @see TransactionsResponse
     * @see BankingComponent
     * 
     */
    public void receive(TransactionsResponse response);
    
    /**
     * This method handles the {@link WithdrawalResponse}. 
     * 
     * Implementations of this method may process the response however they please
     * either further deploying the response via a transfer language (Json, XML etc)
     * or it may locally use the response directly to populate a UI element.
     * @param response WithdrawalResponse received by this method.
     * 
     * @see WithdrawalResponse
     * @see BankingComponent
     * 
     */
    public void receive(WithdrawalResponse response);
    
    /**
     * This method handles the {@link DepositResponse}. 
     * 
     * Implementations of this method may process the response however they please
     * either further deploying the response via a transfer language (Json, XML etc)
     * or it may locally use the response directly to populate a UI element.
     * @param response DepositResponse received by this method.
     * 
     * @see DepositResponse
     * @see BankingComponent
     * 
     */
    public void receive(DepositResponse response);
}
