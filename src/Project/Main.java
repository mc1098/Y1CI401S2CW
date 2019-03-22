package Project;

import PaymentProcessing.CardPaymentProcessor;
import PaymentProcessing.AtmBankingComponent;
import ATMFrame.ATMFrame;
import CardAssociation.LocalCardBankRepository;
import PaymentProcessing.BankingComponent;
import Database.ConnectionException;
import CardAssociation.DebitCardAssociationRepository;
import atm.Choice.AmountChoiceController;
import atm.Choice.AmountChoicePage;
import atm.Login.LoginController;
import atm.Login.LoginPage;
import atm.Choice.ServiceChoiceController;
import atm.Choice.ChoicePage;
import atm.Choice.DepositChoiceController;
import atm.Choice.WithdrawalChoiceController;
import atm.ResultPages.ResultPage;
import atm.ResultPages.ResultController;
import atm.ResultPages.SimpleResultPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import PaymentProcessing.PaymentProcessor;
import CardAssociation.CardAssociationRepository;
import CardAssociation.CardType;
import Database.DerbySQLDatabase;
import Database.SQLDatabaseComponent;
import atm.ResultPages.TransactionResultPage;
import java.util.List;
import org.apache.derby.drda.NetworkServerControl;
import PaymentProcessing.BankingResultHandler;
import CardAssociation.CardBankAccountRepository;
import atm.AccountInfo;
import atm.Presenter;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class Main extends Application
{
    
    @Override
    public void start(Stage primaryStage) throws ConnectionException, Exception
    {
        
        //start network for derby connection.
        NetworkServerControl nsc = new NetworkServerControl();
        nsc.start(null);
        SQLDatabaseComponent database = new DerbySQLDatabase();
        
        //setup ATMFrame - local paths for ads
        ATMFrame frame = new ATMFrame("ads/attract", "ads/processing");
        
        //setup all repository
        CardBankAccountRepository bar = new LocalCardBankRepository(database);
        CardAssociationRepository repository = new DebitCardAssociationRepository(database, bar);
        
        //result and response set up.
        ResultController src = new ResultController(frame);
        ResultPage resultPage = new SimpleResultPage(src);
        TransactionResultPage trp = new TransactionResultPage(src);
        BankingResultHandler presenter = new Presenter(frame, resultPage, trp);
        
        //request set up
        PaymentProcessor bankingInteractor = new CardPaymentProcessor(repository, presenter);
        BankingComponent component = new AtmBankingComponent(bankingInteractor, "Brighton ATM");
        
        //Account model and choice controller implementation setup.
        AccountInfo model = new AccountInfo();
        AmountChoiceController dcc = new DepositChoiceController(frame, model, component);
        AmountChoicePage dcp = new AmountChoicePage(dcc);
        AmountChoiceController acc = new WithdrawalChoiceController(frame, model, component);
        AmountChoicePage acp = new AmountChoicePage(acc);
        ServiceChoiceController scc = new ServiceChoiceController(frame, model, component, acp, dcp);
        ChoicePage scp = new ChoicePage(scc);
        
        
        //get all cardTypes for the login page.
        List<CardType> cardTypes = repository.getAll();
        
        //login page setup.
        LoginController controller = new LoginController(model, frame, scp);
        LoginPage login = new LoginPage(controller, cardTypes);
        
        //set login as first screen
        frame.loadToScreen(login);
        //load idle ad to screen
        frame.idleAd();
        
        //setup scene for JavaFx
        Scene scene = new Scene(frame);
        scene.getStylesheets().add("atm.css");
        
        //set title for window
        primaryStage.setTitle("ATM");
        primaryStage.setScene(scene);
        //fixed window size
        primaryStage.setResizable(false);
        primaryStage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
