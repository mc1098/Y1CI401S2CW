
package ATMFrame;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * An {@link Event} representing an action performed on the ATM. This event is 
 * a base abstract class for more specific events.
 * 
 * It is recommended that any handling of an ATMEvent is done on the specific 
 * event type.
 * 
 * @see Event
 * @see EventType
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public abstract class ATMEvent extends Event
{
    public static final EventType<ATMEvent> ATM_EVENT_TYPE = new EventType(ANY);
    
    public ATMEvent(EventType<? extends Event> eventType)
    {
        super(eventType);
    }
    
    public abstract void invokeHandler(ATMEventHandler handler);
}
