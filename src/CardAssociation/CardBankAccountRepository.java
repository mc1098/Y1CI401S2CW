package CardAssociation;

import Banking.BankAccount;
import Banking.Transaction;
import Banking.Exception.AccountNotFoundException;
import Database.ConnectionException;
import java.util.List;

/**
 * Repository interface for persistence of {@link BankAccount} and 
 * {@link Transactions} for {@link CardAssociation}.
 * 
 * The repository defines methods that can accessed by CardAssociations in order
 * to make requests on that BankAccount interface.
 * 
 * This interface explicitly hides how the persistent objects are saved and loaded
 * into and from memory respectively, therefore implementations are able 
 * to change details of persistence techniques or stores without users of this 
 * interface having to recompile.
 * 
 * @see BankAccount
 * @see Transaction
 * @see CardAssociation
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface CardBankAccountRepository
{
    
    /**
     * Return a {@link BankAccount} object which is linked to the respective 
     * Card Association card type id. 
     * 
     * @param id Card Association Card Type Id.
     * @param accountNumber account number of the BankAccount to be returned.
     * @param accountPass account password of the BankAccount to be returned.
     * 
     * @return Retrieve a {@link BankAccount} object which is linked to the 
     * respective Card Association card type id.
     * 
     * @throws ConnectionException Thrown when an error in connecting to the 
     * data store is experienced by the implementing repository.
     * @throws AccountNotFoundException Thrown when the information provided 
     * does not match any accounts stored.
     * 
     * @see BankAccount
     * @see CardAssociation
     * @see ConnectionException
     * @see AccountNotFoundException
     * 
     */
    public BankAccount getByCardId(long id, int accountNumber, int accountPass)
            throws 
            ConnectionException, 
            AccountNotFoundException;
    
    /**
     * Returns a List of {@link Transaction} objects which are linked to the 
     * {@link BankAccount} and the respective Card Association card type id.
     * 
     * @param id Card Association Card Type Id.
     * @param accountNumber account number of the BankAccount to be returned.
     * @param accountPass account password of the BankAccount to be returned.
     * @return Retrieves a List of {@link Transaction} objects which are linked 
     * to the {@link BankAccount} and the respective Card Association card type 
     * id.
     * @throws ConnectionException Thrown when an error in connecting to the 
     * data store is experienced by the implementing repository.
     * @throws AccountNotFoundException AccountNotFoundException Thrown when the 
     * information provided does not match any accounts stored.
     * 
     * @see BankAccount
     * @see Transaction
     * @see ConnectionException
     * @see AccountNotFoundException
     * 
     */
    public List<Transaction> getTransactionsByCardId(long id, int accountNumber, 
            int accountPass) 
            throws 
            ConnectionException, 
            AccountNotFoundException;
    
    
    /**
     * Saves the {@link BankAccount} object to a persistence store. 
     * 
     * Implementations of this method will be blocking until all the data 
     * has been stored successfully, therefore when control is returned from 
     * this method this will confirm that the save has been successful. 
     * 
     * The Bank account details with be saved by this method and any transient
     * {@link Transaction} that have been stored will also be saved. 
     * 
     * @param account Bank account to save.
     * @throws ConnectionException thrown when an error in connecting to the 
     * data store is experienced by the implementing repository.
     * 
     * @see BankAccount
     * @see Transaction
     * @see ConnectionException
     */
    public void save(BankAccount account) 
            throws 
            ConnectionException;
}
