package atm;

import java.util.ArrayList;

// a simple implementation of a bank
public class LocalBank
{
  int theAccNumber = 0;        // The current account number
  int theAccPasswd = 0;        // The current account password
  
  LocalBankAccount currentAccount = null;
  ArrayList<LocalBankAccount> accounts = new ArrayList<>();
  
    public LocalBank()
  {
      accounts.add(new LocalBankAccount(24601, 12345, 100, -50));
      accounts.add(new LocalBankAccount(31416, 22222, 50, 0));
  }

  
  public void setAccNumber( int accNumber ) 
  { 
    // Need to add code to record account number
    
  }

  
  public void setAccPasswd( int accPasswd ) 
  { 
    // Need to add code to record password

  }

  
  public void loggedOut() 
  {
    theAccNumber = -1;
    theAccPasswd = -1;     
    currentAccount = null;
  }

 
  public boolean checkValid() 
  { 
    // Need to add code to check if User/Password is valid
    Debug.trace( "Bank: checkValid" ); 
    // search the arraylist to find a bank account with matching account and password
    // if you find it, store it in the variable currentAccount and return true
    // if you don't find it, reset everything and return false
    
    
    
    theAccNumber = -1;
    theAccPasswd = -1;    
    currentAccount = null;
    return false;
  }

  
  public boolean withdraw( int amount ) 
  { 
    // Need to add code to withdraw amount from account
    Debug.trace( "Bank: withdraw %d", amount ); 
    
    return true; 
  }
  
  public boolean deposit( int amount )
  { 
    // Need to add code to deposit amount
    Debug.trace( "Bank: Deposit " + amount ); 
    
    return false;
  }
  
  
  public int getBalance() 
  { 
    // Need to add code to get the account balance
    Debug.trace( "Bank: get balance" ); 
    
    return 0;
  }
}
