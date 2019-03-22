
package FinancialMessaging;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class WithdrawalRequest implements BankingRequest
{
    public final int accoundNumber;
    public final long cardTypeId;
    public final int accountPass;
    public final String location;
    public final int amount;
    
    public WithdrawalRequest(int accNum, long cardTypeId, int accPass, 
            String location, int amount)
    {
        this.accoundNumber = accNum;
        this.cardTypeId = cardTypeId;
        this.accountPass = accPass;
        this.location = location;
        this.amount = amount;
    }
}
