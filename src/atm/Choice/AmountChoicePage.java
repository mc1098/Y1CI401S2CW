package atm.Choice;

import ATMFrame.ATMEventHandler;
import ATMFrame.NumPadEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class AmountChoicePage extends ChoicePage<AmountChoiceController>
{
    private final TextField amountField;
    
    public AmountChoicePage(AmountChoiceController controller)
    {
        super(controller);
        this.setId("amount-choice-view");
        this.amountField = new TextField();
        amountField.setTextFormatter(new TextFormatter(new IntegerStringConverter()));
        amountField.setPromptText("SPECIFY AMOUNT (£500 MAX)");
        amountField.setId("amount-field");
        super.setSpacing(10);
        super.getChildren().add(amountField);
        this.numPadHandler = new AmountNumPadHandler();
    }
    
    @Override
    public boolean isBackable() {return false;}
    
    class AmountNumPadHandler implements ATMEventHandler<NumPadEvent> 
    {

        @Override
        public void handle(NumPadEvent e)
        {
            switch(e.getKeyCode())
            {
                case ENTER: 
                    int amount = tryParse(amountField);
                    if(amount > 0 && amount <= 500)
                        controller.customAmount(amount);
                    else 
                        amountField.clear();
                    break;
                    
                case CANCEL: 
                    if(amountField.getLength() == 0)
                        controller.back();
                    else
                        amountField.clear();
                    break;
                    
                case CLEAR: 
                    if(amountField.getLength() > 0)
                        amountField.setText(amountField.getText(0, amountField.getLength()-1));
                    break;
                    
                default: 
                    amountField.appendText(String.valueOf(e.getKeyCode().ordinal()));
            }
        }
        
        private int tryParse(TextField tf)
        {
            if(tf.getLength() <= 0)
                return -1;
            return Integer.parseInt(tf.getText());
        }
        
    }
    
}
