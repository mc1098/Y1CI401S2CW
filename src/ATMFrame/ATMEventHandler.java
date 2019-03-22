
package ATMFrame;

import javafx.event.EventHandler;

/**
 * Handler for events of ATM specific actions.
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface ATMEventHandler<T extends ATMEvent> extends EventHandler<T>
{
    @Override
    public void handle(T event);
    
}
