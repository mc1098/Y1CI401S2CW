package Banking;

import Banking.Exception.InsufficientFunds;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class LocalBankAccount implements BankAccount
{
    private final long id;
    private final int accountNumber;
    private final int accountPassword;
    private int balance;
    private final int overdraftLimit;
    private final List<Transaction> transactions;
    
    /**
     * Creates a LocalBankAccount instance with the given account information.
     * 
     * @param id unique internal identifier for this account.
     * @param accNum Account number associated with this account.
     * @param accPass Account password associated with this account.
     * @param balance Current balance of this account.
     * @param overdraftLimit Overdraft Limit of this account. The numerical value
     * can be either positive or negative as it will be made negative internally.
     */
    public LocalBankAccount(long id, int accNum, int accPass, int balance, 
            int overdraftLimit)
    {
        this.id = id;
        this.accountNumber = accNum;
        this.accountPassword = accPass;
        this.balance = balance;
        this.overdraftLimit = overdraftLimit > 0 ? overdraftLimit*-1 : overdraftLimit;
        this.transactions = new ArrayList<>();
    }
    
    /**
     * @return Return this bank accounts Id for persistence.
     */
    public long getId() {return id;}
    
    /**
     * @return Return the bank account number for this instance.
     */
    public int getAccountNumber() {return accountNumber;}
    
    /**
     * @return Return the bank account password for this instance.
     */
    public int getAccountPassword() {return accountPassword;}
    
    @Override
    public int getBalance() {return balance;}

    @Override
    public Transaction[] getTransactions()
    {
        return transactions.toArray(new Transaction[transactions.size()]);
    }

    @Override
    public void processAmountTransaction(String location, int amount) 
            throws InsufficientFunds
    {
        if(amount > 0)
            processCredit(location, amount);
        else
            processDebit(location, amount);
    }
    
    /**
     * Processes a credit request creating and storing the resulting transaction. 
     * 
     * The transaction is stored locally in a transient variable, it is therefore 
     * recommended that state is saved before returning confirmation of the 
     * transaction in order to maintain consistency in the event of failure.
     * 
     * @param location The location that this request took place.
     * @param amount The amount to credit the account.
     */
    private void processCredit(String location, int amount)
    {
        balance+= amount;
        Transaction transaction = new Transaction(id, false, location, amount, 
                balance);
        transactions.add(transaction);
    }
    
    /**
     * Processes a debit request creating and storing the resulting transaction.
     * 
     * The transaction is stored locally in a transient variable, it is therefore 
     * recommended that state is saved before returning confirmation of the 
     * transaction in order to maintain consistency in the event of failure.
     * 
     * @param location The location that this request took place.
     * @param amount The amount to debit the account.
     * @throws InsufficientFunds This occurs in the event that a debit request
     * would result in the balance being over the overdraft limit. When this 
     * exception is thrown then no transaction is created.
     */
    private void processDebit(String location, int amount) 
            throws InsufficientFunds
    {
        //amount is negative here so we add instead of minus.
        if((balance + amount) < overdraftLimit)
            throw new InsufficientFunds(String.format("Debit request denied - "
                    + "Insufficient funds. Balance: %d Overdraft Limit: %d Debit "
                    + "request amount: %d",
                    balance, overdraftLimit, amount));
        
        balance+= amount;
        Transaction transaction = new Transaction(id, true, location, amount * -1,
                balance);
        transactions.add(transaction);
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof LocalBankAccount))
            return false;
        
        LocalBankAccount lba = (LocalBankAccount) o;
        
        return (this.id == lba.id && 
                this.accountNumber == lba.accountNumber &&
                this.accountPassword == lba.accountPassword &&
                this.balance == lba.balance &&
                this.overdraftLimit == lba.overdraftLimit);
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 29 * hash + this.accountNumber;
        hash = 29 * hash + this.accountPassword;
        hash = 29 * hash + this.balance;
        hash = 29 * hash + this.overdraftLimit;
        return hash;
    }
}
