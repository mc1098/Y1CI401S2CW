package Banking;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Transaction representing when a debit or credit event was processed.
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class Transaction
{
    private long accountId;
    private final boolean debit;
    private final LocalDateTime occurred;
    private final String location;
    private final int amount;
    private final int balance;
    
    public Transaction(long accountId, boolean debit, LocalDateTime occurred, 
            String location, int amount, int resultingBalance)
    {
        this.accountId = accountId;
        this.debit = debit;
        this.occurred = occurred;
        this.location = location;
        this.amount = amount;
        this.balance = resultingBalance;
    }
    
    public Transaction(long accountId, boolean debit, String location, 
            int amount, int resultingBalance)
    {
        this(accountId, debit, LocalDateTime.now(), location, amount, 
                resultingBalance);
    }
    
    /**
     * @return Return the Bank Account Id associated with this transaction.
     */
    public long getAccountId() {return accountId;}
    
    /**
     * @return Return true when the transaction represents a debit, false if credit.
     */
    public boolean isDebit() {return debit;}
    
    /**
     * @return Returns the date and time when transaction occurred.
     */
    public LocalDateTime getOccurred() {return occurred;}
    
    /**
     * @return Returns the location at which the transaction was requested.
     */
    public String getLocation() {return location;}
    
    /**
     * @return Returns the credit or debit amount, depending on the 
     * {@link #isDebit()} result.
     */
    public int getAmount() {return amount;}
    
    /**
     * @return Returns the resulting balance after this Transaction.
     */
    public int getBalance() {return balance;}
    
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Transaction))
            return false;
        
        Transaction t = (Transaction) o;
        
        return (this.accountId == t.accountId && 
                this.debit == t.debit && 
                this.occurred.equals(t.occurred) && 
                this.location.equals(t.location) && 
                this.amount == t.amount && 
                this.balance == t.balance);
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 53 * hash + (int) (this.accountId ^ (this.accountId >>> 32));
        hash = 53 * hash + (this.debit ? 1 : 0);
        hash = 53 * hash + Objects.hashCode(this.occurred);
        hash = 53 * hash + Objects.hashCode(this.location);
        hash = 53 * hash + this.amount;
        hash = 53 * hash + this.balance;
        return hash;
    }
}
