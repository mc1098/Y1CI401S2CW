
package ATMFrame;

/**
 * MenuButton specifically for use with the {@link ATM} and to be coupled with 
 * the use of the {@link ATMEventHandler}. 
 * 
 * These buttons on action will fire a {@link MenuEvent} with the given 
 * {@link ATMKeyCode} this button was initialised with.
 * 
 * @see MenuEvent
 * @see ATMKeyCode
 * @see ATMEventHandler
 * @see ATM
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class MenuButton extends ATMButton
{
    
    public MenuButton(ATMKeyCode keyCode, String text)
    {
        super(keyCode, text);
        setOnAction((e) -> this.fireEvent(new MenuEvent(keyCode)));
    }
    
    public MenuButton(ATMKeyCode keyCode)
    {
        this(keyCode, "");
    }
    
}
