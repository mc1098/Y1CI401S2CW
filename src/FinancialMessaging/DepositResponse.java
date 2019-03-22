package FinancialMessaging;

/**
 *
 * Response object returned from a Deposit request. 
 * 
 * This object will contain whether the request was successful and then respective
 * data in either case.
 * 
 * When the request is successful then the deposit amount is confirmed in this response.
 * 
 * When the request is unsuccessful then the deposit amount will be zero, but
 * one of the error flags will be true indicating what caused the request to be
 * denied.
 * 
 * @see BankingResultHandler
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class DepositResponse implements BankingResponse
{
    public static DepositResponse INVALID_ACCOUNT_INFO = new DepositResponse(0, true, false);
    public static DepositResponse UNABLE_TO_GET_INFO = new DepositResponse(0, false, true);
    
    public static DepositResponse success(int deposit)
    {
        return new DepositResponse(deposit, false, false);
    }
    
    public final int deposit;
    public final boolean invalidAccountInfo;
    public final boolean unableToGetBankingInfo;
    
    
    public DepositResponse(int deposit, boolean invalidAccountInfo, 
            boolean unableToGetBankingInfo)
    {
        this.deposit = deposit;
        this.invalidAccountInfo = invalidAccountInfo;
        this.unableToGetBankingInfo = unableToGetBankingInfo;
    }
    
    public boolean sucess() 
    {
        return !invalidAccountInfo && !unableToGetBankingInfo;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof DepositResponse))
            return false;
        
        DepositResponse dr = (DepositResponse) o;
        
        return (this.deposit == dr.deposit && 
                this.invalidAccountInfo == dr.invalidAccountInfo && 
                this.unableToGetBankingInfo == dr.unableToGetBankingInfo);
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 37 * hash + this.deposit;
        hash = 37 * hash + (this.invalidAccountInfo ? 1 : 0);
        hash = 37 * hash + (this.unableToGetBankingInfo ? 1 : 0);
        return hash;
    }
}
