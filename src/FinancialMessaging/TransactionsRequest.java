package FinancialMessaging;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class TransactionsRequest implements BankingRequest
{
    public final int accoundNumber;
    public final long cardTypeId;
    public final int accountPass;
    
    public TransactionsRequest(int accNum, long cardTypeId, int accPass)
    {
        this.accoundNumber = accNum;
        this.cardTypeId = cardTypeId;
        this.accountPass = accPass;
    }
}
