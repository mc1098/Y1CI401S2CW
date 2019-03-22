
package ATMFrame;

import javafx.event.EventType;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class MenuEvent extends ButtonEvent
{
    
    public static final EventType<MenuEvent> MENU_EVENT = new EventType<MenuEvent>(BUTTON_EVENT, "MenuEvent");
    
    public MenuEvent(ATMKeyCode keyCode)
    {
        super(MENU_EVENT, keyCode);
    }
    
}
