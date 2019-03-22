
package PaymentProcessing;

import FinancialMessaging.TransactionsRequest;
import FinancialMessaging.DepositRequest;
import FinancialMessaging.WithdrawalRequest;
import FinancialMessaging.BalanceRequest;

/**
 * This interface outlines the responsibilities of a Payment Processor to accept
 * a request then to manage contacting the appropriate CardAssociation in order
 * to forward that request.
 * 
 * 
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface PaymentProcessor
{
    public void accept(BalanceRequest request);
    public void accept(TransactionsRequest request);
    public void accept(WithdrawalRequest request);
    public void accept(DepositRequest request);
}
