package Banking.Account;

import Banking.LocalBankAccount;
import Banking.BankAccount;
import Banking.Transaction;
import CardAssociation.LocalCardBankRepository;
import Banking.Exception.AccountNotFoundException;
import Database.ConnectionException;
import Database.ObjectToPs;
import Database.RsToObject;
import Database.SQLDatabaseComponent;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class LocalCardBankRepositoryTest
{
    
    public LocalCardBankRepositoryTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getByCardId method, of class LocalCardBankRepository.
     * 
     * This test does not confirm the correctness of the SQL statement made 
     * by repository, instead the {@link ResultSet} is mocked and we are testing
     * the implementation of the repository to coordinate with the 
     * {@link SQLDatabaseComponent} and the {@link RsToObject} defined internally
     * by the repository.
     * 
     * The SQL is tested externally directly with the database to confirm.
     * 
     * @throws java.lang.Exception No exception is expected to be thrown from 
     * this test as it does not connect to the database.
     */
    @Test
    public void testGetByCardId() throws Exception
    {
        System.out.println("getByCardId");
        long id = 1L;
        int accountNum = 41264242;
        int accountPass = 2332;
        int balance = 100;
        int overdraftLimit = 1000;
        
        MockSQLDatabaseComponent sqldc = new MockSQLDatabaseComponent();
        MockResultSet mrs = new MockResultSet();
        sqldc.rs = mrs;
        //mock ResultSet values - null at index 0 as ResultSet starts at 1.
        mrs.result = new Object[]{ null, id, accountNum, accountPass, balance, overdraftLimit };
        
        LocalCardBankRepository instance = new LocalCardBankRepository(sqldc);
        BankAccount expResult = new LocalBankAccount(id, accountNum, accountPass,
                balance, overdraftLimit);
        BankAccount result = instance.getByCardId(id, accountNum, accountPass);
        
        assertEquals(expResult, result);
    }
    
    @Test (expected = AccountNotFoundException.class)
    public void testGetByCardId_InvalidAccountInfo() throws Exception
    {
        System.out.println("getByCardId_InvalidAccountInfo");
        
        long id = 1L;
        int accountNum = 41264242;
        int accountPass = 2332;
        
        MockSQLDatabaseComponent sqldc = new MockSQLDatabaseComponent();
        MockResultSet mrs = new MockResultSet();
        sqldc.rs = mrs;
        //set next counter to one - therefore next will return false immediately 
        //mocking a no result found.
        mrs.next = 1;
        
        LocalCardBankRepository instance = new LocalCardBankRepository(sqldc);
        instance.getByCardId(id, accountNum, accountPass);
    }
    
    @Test (expected = ConnectionException.class)
    public void testGetByCardId_ConnectionException() throws Exception
    {
        System.out.println("getByCardId_ConnectionException");
        
        long id = 1L;
        int accountNum = 41264242;
        int accountPass = 2332;
        
        MockSQLDatabaseComponent sqldc = new MockSQLDatabaseComponent();
        //No setting of MockResultSet in sqldc resulting in it throwing SQLException
        //to mock connection error.
        
        LocalCardBankRepository instance = new LocalCardBankRepository(sqldc);
        instance.getByCardId(id, accountNum, accountPass);
    }
    
    /**
     * Test of getTransactionsByCardId method, of class LocalCardBankRepository.
     * 
     * This test does not confirm the correctness of the SQL statement made 
     * by repository, instead the {@link ResultSet} is mocked and we are testing
     * the implementation of the repository to coordinate with the 
     * {@link SQLDatabaseComponent} and the {@link RsToObject} defined internally
     * by the repository.
     * 
     * The SQL is tested externally directly with the database to confirm.
     * 
     * @throws java.lang.Exception No exception is expected to be thrown from 
     * this test as it does not connect to the database.
     */
    @Test
    public void testGetTransactionsByCardId() throws Exception
    {
        System.out.println("getTransactionsByCardId");
        
        long id = 1L;
        int accountNum = 41264242;
        int accountPass = 2332;
        
        boolean isDebit = true;
        LocalDateTime ldt = LocalDateTime.now();
        String location = "location";
        int amount = 100;
        int newBalance = 200;
        
        Transaction transaction = new Transaction(id, isDebit, 
                ldt, location, amount, newBalance);
        
        MockSQLDatabaseComponent msqldc = new MockSQLDatabaseComponent();
        MockResultSet mrs = new MockResultSet();
        msqldc.rs = mrs;
        mrs.next = 1;
        mrs.result = new Object[] { null, null, id, isDebit, 
            Timestamp.valueOf(ldt), location, amount, newBalance};
        
        LocalCardBankRepository instance = new LocalCardBankRepository(msqldc);
        
        List<Transaction> expResult = new ArrayList<Transaction>() 
        {
            {add(transaction);}
        };
        
        List<Transaction> result = instance
                .getTransactionsByCardId(id, accountNum, accountPass);
        
        assertEquals(expResult, result);
    }
    
    /**
     * AccountNotFoundException is thrown when no transactions are found. When 
     * accounts are made they also must include a transaction to increase the 
     * balance above zero therefore a valid account will have at least one 
     * transaction.
     * 
     * @throws Exception 
     */
    @Test (expected = AccountNotFoundException.class)
    public void testGetTransactionsByCardId_InvalidAccountInfo() throws Exception
    {
        System.out.println("getTransactionsByCardId_InvalidAccountInfo");
        
        long id = 1L;
        int accountNum = 41264242;
        int accountPass = 2332;
        
        MockSQLDatabaseComponent sqldc = new MockSQLDatabaseComponent();
        MockResultSet mrs = new MockResultSet();
        sqldc.rs = mrs;
        mrs.firstResult = false;
        
        LocalCardBankRepository instance = new LocalCardBankRepository(sqldc);
        instance.getTransactionsByCardId(id, accountNum, accountPass);
    }
    
    @Test (expected = ConnectionException.class)
    public void testGetTransactionsByCardId_ConnectionException() throws Exception 
    {
        System.out.println("getTransactionsByCardId_ConnectionException");
        
        long id = 1L;
        int accountNum = 41264242;
        int accountPass = 2332;
        
        MockSQLDatabaseComponent sqldc = new MockSQLDatabaseComponent();
        //No setting of MockResultSet in sqldc resulting in it throwing SQLException
        //to mock connection error.
        
        LocalCardBankRepository instance = new LocalCardBankRepository(sqldc);
        instance.getTransactionsByCardId(id, accountNum, accountPass);
    }
    

    /**
     * Test of save method, of class LocalCardBankRepository.
     * 
     * This test does not confirm the correctness of the SQL statement made 
     * by repository, instead the {@link PreparedStatement} is mocked and we are testing
     * the implementation of the repository to coordinate with the 
     * {@link SQLDatabaseComponent} and the {@link ObjectToPs} defined internally
     * by the repository.
     * 
     * The save method will actually use two PreparedStatements if a Transaction
     * is to be saved. 
     * 
     * This test will scrutinise the use of both PreparedStatements.
     * 
     * 
     * The SQL is tested externally directly with the database to confirm.
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testSave() throws Exception
    {
        System.out.println("save");
        
        long id = 1;
        int accountNum = 41264242;
        int accountPass = 2332; 
        int balance = 100;
        int overdraftLimit = 1000;
        
        String location = "location";
        int amount = -50;
        
        LocalBankAccount account = new LocalBankAccount(id, accountNum, accountPass, 
                balance, overdraftLimit);
        account.processAmountTransaction(location, amount);
        
        MockSQLDatabaseComponent msqldc = new MockSQLDatabaseComponent();
        MockPreparedStatement mps = new MockPreparedStatement();
        msqldc.ps = mps;
        mps.columns = new Object[2][7];
        
        LocalCardBankRepository instance = new LocalCardBankRepository(msqldc);
        instance.save(account);
        
        //Checks the first PreparedStatement values - Bank_Account Table
        assertEquals(account.getBalance(), mps.columns[0][1]);
        assertEquals(id, mps.columns[0][2]);
        
        
        //Checks the second PreparedStatement Value - Bank_Account_Transactions Table
        assertEquals(id, mps.columns[1][1]);
        assertEquals(true, mps.columns[1][2]); //isDebit true
        assertEquals(Timestamp.valueOf(account
                .getTransactions()[0].getOccurred()), mps.columns[1][3]);
        assertEquals(location, mps.columns[1][4]);
        //debit is saved as a positive number in the transactions table.
        assertEquals(amount * -1, mps.columns[1][5]); 
        assertEquals(account.getBalance(), mps.columns[1][6]);
        
    }
    
    
    class MockSQLDatabaseComponent implements SQLDatabaseComponent
    {
        
        public ResultSet rs;
        public PreparedStatement ps;

        @Override
        public <T> T query(String query, RsToObject<T> rto) throws SQLException
        {
            if(rs != null)
                return rto.initObject(rs);
            throw new SQLException("Intentional expection for testing.");
        }

        @Override
        public <T> void update(String prepared, ObjectToPs<T> otr, T obj) throws SQLException
        {
            if(ps != null)
                otr.mapToPs(ps, obj);
            else
                throw new SQLException("Intentional exception for testing");
        }
        
    }
    
    //Most of the methods are not implemented in anyway but must be inherited.
    class MockResultSet implements ResultSet
    {
        int next = 0;
        public Object[] result;
        public boolean firstResult = true;

        @Override
        public boolean next() throws SQLException
        {
            //only one record.
            return next++ < 1;
        }

        @Override
        public void close() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean wasNull() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getString(int i) throws SQLException
        {
            return (String) result[i];
        }

        @Override
        public boolean getBoolean(int i) throws SQLException
        {
            return (Boolean) result[i];
        }

        @Override
        public byte getByte(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public short getShort(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getInt(int i) throws SQLException
        {
            return (Integer) result[i];
        }

        @Override
        public long getLong(int i) throws SQLException
        {
            return (Long) result[i];
        }

        @Override
        public float getFloat(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public double getDouble(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public BigDecimal getBigDecimal(int i, int i1) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public byte[] getBytes(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Date getDate(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Time getTime(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Timestamp getTimestamp(int i) throws SQLException
        {
            return (Timestamp) result[i];
        }

        @Override
        public InputStream getAsciiStream(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public InputStream getUnicodeStream(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public InputStream getBinaryStream(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getString(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean getBoolean(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public byte getByte(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public short getShort(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getInt(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public long getLong(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public float getFloat(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public double getDouble(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public BigDecimal getBigDecimal(String string, int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public byte[] getBytes(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Date getDate(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Time getTime(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Timestamp getTimestamp(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public InputStream getAsciiStream(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public InputStream getUnicodeStream(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public InputStream getBinaryStream(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public SQLWarning getWarnings() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void clearWarnings() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getCursorName() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ResultSetMetaData getMetaData() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getObject(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getObject(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int findColumn(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Reader getCharacterStream(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Reader getCharacterStream(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public BigDecimal getBigDecimal(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public BigDecimal getBigDecimal(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isBeforeFirst() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isAfterLast() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isFirst() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isLast() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void beforeFirst() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void afterLast() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean first() throws SQLException
        {
            return firstResult;
        }

        @Override
        public boolean last() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getRow() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean absolute(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean relative(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean previous() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setFetchDirection(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getFetchDirection() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setFetchSize(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getFetchSize() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getType() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getConcurrency() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean rowUpdated() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean rowInserted() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean rowDeleted() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateNull(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBoolean(int i, boolean bln) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateByte(int i, byte b) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateShort(int i, short s) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateInt(int i, int i1) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateLong(int i, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateFloat(int i, float f) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateDouble(int i, double d) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBigDecimal(int i, BigDecimal bd) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateString(int i, String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBytes(int i, byte[] bytes) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateDate(int i, Date date) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateTime(int i, Time time) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateTimestamp(int i, Timestamp tmstmp) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateAsciiStream(int i, InputStream in, int i1) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBinaryStream(int i, InputStream in, int i1) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateCharacterStream(int i, Reader reader, int i1) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateObject(int i, Object o, int i1) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateObject(int i, Object o) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateNull(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBoolean(String string, boolean bln) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateByte(String string, byte b) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateShort(String string, short s) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateInt(String string, int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateLong(String string, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateFloat(String string, float f) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateDouble(String string, double d) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBigDecimal(String string, BigDecimal bd) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateString(String string, String string1) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBytes(String string, byte[] bytes) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateDate(String string, Date date) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateTime(String string, Time time) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateTimestamp(String string, Timestamp tmstmp) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateAsciiStream(String string, InputStream in, int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBinaryStream(String string, InputStream in, int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateCharacterStream(String string, Reader reader, int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateObject(String string, Object o, int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateObject(String string, Object o) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void insertRow() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateRow() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void deleteRow() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void refreshRow() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void cancelRowUpdates() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void moveToInsertRow() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void moveToCurrentRow() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Statement getStatement() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getObject(int i, Map<String, Class<?>> map) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Ref getRef(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Blob getBlob(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Clob getClob(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Array getArray(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getObject(String string, Map<String, Class<?>> map) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Ref getRef(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Blob getBlob(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Clob getClob(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Array getArray(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Date getDate(int i, Calendar clndr) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Date getDate(String string, Calendar clndr) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Time getTime(int i, Calendar clndr) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Time getTime(String string, Calendar clndr) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Timestamp getTimestamp(int i, Calendar clndr) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Timestamp getTimestamp(String string, Calendar clndr) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public URL getURL(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public URL getURL(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateRef(int i, Ref ref) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateRef(String string, Ref ref) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBlob(int i, Blob blob) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBlob(String string, Blob blob) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateClob(int i, Clob clob) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateClob(String string, Clob clob) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateArray(int i, Array array) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateArray(String string, Array array) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public RowId getRowId(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public RowId getRowId(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateRowId(int i, RowId rowid) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateRowId(String string, RowId rowid) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getHoldability() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isClosed() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateNString(int i, String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateNString(String string, String string1) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateNClob(int i, NClob nclob) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateNClob(String string, NClob nclob) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public NClob getNClob(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public NClob getNClob(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public SQLXML getSQLXML(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public SQLXML getSQLXML(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateSQLXML(int i, SQLXML sqlxml) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateSQLXML(String string, SQLXML sqlxml) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getNString(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getNString(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Reader getNCharacterStream(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Reader getNCharacterStream(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateNCharacterStream(int i, Reader reader, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateNCharacterStream(String string, Reader reader, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateAsciiStream(int i, InputStream in, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBinaryStream(int i, InputStream in, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateCharacterStream(int i, Reader reader, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateAsciiStream(String string, InputStream in, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBinaryStream(String string, InputStream in, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateCharacterStream(String string, Reader reader, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBlob(int i, InputStream in, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBlob(String string, InputStream in, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateClob(int i, Reader reader, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateClob(String string, Reader reader, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateNClob(int i, Reader reader, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateNClob(String string, Reader reader, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateNCharacterStream(int i, Reader reader) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateNCharacterStream(String string, Reader reader) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateAsciiStream(int i, InputStream in) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBinaryStream(int i, InputStream in) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateCharacterStream(int i, Reader reader) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateAsciiStream(String string, InputStream in) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBinaryStream(String string, InputStream in) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateCharacterStream(String string, Reader reader) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBlob(int i, InputStream in) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateBlob(String string, InputStream in) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateClob(int i, Reader reader) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateClob(String string, Reader reader) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateNClob(int i, Reader reader) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void updateNClob(String string, Reader reader) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public <T> T getObject(int i, Class<T> type) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public <T> T getObject(String string, Class<T> type) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public <T> T unwrap(Class<T> type) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isWrapperFor(Class<?> type) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    class MockPreparedStatement implements PreparedStatement
    {
        private int statementNo = 0;
        public Object[][] columns;

        @Override
        public ResultSet executeQuery() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int executeUpdate() throws SQLException
        {
            statementNo+= 1;
            return -1;
        }

        @Override
        public void setNull(int i, int i1) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setBoolean(int i, boolean bln) throws SQLException
        {
            columns[statementNo][i] = bln;
        }

        @Override
        public void setByte(int i, byte b) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setShort(int i, short s) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setInt(int i, int i1) throws SQLException
        {
            columns[statementNo][i] = i1;
        }

        @Override
        public void setLong(int i, long l) throws SQLException
        {
            columns[statementNo][i] = l;
        }

        @Override
        public void setFloat(int i, float f) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setDouble(int i, double d) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setBigDecimal(int i, BigDecimal bd) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setString(int i, String string) throws SQLException
        {
            columns[statementNo][i] = string;
        }

        @Override
        public void setBytes(int i, byte[] bytes) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setDate(int i, Date date) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setTime(int i, Time time) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setTimestamp(int i, Timestamp tmstmp) throws SQLException
        {
            columns[statementNo][i] = tmstmp;
        }

        @Override
        public void setAsciiStream(int i, InputStream in, int i1) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setUnicodeStream(int i, InputStream in, int i1) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setBinaryStream(int i, InputStream in, int i1) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void clearParameters() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setObject(int i, Object o, int i1) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setObject(int i, Object o) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean execute() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void addBatch() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setCharacterStream(int i, Reader reader, int i1) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setRef(int i, Ref ref) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setBlob(int i, Blob blob) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setClob(int i, Clob clob) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setArray(int i, Array array) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ResultSetMetaData getMetaData() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setDate(int i, Date date, Calendar clndr) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setTime(int i, Time time, Calendar clndr) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setTimestamp(int i, Timestamp tmstmp, Calendar clndr) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setNull(int i, int i1, String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setURL(int i, URL url) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ParameterMetaData getParameterMetaData() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setRowId(int i, RowId rowid) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setNString(int i, String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setNCharacterStream(int i, Reader reader, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setNClob(int i, NClob nclob) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setClob(int i, Reader reader, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setBlob(int i, InputStream in, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setNClob(int i, Reader reader, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setSQLXML(int i, SQLXML sqlxml) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setObject(int i, Object o, int i1, int i2) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setAsciiStream(int i, InputStream in, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setBinaryStream(int i, InputStream in, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setCharacterStream(int i, Reader reader, long l) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setAsciiStream(int i, InputStream in) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setBinaryStream(int i, InputStream in) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setCharacterStream(int i, Reader reader) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setNCharacterStream(int i, Reader reader) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setClob(int i, Reader reader) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setBlob(int i, InputStream in) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setNClob(int i, Reader reader) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ResultSet executeQuery(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int executeUpdate(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void close() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getMaxFieldSize() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setMaxFieldSize(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getMaxRows() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setMaxRows(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setEscapeProcessing(boolean bln) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getQueryTimeout() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setQueryTimeout(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void cancel() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public SQLWarning getWarnings() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void clearWarnings() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setCursorName(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean execute(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ResultSet getResultSet() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getUpdateCount() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean getMoreResults() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setFetchDirection(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getFetchDirection() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setFetchSize(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getFetchSize() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getResultSetConcurrency() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getResultSetType() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void addBatch(String string) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void clearBatch() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int[] executeBatch() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Connection getConnection() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean getMoreResults(int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ResultSet getGeneratedKeys() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int executeUpdate(String string, int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int executeUpdate(String string, int[] ints) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int executeUpdate(String string, String[] strings) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean execute(String string, int i) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean execute(String string, int[] ints) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean execute(String string, String[] strings) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getResultSetHoldability() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isClosed() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setPoolable(boolean bln) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isPoolable() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void closeOnCompletion() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isCloseOnCompletion() throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public <T> T unwrap(Class<T> type) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isWrapperFor(Class<?> type) throws SQLException
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
}
