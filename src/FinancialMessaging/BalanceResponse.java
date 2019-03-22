package FinancialMessaging;

/**
 * Response object returned from a Balance request. 
 * 
 * This object will contain whether the request was successful and then respective
 * data in either case.
 * 
 * When the request is successful then the balance amount is contained in this response.
 * 
 * When the request is unsuccessful then the balance amount will be zero, but
 * one of the error flags will be true indicating what caused the request to be
 * denied.
 * 
 * @see BankingResultHandler
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class BalanceResponse implements BankingResponse
{
    public static BalanceResponse INVALID_ACCOUNT_INFO = new BalanceResponse(0, true, false);
    public static BalanceResponse UNABLE_TO_GET_INFO = new BalanceResponse(0, false, true);
    
    public static BalanceResponse success(int balance)
    {
        return new BalanceResponse(balance, false, false);
    }
    
    public final boolean invalidAccountInfo;
    public final boolean unableToGetBankingInfo;
    public final int amount;
    
    public BalanceResponse(int amount, boolean invalidAccountInfo, 
            boolean unableToGetBankingInfo)
    {
        this.invalidAccountInfo = invalidAccountInfo;
        this.unableToGetBankingInfo = unableToGetBankingInfo;
        this.amount = amount;
    }
    
    public boolean sucess() {return !invalidAccountInfo && !unableToGetBankingInfo;}
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof BalanceResponse))
            return false;
        
        BalanceResponse br = (BalanceResponse) o;
        
        return (this.amount == br.amount && 
                this.invalidAccountInfo == br.invalidAccountInfo && 
                this.unableToGetBankingInfo == br.unableToGetBankingInfo);
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 43 * hash + (this.invalidAccountInfo ? 1 : 0);
        hash = 43 * hash + (this.unableToGetBankingInfo ? 1 : 0);
        hash = 43 * hash + this.amount;
        return hash;
    }
    
}
