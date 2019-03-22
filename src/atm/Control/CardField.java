package atm.Control;

import CardAssociation.CardType;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

/**
 * Specialised {@link TextField} for Card/Account Numbers. 
 * 
 * The field only accepts integer inputs and any attempt at other characters 
 * will cause the input to be erased.
 * 
 * This field validates against an accepted list of {@link CardType}.
 * It will also check that the input is the correct length. Both of these checks
 * are bound to the {@link ObservableBooleanValue} which can be checked through
 * the {@link #validationObservable()}.
 * 
 * This CardField does not perform the Luhn algorithm on the input for simplicity.
 * 
 * 
 * @see TextField
 * @see CardType
 * @see ObservableBooleanValue
 * 
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class CardField extends TextField
{
    private final List<CardType> cardTypes;
    private final ObjectProperty<CardType> cardType;
    private final BooleanProperty digitLengthCheck;
    private final BooleanProperty validProperty;
    
    /**
     * Initialises the CardField to accept the given {@link CardType} in the list.
     * 
     * If an empty list is passed then the CardField method {@link #validationObservable()}
     * will always return a false {@link ObservableBooleanValue}.
     * @param cardTypes Accepted CardTypes to validate against.
     * 
     * @see CardType
     * @see #validationObservable() 
     * @see ObservableBooleanValue
     * 
     */
    public CardField(List<CardType> cardTypes)
    {
        super.setPromptText("ACCOUNT NUMBER");
        //IntegerStringConverter to conform input to Integers only.
        super.setTextFormatter(new TextFormatter(new IntegerStringConverter()));
        
        this.cardTypes = cardTypes;
        this.cardType = new SimpleObjectProperty<>();
        this.digitLengthCheck = new SimpleBooleanProperty();
        this.validProperty = new SimpleBooleanProperty();
        
        validation(); 
    }
    
    /**
     * Method for setting up Property bindings.
     */
    private void validation()
    {
        //True == (Field.getLength() == 8)
        digitLengthCheck.bind(super.textProperty().length().isEqualTo(8));
        
        //Binds the validProperty against whether a cardType is set and if the 
        //text length is correct.
        validProperty.bind(cardType.isNotNull().and(digitLengthCheck));
        
        //Listener to set the cardType if an appropriate input has been entered.
        super.textProperty().addListener((ob, o, n) -> 
        {
            cardType.set(null);
            //If no text don't do anything else.
            if(ob.getValue().length() > 0)
            {
                cardTypes.forEach((ct)-> 
                {
                    //Check each accepted CardType's prefixes against the input.
                    for(String s : ct.prefixes)
                        //When input is less characters check that it's the 
                        //start of the prefix else do the reverse.
                        if(ob.getValue().length() < s.length() ? 
                                s.startsWith(ob.getValue()) : 
                                ob.getValue().startsWith(s))
                            cardType.set(ct);
                });
                
            }
            
        });
    }
    
    /**
     * Returns an {@link ObservableBooleanValue} that is true when the input
     * length is equal to 8.
     * @return Retrieves an {@link ObservableBooleanValue} that is true when the input
     * length is equal to 8.
     * 
     * @see ObservableBooleanValue
     * @see #validationObservable() 
     * @see #cardTypeObservable()
     * 
     */
    public ObservableBooleanValue lengthValidationObservable() {return digitLengthCheck;}
    
    /**
     * Returns an {@link ObservableBooleanValue} that is true when the input 
     * is correctly formatted.
     * 
     * An input is considered correctly formatted when the below conditions are
     * met:
     * <ol>
     * <li> 
     *      {@link #lengthValidationObservable()} return contains a true value. 
     *      Which occurs when the character length of the input is equal to 8. 
     * </li>
     * <li>
     *      {@link #cardTypeObservable()} return contains a non null value. 
     *      This occurs when the input is valid and is in an accepted card format.
     * </li>
     * </ol>
     * 
     * @return Retrieves an {@link ObservableBooleanValue} that is true when the input 
     * is correctly formatted.
     * 
     * @see ObservableBooleanValue
     * @see #lengthValidationObservable() 
     * @see #cardTypeObservable() 
     * 
     */
    public ObservableBooleanValue validationObservable() {return validProperty;}
    
    /**
     * Returns an {@link ReadOnlyObjectProperty} that contains a {@link CardType}
     * when the input is formatted to match that CardType respectively.
     * @return Retrieves an {@link ReadOnlyObjectProperty} that contains a {@link CardType}
     * when the input is formatted to match that CardType respectively.
     */
    public ReadOnlyObjectProperty<CardType> cardTypeObservable() {return cardType;}
    
    @Override
    public void clear()
    {
        cardType.set(null);
        super.clear();
    }
    
}
