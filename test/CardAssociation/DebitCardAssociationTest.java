package CardAssociation;

import Banking.BankAccount;
import Banking.LocalBankAccount;
import Banking.Transaction;
import FinancialMessaging.BalanceResponse;
import FinancialMessaging.DepositResponse;
import Banking.Exception.AccountNotFoundException;
import Database.ConnectionException;
import FinancialMessaging.TransactionsResponse;
import FinancialMessaging.WithdrawalResponse;
import FinancialMessaging.BalanceRequest;
import FinancialMessaging.DepositRequest;
import FinancialMessaging.TransactionsRequest;
import FinancialMessaging.WithdrawalRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;
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
public class DebitCardAssociationTest
{
    
    public DebitCardAssociationTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        //Reset Logger so that tests don't log to console.
        LogManager.getLogManager().reset();
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
    public void testGetName()
    {
        System.out.println("getName");
        DebitCardAssociation instance = new DebitCardAssociation(null, -1, "name");
        String expResult = "name";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    @Test
    public void testProcessRequest_BalanceRequest()
    {
        System.out.println("BalanceRequest");
        
        BalanceRequest request = new BalanceRequest(1, 1, 1234);
        
        MockRepository mr = new MockRepository();
        //Mock return value by Repository for known balance of 100.
        mr.getByCardIdResult = new LocalBankAccount(1, 1, 1234, 100, 1000);
        DebitCardAssociation instance = new DebitCardAssociation(mr, 1, null);
        BalanceResponse expResult = BalanceResponse.success(100);
        
        BalanceResponse result = instance.processRequest(request);
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testProcessRequest_BalanceRequest_AccountNotFound()
    {
        System.out.println("BalanceRequest_AccountNotFound");
        
        BalanceRequest request = new BalanceRequest(1, 1, 1234);
        
        MockRepository mr = new MockRepository();
        //set mock repository to throw AccountNotFoundException
        mr.throwAccountNotFoundException = true;
        DebitCardAssociation instance = new DebitCardAssociation(mr, 1, null);
        
        BalanceResponse expResult = BalanceResponse.INVALID_ACCOUNT_INFO;
        BalanceResponse result = instance.processRequest(request);
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testProcessRequest_BalanceRequest_ConnectionException()
    {
        System.out.println("BalanceRequest_ConnectionException");
        
        BalanceRequest request = new BalanceRequest(1, 1, 1234);
        
        MockRepository mr = new MockRepository();
        //set mock repository to throw ConnectionException
        mr.throwConnectionException = true;
        DebitCardAssociation instance = new DebitCardAssociation(mr, 1, null);
        
        BalanceResponse expResult = BalanceResponse.UNABLE_TO_GET_INFO;
        BalanceResponse result = instance.processRequest(request);
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testProcessRequest_TransactionsRequest()
    {
        System.out.println("TransactionsRequest");
        
        TransactionsRequest request = new TransactionsRequest(1, 1, 1234);
        MockRepository mr = new MockRepository();
        Transaction transaction = new Transaction(1, true, LocalDateTime.now(), 
                "location", 50, 50);
        //mock Transactions to be returned.
        mr.getTransactionsByCardIdResult = new ArrayList<Transaction>() 
        {{
            add(transaction);
        }};
        
        DebitCardAssociation instance = new DebitCardAssociation(mr, 1, null);
        
        TransactionsResponse expResult = TransactionsResponse
                .success(new Transaction[] {transaction});
        
        TransactionsResponse result = instance.processRequest(request);
        assertEquals(expResult, result);
    }
    
    @Test 
    public void testProcessRequest_TransactionsRequest_AccountNotFound()
    {
        System.out.println("TransactionsRequest_AccountNotFound");
        
        TransactionsRequest request = new TransactionsRequest(1, 1, 1234);
        MockRepository mr = new MockRepository();
        //set mock repository to throw AccountNotFoundException
        mr.throwAccountNotFoundException = true;
        
        DebitCardAssociation instance = new DebitCardAssociation(mr, 1, null);
        TransactionsResponse expResult = TransactionsResponse.INVALID_ACCOUNT_INFO;
        
        TransactionsResponse result = instance.processRequest(request);
        assertEquals(expResult, result);
    }
    
    @Test 
    public void testProcessRequest_TransactionsRequest_ConnectionException()
    {
        System.out.println("TransactionsRequest_ConnectionException");
        
        TransactionsRequest request = new TransactionsRequest(1, 1, 1234);
        MockRepository mr = new MockRepository();
        //set mock repository to throw ConnectionException
        mr.throwConnectionException = true;
        
        DebitCardAssociation instance = new DebitCardAssociation(mr, 1, null);
        TransactionsResponse expResult = TransactionsResponse.UNABLE_TO_GET_INFO;
        
        TransactionsResponse result = instance.processRequest(request);
        assertEquals(expResult, result);
    }

    @Test
    public void testProcessRequest_WithdrawalRequest()
    {
        System.out.println("WithdrawalRequest");
        
        WithdrawalRequest request = new WithdrawalRequest(1, 1, 1234, "location", 100);
        MockRepository mr = new MockRepository();
        //mock returned account
        mr.getByCardIdResult = new LocalBankAccount(1, 1, 1234, 1000, 1000);
        
        DebitCardAssociation instance = new DebitCardAssociation(mr, 1, null);
        WithdrawalResponse expResult = WithdrawalResponse.success(100);
        WithdrawalResponse result = instance.processRequest(request);
        
        assertEquals(expResult, result);
        //confirm that save was called to update the account.
        assertTrue(mr.wasSaveCalled);
    }
    
    @Test
    public void testProcessRequest_WithdrawalRequest_AccountNotFound()
    {
        System.out.println("WithdrawalRequest_AccountNotFound");
        
        WithdrawalRequest request = new WithdrawalRequest(1, 1, 1234, "location", 100);
        MockRepository mr = new MockRepository();
        //set mock repository to throw AccountNotFoundException
        mr.throwAccountNotFoundException = true;
        
        DebitCardAssociation instance = new DebitCardAssociation(mr, 1, null);
        WithdrawalResponse expResult = WithdrawalResponse.INVALID_ACCOUNT_INFO;
        WithdrawalResponse result = instance.processRequest(request);
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testProcessRequest_WithdrawalRequest_ConnectionException()
    {
        System.out.println("WithdrawalRequest_ConnectionException");
        
        WithdrawalRequest request = new WithdrawalRequest(1, 1, 1234, "location", 100);
        MockRepository mr = new MockRepository();
        //set mock repository to throw ConnectionException
        mr.throwConnectionException = true;
        
        DebitCardAssociation instance = new DebitCardAssociation(mr, 1, null);
        WithdrawalResponse expResult = WithdrawalResponse.UNABLE_TO_GET_INFO;
        WithdrawalResponse result = instance.processRequest(request);
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testProcessRequest_WithdrawalRequest_InsufficientFunds()
    {
        System.out.println("WithdrawalRequest_InsufficientFunds");
        
        WithdrawalRequest request = new WithdrawalRequest(1, 1, 1234, "location", 200);
        MockRepository mr = new MockRepository();
        //mock returned account
        mr.getByCardIdResult = new LocalBankAccount(1, 1, 1234, 100, 0);
        
        DebitCardAssociation instance = new DebitCardAssociation(mr, 1, null);
        WithdrawalResponse expResult = WithdrawalResponse.INSUFFICIENT_FUNDS;
        WithdrawalResponse result = instance.processRequest(request);
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testProcessRequest_DepositRequest()
    {
        System.out.println("DepositRequest");
        
        DepositRequest request = new DepositRequest(1, 1, 1234, "location", 100);
        MockRepository mr = new MockRepository();
        mr.getByCardIdResult = new LocalBankAccount(1, 1, 1234, 200, 1000);
        DebitCardAssociation instance = new DebitCardAssociation(mr, 1, null);
        
        DepositResponse expResult = DepositResponse.success(100);
        DepositResponse result = instance.processRequest(request);
        assertEquals(expResult, result);
        //confirm that save was called to update the account.
        assertTrue(mr.wasSaveCalled);
    }
    
    @Test
    public void testProcessRequest_DepositRequest_AccountNotFound()
    {
        System.out.println("DepositRequest_AccountNotFound");
        
        DepositRequest request = new DepositRequest(1, 1, 1234, "location", 100);
        MockRepository mr = new MockRepository();
        //set mock repository to throw AccountNotFoundException
        mr.throwAccountNotFoundException = true;
        DebitCardAssociation instance = new DebitCardAssociation(mr, 1, null);
        
        DepositResponse expResult = DepositResponse.INVALID_ACCOUNT_INFO;
        DepositResponse result = instance.processRequest(request);
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testProcessRequest_DepositRequest_ConnectionException()
    {
        System.out.println("DepositRequest_ConnectionException");
        
        DepositRequest request = new DepositRequest(1, 1, 1234, "location", 100);
        MockRepository mr = new MockRepository();
        //set mock repository to throw ConnectionException
        mr.throwConnectionException = true;
        DebitCardAssociation instance = new DebitCardAssociation(mr, 1, null);
        
        DepositResponse expResult = DepositResponse.UNABLE_TO_GET_INFO;
        DepositResponse result = instance.processRequest(request);
        
        assertEquals(expResult, result);
    }
    
    class MockRepository implements CardBankAccountRepository
    {
        boolean throwAccountNotFoundException;
        boolean throwConnectionException;
        BankAccount getByCardIdResult;
        List<Transaction> getTransactionsByCardIdResult;
        boolean wasSaveCalled;
        

        @Override
        public BankAccount getByCardId(long id, int accountNumber, 
                int accountPass) 
                throws 
                ConnectionException, 
                AccountNotFoundException
        {
            if(throwAccountNotFoundException)
                throw new AccountNotFoundException("Intentional exception thrown for testing");
            if(throwConnectionException)
                throw new ConnectionException("Intentional exception thrown for testing");
            
            return getByCardIdResult;
        }

        @Override
        public List<Transaction> getTransactionsByCardId(long id, 
                int accountNumber, int accountPass) 
                throws 
                ConnectionException, 
                AccountNotFoundException
        {
            if(throwAccountNotFoundException)
                throw new AccountNotFoundException("Intentional exception thrown for testing");
            if(throwConnectionException)
                throw new ConnectionException("Intentional exception thrown for testing");
            
            return getTransactionsByCardIdResult;
        }

        @Override
        public void save(BankAccount account) throws ConnectionException
        {
            wasSaveCalled = true;
        }
        
    }
    
}
