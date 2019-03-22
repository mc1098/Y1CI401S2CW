package PaymentProcessing;

/**
 * This interface defines the Banking methods supported for processing by 
 * this component. 
 * 
 * Calls to the component will result in the respective request being processed.
 * Implementations of these methods may process the requests locally or 
 * through a server.
 * 
 * The results of the process regardless of success or failure is then 
 * sent to the {@link BankingResultHandler}, which would be defined by the 
 * implementing class.
 * 
 * @see BankingResultHandler
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface BankingComponent
{
    /**
     * Sends a Balance request with the given information provided.
     * 
     * The result is returned to the {@link BankingResultHandler} which may be 
     * successful or not. 
     * 
     * @param accNum Account number of the account.
     * @param cardType Card type as defined by the prefix of the account number.
     * @param accPass Account password.
     * 
     * 
     */
    public void balance(int accNum, long cardType, int accPass);
    
    /**
     * Sends a transactions request with the given information provided. This
     * is a request for the transaction history associated with this account 
     * information.
     * 
     * The result is returned to the {@link BankingResultHandler} which maybe 
     * successful or not. 
     * 
     * @param accNum Account number of the account.
     * @param cardType Card type as defined by the prefix of the account number.
     * @param accPass Account password.
     */
    public void transactions(int accNum, long cardType, int accPass);
    
    /**
     * Sends a Withdrawal request with the given information provided.
     * 
     * The result is returned to the {@link BankingResultHandler} which maybe 
     * successful or not. 
     * 
     * @param accNum Account number of the account.
     * @param cardType Card type as defined by the prefix of the account number.
     * @param accPass Account password.
     * @param amount amount to withdraw.
     */
    public void withdraw(int accNum, long cardType, int accPass, int amount);
    
    /**
     * Sends a Deposit request with the given information provided.
     * 
     * The result is returned to the {@link BankingResultHandler} which maybe 
     * successful or not. 
     * 
     * @param accNum Account number of the account.
     * @param cardType Card type as defined by the prefix of the account number.
     * @param accPass Account password.
     * @param amount amount to deposit.
     */
    public void deposit(int accNum, long cardType, int accPass, int amount);
}
