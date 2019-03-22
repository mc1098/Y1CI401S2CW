
package Banking;

import Banking.Exception.InsufficientFunds;

/**
 * This interface defines the required methods for processing bank account 
 * requests, whether they be state changing or not. 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface BankAccount
{
    /**
     * Retrieve the current balance for this Bank Account instance.
     * <p>
     * The balance returned is signed integer value and will display a negative
     * value if the account is in overdraft.
     * </p>
     * @return the current balance for this Bank Account instance.
     */
    public int getBalance();
    
    /**
     * Return a transient history of {@link Transaction}s for this Bank Account instance.
     * <p>
     * The last transaction, if any, from this method should define the last 
     * transaction performed on this account and therefore the result from 
     * their respective getBalance() methods should by synonymous.
     * </p>
     * <p>
     * The transactions returned are transient, therefore transactions performed
     * in a previous session should be retrieved using the 
     * {@link BankAccountRepository}. This keeps the BankAccount instance a 
     * lightweight object even when representing an account that may have seen 
     * thousands of transactions. 
     * </p>
     * @return Retrieve a transient history of transactions for this Bank Account instance.
     * 
     * @see BankAccountRepository
     * @see Transaction
     */
    public Transaction[] getTransactions();
    
    /**
     * Process an amount request, positive amounts being credit and negative
     * being debit with the respective {@link Transaction} being created and stored 
     * on success of the request.
     * <p>
     * To clarify when a withdrawal or debit request is made then this method
     * should be used with a negative amount. Therefore a positive amount is 
     * expected when crediting the account.
     * </p>
     * The transaction is stored locally in a transient variable, it is therefore 
     * recommended that state is saved before returning confirmation of the 
     * transaction in order to maintain consistency in the event of failure.
     * 
     * @param location The location that this request took place.
     * @param amount The amount to be credited(+) or debited(-) to the account.
     * @throws InsufficientFunds This occurs in the event that a debit request
     * would result in the balance being over the overdraft limit. When this 
     * exception is thrown then no transaction is created.
     * 
     * @see Transaction
     * 
     */
    public void processAmountTransaction(String location, int amount) throws InsufficientFunds;
}
