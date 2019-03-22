package FinancialMessaging;

/**
 *
 * Response object returned from a withdrawal request. 
 * 
 * This object will contain whether the request was successful and the respective
 * data in either case.
 * 
 * When the request is successful then the withdrawn amount is contained in this 
 * response.
 * 
 * When the request is unsuccessful then the withdrawn amount will be zero, but
 * one of the error flags will be true indicating what caused the request to be
 * denied.
 * 
 * @see BankingResultHandler
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class WithdrawalResponse
{
    // Convenience static members/methods
    public static WithdrawalResponse INVALID_OR_FRAUD = new WithdrawalResponse(0, true, false, false, false);
    public static WithdrawalResponse INSUFFICIENT_FUNDS = new WithdrawalResponse(0, false, true, false, false);
    public static WithdrawalResponse INVALID_ACCOUNT_INFO = new WithdrawalResponse(0, false, false, true, false);
    public static WithdrawalResponse UNABLE_TO_GET_INFO = new WithdrawalResponse(0, false, false, false, true);
    public static WithdrawalResponse success(int withdrawn)
    {
        return new WithdrawalResponse(withdrawn, false, false, false, false);
    }
    
    public final int withdrawn;
    public final boolean invalidOrFraudulentRequest;
    public final boolean insufficientFunds;
    public final boolean invalidAccountInfo;
    public final boolean unableToGetBankingInfo;
    
    
    public WithdrawalResponse(int withdrawn, boolean invalidOrFraudulentRequest, 
            boolean insufficientFunds, boolean invalidAccountInfo, 
            boolean unableToGetBankingInfo)
    {
        this.withdrawn = withdrawn;
        this.invalidOrFraudulentRequest = invalidOrFraudulentRequest;
        this.insufficientFunds = insufficientFunds;
        this.invalidAccountInfo = invalidAccountInfo;
        this.unableToGetBankingInfo = unableToGetBankingInfo;
    }
    
    public boolean sucess() 
    {
        return !invalidOrFraudulentRequest && !invalidAccountInfo && 
                !unableToGetBankingInfo && !insufficientFunds;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof WithdrawalResponse))
            return false;
        
        WithdrawalResponse wr = (WithdrawalResponse) o;
        
        return (this.withdrawn == wr.withdrawn && 
                this.insufficientFunds == wr.insufficientFunds &&
                this.invalidOrFraudulentRequest == wr.invalidOrFraudulentRequest && 
                this.invalidAccountInfo == wr.invalidAccountInfo && 
                this.unableToGetBankingInfo == wr.unableToGetBankingInfo);
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 89 * hash + this.withdrawn;
        hash = 89 * hash + (this.invalidOrFraudulentRequest ? 1 : 0);
        hash = 89 * hash + (this.insufficientFunds ? 1 : 0);
        hash = 89 * hash + (this.invalidAccountInfo ? 1 : 0);
        hash = 89 * hash + (this.unableToGetBankingInfo ? 1 : 0);
        return hash;
    }
}
