package CardAssociation;

import Banking.BankAccount;
import Banking.LocalBankAccount;
import Banking.Transaction;
import Banking.Exception.AccountNotFoundException;
import Database.ConnectionException;
import Database.RsToObject;
import Database.SQLDatabaseComponent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import Database.ObjectToPs;
import java.sql.Timestamp;

/**
 * This repository contains the details for querying
 * a SQL database and initialising a {@link LocalBankAccount}.
 * 
 * 
 * The SQL querying in this repository is kept vendor independent allowing 
 * queries to be sent to the given {@link SQLDatabaseComponent} which contains 
 * specific vendor implementation details.
 * 
 * @see DebitCardAssociation
 * @see CardType
 * @see SQLDatabaseComponent
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class LocalCardBankRepository 
        implements CardBankAccountRepository
{
    private final SQLDatabaseComponent database;
    private final RsToTransactions rsToTransactions;
    private final RsToLocalBankAccount rsToLocalBankAccount;
    private final UpdateBalanceToPs updateBalanceToPs;
    private final InsertTransactionsToPs insertTransactionsToPs;
    
    public LocalCardBankRepository(SQLDatabaseComponent database)
    {
        this.database = database;
        this.rsToTransactions = new RsToTransactions();
        this.rsToLocalBankAccount = new RsToLocalBankAccount();
        this.updateBalanceToPs = new UpdateBalanceToPs();
        this.insertTransactionsToPs = new InsertTransactionsToPs();
    }

    
    @Override
    public BankAccount getByCardId(long id, int accountNum, int accountPass) 
            throws ConnectionException, AccountNotFoundException
    {
        String query = String.format("select a.* from atm.bank_account a \n" +
            "inner join atm.BANK_ACCOUNT_TRANSACTIONS t on t.account_id = a.account_id \n" +
            "inner join atm.CARD_ACCOUNT c on c.BANK_ACCOUNT_ID = t.account_id\n" +
            "inner join atm.card_association ca on ca.card_association_id = c.card_association_id\n" +
            "where c.card_association_id = %d AND a.account_number = %d AND "
                + "a.account_password = %d", id, accountNum, accountPass);
        
        try 
        {
            LocalBankAccount account = database.query(query, rsToLocalBankAccount);
            if(account == null)
                throw new AccountNotFoundException(String.format("No Bank account "
                    + "found that is linked to the card id of %d", id));
            
            return account;
            
        } catch(SQLException ex)
        {
            throw new ConnectionException(ex);
        }
    }
    
    @Override
    public List<Transaction> getTransactionsByCardId(long id, int accountNum, 
            int accountPass) 
            throws ConnectionException, AccountNotFoundException
    {
        String query = String.format("select t.* from atm.bank_account a \n" +
            "inner join atm.BANK_ACCOUNT_TRANSACTIONS t on t.account_id = a.account_id \n" +
            "inner join atm.CARD_ACCOUNT c on c.BANK_ACCOUNT_ID = t.account_id\n" +
            "inner join atm.card_association ca on ca.card_association_id = c.card_association_id\n" +
            "where c.card_association_id = %d AND a.account_number = %d "
                + "AND a.account_password = %d", id, accountNum, accountPass);
        
        try
        {
            List<Transaction> transactions = database.query(query, rsToTransactions);
            if(transactions == null)
                throw new AccountNotFoundException("The account information "
                        + "provided was incorrect");
            return transactions;
        } catch (SQLException ex)
        {
            throw new ConnectionException(ex);
        }
    }

    @Override
    public void save(BankAccount account) throws ConnectionException
    {
        if(!(account instanceof LocalBankAccount))
            return;
        
        LocalBankAccount lba = (LocalBankAccount) account;
        
        String update = "update bank_account a set a.balance = ?"
                + "where a.account_id = ?";
        
        try 
        {
            database.update(update, updateBalanceToPs, lba);
            if(lba.getTransactions().length > 0)
            {
                //insert.
                StringBuilder insert = new StringBuilder("insert into "
                        + "bank_account_transactions (account_id, debit, "
                        + "occurred, location, amount, balance) VALUES ");
                
                for (int i = 0; i < lba.getTransactions().length; i++)
                    insert.append("(?, ?, ?, ?, ?, ?) ");
                
                database.update(insert.toString(), insertTransactionsToPs, 
                        lba.getTransactions());
                
            }
        } catch(SQLException ex)
        {
            throw new ConnectionException(ex);
        }
    }

    

    
    
    class RsToLocalBankAccount implements RsToObject<LocalBankAccount>
    {

        @Override
        public LocalBankAccount initObject(ResultSet rs) throws SQLException
        {
            if(!rs.next())
                return null;
            
            long id = rs.getLong(1);
            int accNum = rs.getInt(2);
            int accPass = rs.getInt(3);
            int balance = rs.getInt(4);
            int overdraftLimit = rs.getInt(5);
            
            return new LocalBankAccount(id, accNum, accPass, balance, 
                    overdraftLimit);
            
        }
        
    }
    
    class RsToTransactions implements RsToObject<List<Transaction>>
    {

        @Override
        public List<Transaction> initObject(ResultSet rs) throws SQLException
        {
            List<Transaction> list = new ArrayList<>();
            if(!rs.first())
                return null;
            do 
            {
                long accountId = rs.getLong(2);
                boolean debit = rs.getBoolean(3);
                LocalDateTime occurred = rs.getTimestamp(4).toLocalDateTime();
                String location = rs.getString(5);
                int amount = rs.getInt(6);
                int balance = rs.getInt(7);
                list.add(new Transaction(accountId, debit, occurred, location, 
                        amount, balance));
                
            } while(rs.next());
                    
            return list;
        }

        
    }
    
    class UpdateBalanceToPs implements ObjectToPs<LocalBankAccount>
    {

        @Override
        public void mapToPs(PreparedStatement ps, LocalBankAccount t) throws SQLException
        {
            ps.setInt(1, t.getBalance());
            ps.setLong(2, t.getId());
            ps.executeUpdate();
        }
        
    }
    
    class InsertTransactionsToPs implements ObjectToPs<Transaction[]>
    {

        @Override
        public void mapToPs(PreparedStatement ps, Transaction[] ts) throws SQLException
        {
            for (Transaction t : ts)
            {
                ps.setLong(1, t.getAccountId());
                ps.setBoolean(2, t.isDebit());
                ps.setTimestamp(3, Timestamp.valueOf(t.getOccurred()));
                ps.setString(4, t.getLocation());
                ps.setInt(5, t.getAmount());
                ps.setInt(6, t.getBalance());
            }
            ps.executeUpdate();
        }
        
    }
    
}
