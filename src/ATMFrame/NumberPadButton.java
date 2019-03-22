package ATMFrame;

/**
 * NumberPadButton specifically for use with the {@link ATM} and to be coupled 
 * with the use of the {@link ATMEventHandler}. 
 * 
 * These buttons on action will fire a {@link NumPadEvent} with the given 
 * {@link ATMKeyCode} this button was initialised with.
 * 
 * @see NumPadEvent
 * @see ATMKeyCode
 * @see ATMEventHandler
 * @see ATM
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class NumberPadButton extends ATMButton
{
    
    public NumberPadButton(ATMKeyCode code, String text)
    {
        super(code, text);
        setOnAction((e) -> this.fireEvent(new NumPadEvent(code)));
    }
}
