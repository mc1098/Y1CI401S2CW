package atm;


public class LocalBankAccount
{
    public int account;
    public int password;
    public int balance;
    public int overdraftlimit;
    
    public LocalBankAccount(int a, int p)
    {
        account = a;
        password = p;
        balance = 0;
        overdraftlimit = 0;
    }

    public LocalBankAccount(int a, int p, int b, int o)
    {
        account = a;
        password = p;
        balance = b;
        overdraftlimit = o;
    }
}
