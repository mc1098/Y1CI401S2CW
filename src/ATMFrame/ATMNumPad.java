package ATMFrame;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * NumPad Pane with {@link NumberPadButton} for use with the {@link ATM}.
 * 
 * @see NumberPadButton
 * @see ATM
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class ATMNumPad extends GridPane
{
    
    public ATMNumPad()
    {
        this.setId("atm-numpad");
        
        //Set first row [1, 2, 3, ENTER]
        super.addRow(0, new NumberPadButton(ATMKeyCode.NUM_PAD_ONE, "1"), 
                new NumberPadButton(ATMKeyCode.NUM_PAD_TWO, "2"), 
                new NumberPadButton(ATMKeyCode.NUM_PAD_THREE, "3"));
        Button enter = new NumberPadButton(ATMKeyCode.ENTER, "ENTER");
        enter.setId("atm-enter");
        super.add(enter, 3, 0);
        
        //Set second row [4, 5, 6, CLEAR]
        super.addRow(1, new NumberPadButton(ATMKeyCode.NUM_PAD_FOUR, "4"), 
                new NumberPadButton(ATMKeyCode.NUM_PAD_FIVE, "5"), 
                new NumberPadButton(ATMKeyCode.NUM_PAD_SIX, "6"));
        Button clr = new NumberPadButton(ATMKeyCode.CLEAR, "CLEAR");
        clr.setId("atm-clear");
        super.add(clr, 3, 1);
        
        //Set third row [7, 8, 9, CANCEL]
        super.addRow(2, new NumberPadButton(ATMKeyCode.NUM_PAD_SEVEN, "7"), 
                new NumberPadButton(ATMKeyCode.NUM_PAD_EIGHT, "8"), 
                new NumberPadButton(ATMKeyCode.NUM_PAD_NINE, "9"));
        Button cancel = new NumberPadButton(ATMKeyCode.CANCEL, "CANCEL");
        cancel.setId("atm-cancel");
        super.add(cancel, 3, 2);
        //Set fourth row - Zero button expanded to three columns so it's set in 
        //the middle.
        super.add(new NumberPadButton(ATMKeyCode.NUM_PAD_ZERO, "0"), 1, 3, 2, 1);
    }
    
    /**
     * Sets the given handler to all of the {@link NumberPadButton} objects.
     * @param handler handler to set to all the NumberPadButtons.
     * 
     * @see ATMEventHandler
     * @see NumPadEvent
     * @see NumberPadButton
     * @see #removeNumPadHandler(ATMFrame.ATMEventHandler) 
     */
    public void addNumPadHandler(ATMEventHandler<NumPadEvent> handler)
    {
        super.getChildren().forEach((c)->((ATMButton)c)
                .addEventHandler(NumPadEvent.NUM_PAD_EVENT, handler));
    }
    
    /**
     * Removes the given handler from all of the {@link NumberPadButton} objects.
     * @param handler handler to remove from all the NumberPadButtons.
     * 
     * @see ATMEventHandler
     * @see NumPadEvent
     * @see NumberPadButton
     * @see #addNumPadHandler(ATMFrame.ATMEventHandler) 
     */
    public void removeNumPadHandler(ATMEventHandler<NumPadEvent> handler)
    {
        super.getChildren().forEach((c) -> ((ATMButton)c)
                .removeEventHandler(NumPadEvent.NUM_PAD_EVENT, handler));
    }
    
}
