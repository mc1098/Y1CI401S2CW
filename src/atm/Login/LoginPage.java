package atm.Login;

import ATMFrame.ATMEventHandler;
import ATMFrame.MenuEvent;
import ATMFrame.NumPadEvent;
import ATMFrame.Screenable;
import CardAssociation.CardType;
import atm.Control.CardField;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

/**
 * JavaFX UI for the login ATM application for retrieving the account information
 * from the user.
 * 
 * This class is responsible for displaying the ATM account information request
 * page. 
 * The page contains: 
 * <ul>
 * <li>
 *      Welcome message.
 * </li>
 * <li>
 *      {@link CardField} control element for Account/Card Number input.
 * </li>
 * <li>
 *      {@link PasswordField} control for Account password input.
 * </li>
 * </ul>
 * 
 * The account number and password are checked for simple formatting correctness,
 * however they are not checked against the internal database until a request 
 * is made. This type of account verification is to mimic how ATMs process
 * correctly formatted information but with incorrectly account information 
 * (such as a password being entered incorrectly). 
 * 
 * @see Screenable
 * @see CardField
 * @see PasswordField
 * 
 * 
 * 
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class LoginPage extends VBox implements Screenable
{
    private final LoginController controller;
    private final TextArea message;
    
    private final ImageView cardImage;
    private final CardField accNumField;
    private final PasswordField accPassField;
    private final BooleanProperty accPassValidity;
    
    private final ATMEventHandler<MenuEvent> menuHandler;
    private final ATMEventHandler<NumPadEvent> numpadHandler;
    
    public LoginPage(LoginController controller, List<CardType> cardTypes)
    {
        //initialise controller and message
        this.controller = controller;
        this.message = new TextArea("Welcome to the ATM.\nPlease enter your "
                + "account information below.");
        
        //set message to not editable so the message cannot be changed by the user.
        message.setEditable(false);
        
        //CardField setup - takes the list of accepted card types.
        this.accNumField = new CardField(cardTypes);
        
        //Card Image view to show an image from a local file when an accept card 
        //type is recognized by the CardField
        this.cardImage = new ImageView();
        cardImage.setPreserveRatio(true);
        cardImage.setFitHeight(40);
        StackPane cardImageContainer = new StackPane(cardImage);
        
        //Password setup - Use of IntegerStringConverter in formatter to 
        //restrict user input to ints.
        this.accPassField = new PasswordField();
        accPassField.setTextFormatter(new TextFormatter(new IntegerStringConverter()));
        accPassField.setPromptText("PASSWORD");
        VBox accountInfoContainer = new VBox(accNumField, accPassField);
        
        //Custom boolean property to check password validatity (such as length)
        this.accPassValidity = new SimpleBooleanProperty(false);
        
        super.getChildren().addAll(message, cardImageContainer, accountInfoContainer);
        
        //CSS id for styling
        this.setId("login-view");
        
        //Initialise handlers required for Screenable interface - no actions for 
        //menuHandler so this defines an empty handler.
        this.menuHandler = (e) -> {};
        this.numpadHandler = new LoginNumPadEventHandler();
        
        //Setup validation and property binds.
        validation();
    }
    
    private void validation()
    {
        //If a valid Account Number is given disable the field so input goes to the PasswordField.
        accNumField.disableProperty().bind(accNumField.validationObservable());
        
        //If text has been entered but is not an accepted card type then change css;
        accNumField.textProperty().addListener((ob, o, n) -> 
        {
            if(ob.getValue().length() > 0 && accNumField.cardTypeObservable().isNull().get())
                accNumField.setStyle("-fx-border-color: white white red white;");
            else
                accNumField.setStyle("-fx-border-color: white");
        });
        
        
        //When a accepted card number is typed search for a respective card image.
        accNumField.cardTypeObservable().addListener((ob, o, n) -> 
        {
            if(ob.getValue() != null && accNumField.getLength() >= 4)
                {
                    Path path = Paths.get("card_images", String.format("%s.jpg", 
                            ob.getValue().card.toLowerCase()));
                    cardImage.setImage(new Image(path.toUri().toString()));
                }
            else cardImage.setImage(null);
        });
        
        //bind the custom validity property to true when the passwordField contains a 4 digit number.
        accPassField.textProperty().addListener((ob, o, n) -> 
        {
            accPassValidity.set(ob.getValue().matches("\\d{4}"));
        });
        
    }

    @Override
    public boolean isBackable() {return true;}

    @Override
    public ATMEventHandler<MenuEvent> getMenuHandler()
    {
        return menuHandler;
    }

    @Override
    public ATMEventHandler<NumPadEvent> getNumPadHandler()
    {
        return numpadHandler;
    }
    
    /**
     * Internal class allows access to private fields therefore maintaining
     * encapsulation and allows an organised place for all the NumPadEvent 
     * handling.
     */
    class LoginNumPadEventHandler implements ATMEventHandler<NumPadEvent>
    {

        @Override
        public void handle(NumPadEvent e)
        {
            switch(e.getKeyCode())
            {
                case ENTER: 
                    onEnter();
                    break;
                case CLEAR: 
                    onClear();
                    break;
                case CANCEL: 
                    onCancel();
                    break;
                default:
                    //Number keys - check which field the user is on before appending.
                    if(accNumField.isDisabled())
                        accPassField.appendText(String
                                .valueOf(e.getKeyCode().ordinal()));
                    else 
                        accNumField.appendText(String
                                .valueOf(e.getKeyCode().ordinal()));
                    
            }
        }
        
        /**
         * Confirm validation on entered inputs, if valid send to controller
         * to process. 
         * 
         * When the controller is used to process the input the fields are 
         * cleared for future use.
         */
        private void onEnter()
        {
            //validate before sending controller information
            if(accNumField.validationObservable().get() && accPassValidity.get())
            {
                controller.onEnter(getIntFromInput(accNumField), 
                        accNumField.cardTypeObservable().get().id,
                        getIntFromInput(accPassField));
                onCancel();
            }
            
        }
        
        /**
         * Removes the last character from the input field focused on.
         * 
         * Pressing clear when the accNumField is empty will do nothing.
         * Pressing clear when the accPassField is empty will actually
         * put the focus back onto the accNumField and remove the last character.
         * 
         */
        private void onClear()
        {
            if(accNumField.isDisabled())
                if(accPassField.getLength() == 0)
                    accNumField.setText(accNumField.getText(0, 
                            accNumField.getLength()-1));
                else
                    accPassField.setText(accPassField.getText(0, 
                            accPassField.getLength()-1));
            else 
                if(accNumField.getLength() > 0)
                    accNumField.setText(accNumField.getText(0, 
                            accNumField.getLength()-1));
        }
        
        /**
         * Clears the two input fields for reuse.
         */
        private void onCancel() 
        {
            accNumField.clear();
            accPassField.clear();
            accPassValidity.set(false);
        }
        
        /**
         * Convenience method for retrieving an integer from a TextField.
         * @param tf TextField to retrieve integer from.
         * @return Return integer from TextField
         */
        private int getIntFromInput(TextField tf)
        {
            return Integer.parseInt(tf.getText());
        }
        
    }
    
}
