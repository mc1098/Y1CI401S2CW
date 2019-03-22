
package ATMFrame;

import javafx.event.EventType;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class NumPadEvent extends ButtonEvent
{
    public static final EventType<NumPadEvent> NUM_PAD_EVENT = new EventType<NumPadEvent>(BUTTON_EVENT, "NumPadEvent");

    private final ATMKeyCode keyCode;
    
    public NumPadEvent(ATMKeyCode keyCode)
    {
        super(NUM_PAD_EVENT, keyCode);
        this.keyCode = keyCode;
    }

    @Override
    public void invokeHandler(ATMEventHandler handler)
    {
        handler.handle(this);
    }
}
