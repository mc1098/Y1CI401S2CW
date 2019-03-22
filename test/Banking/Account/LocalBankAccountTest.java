package Banking.Account;

import Banking.LocalBankAccount;
import Banking.Transaction;
import Banking.Exception.InsufficientFunds;
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
public class LocalBankAccountTest
{
    
    public LocalBankAccountTest()
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

    @Test
    public void testGetId()
    {
        System.out.println("getId");
        
        long expResult = 5456;
        LocalBankAccount instance = new LocalBankAccount(expResult, 0, 0, 0, 0);
        long result = instance.getId();
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetAccountNumber()
    {
        System.out.println("getAccountNumber");
        
        int expResult = 41235678;
        LocalBankAccount instance = new LocalBankAccount(0, expResult, 0, 0, 0);
        int result = instance.getAccountNumber();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAccountPassword()
    {
        System.out.println("getAccountPassword");
        
        int expResult = 1234;
        LocalBankAccount instance = new LocalBankAccount(0, 0, expResult, 0, 0);
        int result = instance.getAccountPassword();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetBalance()
    {
        System.out.println("getBalance");
        
        int expResult = 500;
        LocalBankAccount instance = new LocalBankAccount(0, 0, 0, expResult, 0);
        int result = instance.getBalance();
        assertEquals(expResult, result);
    }
    
    @Test 
    public void testWithdrawal() throws Exception
    {
        System.out.println("processAmountTransaction_Withdrawal");
        
        String location = "Java Town";
        int amount = -200; //withdrawal are explicitly negative values 
        
        LocalBankAccount instance = new LocalBankAccount(1, 41235678, 1234, 500, 500);
        
        assertEquals(500, instance.getBalance());
        instance.processAmountTransaction(location, amount);
        assertEquals(300, instance.getBalance());
        //balance change expected plus a new transaction in transient history.
        
        assertEquals(1, instance.getTransactions().length);
        Transaction transaction = instance.getTransactions()[0];
        assertTrue(transaction.isDebit());
        assertEquals(amount * -1, transaction.getAmount());
        assertEquals(instance.getBalance(), transaction.getBalance());
        
    }
    
    @Test (expected = InsufficientFunds.class)
    public void testWithdrawal_InsufficientFunds() throws Exception
    {
        System.out.println("processAmountTransaction_Withdrawal_InsufficientFunds");
        
        String location = "Java Town";
        int amount = -200; //withdrawal are explicitly negative values 
        
        LocalBankAccount instance = new LocalBankAccount(1, 41235678, 1234, 0, 100);
        assertEquals(0, instance.getBalance());
        instance.processAmountTransaction(location, amount);
        
    }
    
    @Test 
    public void testDeposit() throws Exception
    {
        System.out.println("processAmountTransaction_Deposit");
        
        String location = "Java Town";
        int amount = 200; //Deposit are explicitly positive values 
        
        LocalBankAccount instance = new LocalBankAccount(1, 41235678, 1234, 500, 500);
        
        assertEquals(500, instance.getBalance());
        instance.processAmountTransaction(location, amount);
        assertEquals(700, instance.getBalance());
        //balance change expected plus a new transaction in transient history.
        
        assertEquals(1, instance.getTransactions().length);
        Transaction transaction = instance.getTransactions()[0];
        assertFalse(transaction.isDebit());
        assertEquals(amount, transaction.getAmount());
        assertEquals(instance.getBalance(), transaction.getBalance());
        
    }
    
    
    
}
