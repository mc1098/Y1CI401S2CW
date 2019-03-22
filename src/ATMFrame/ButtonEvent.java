
package ATMFrame;

import javafx.event.EventType;

/**
 * An Event representing a button action performed on the ATM. 

 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class ButtonEvent extends ATMEvent
{
    
    public static final EventType<ButtonEvent> BUTTON_EVENT = new EventType<ButtonEvent>(ATM_EVENT_TYPE, "ButtonEvent");

    private final ATMKeyCode keyCode;
    
    public ButtonEvent(EventType<? extends ATMEvent> type, ATMKeyCode keyCode)
    {
        super(type);
        this.keyCode = keyCode;
    }
    
    public ButtonEvent(ATMKeyCode keyCode)
    {
        super(BUTTON_EVENT);
        this.keyCode = keyCode;
    }
    
    public ATMKeyCode getKeyCode() {return keyCode;};

    @Override
    public void invokeHandler(ATMEventHandler handler)
    {
        handler.handle(this);
    }
    
}
