package PaymentProcessing;

import FinancialMessaging.WithdrawalResponse;
import FinancialMessaging.TransactionsRequest;
import FinancialMessaging.TransactionsResponse;
import FinancialMessaging.DepositResponse;
import FinancialMessaging.DepositRequest;
import FinancialMessaging.BalanceResponse;
import FinancialMessaging.WithdrawalRequest;
import FinancialMessaging.BalanceRequest;
import Database.ConnectionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import CardAssociation.CardAssociation;
import CardAssociation.CardAssociationRepository;
import CardAssociation.UnrecognizedCardNumber;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class CardPaymentProcessor implements PaymentProcessor
{
    private final static Logger LOGGER = Logger.getLogger(CardPaymentProcessor.class.getName());
    
    private final CardAssociationRepository repository;
    private final BankingResultHandler out;
    
    public CardPaymentProcessor(CardAssociationRepository repository, 
            BankingResultHandler out)
    {
        this.repository = repository;
        this.out = out;
    }
    
    @Override
    public void accept(BalanceRequest request)
    {
        try 
        {
            CardAssociation ca = repository.getById(request.cardTypeId);
            out.receive(ca.processRequest(request));
        } catch(ConnectionException ex)
        {
            LOGGER.log(Level.WARNING, null, ex);
            out.receive(BalanceResponse.UNABLE_TO_GET_INFO);
        }
        catch(UnrecognizedCardNumber ex)
        {
            LOGGER.log(Level.INFO, "Unsupported card number used.", ex);
            out.receive(BalanceResponse.INVALID_ACCOUNT_INFO);
        }
            
    }

    @Override
    public void accept(TransactionsRequest request)
    {
        try 
        {
            CardAssociation ca = repository.getById(request.cardTypeId);
            out.receive(ca.processRequest(request));
        } catch(ConnectionException ex)
        {
            LOGGER.log(Level.WARNING, null, ex);
            out.receive(TransactionsResponse.UNABLE_TO_GET_INFO);
        }catch(UnrecognizedCardNumber ex)
        {
            LOGGER.log(Level.INFO, "Unsupported card number used.", ex);
            out.receive(TransactionsResponse.INVALID_ACCOUNT_INFO);
        }
    }

    @Override
    public void accept(WithdrawalRequest request)
    {
        try 
        {
            CardAssociation ca = repository.getById(request.cardTypeId);
            out.receive(ca.processRequest(request));
        } catch(ConnectionException ex)
        {
            LOGGER.log(Level.WARNING, null, ex);
            out.receive(WithdrawalResponse.UNABLE_TO_GET_INFO);
        }catch(UnrecognizedCardNumber ex)
        {
            LOGGER.log(Level.INFO, "Unsupported card number used.", ex);
            out.receive(WithdrawalResponse.INVALID_ACCOUNT_INFO);
        }
    }

    @Override
    public void accept(DepositRequest request)
    {
        try 
        {
            CardAssociation ca = repository.getById(request.cardTypeId);
            out.receive(ca.processRequest(request));
        } catch(ConnectionException ex)
        {
            LOGGER.log(Level.WARNING, null, ex);
            out.receive(DepositResponse.UNABLE_TO_GET_INFO);
        }catch(UnrecognizedCardNumber ex)
        {
            LOGGER.log(Level.INFO, "Unsupported card number used.", ex);
            out.receive(DepositResponse.INVALID_ACCOUNT_INFO);
        }
    }
    
}
