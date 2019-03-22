package ATMFrame;


/**
 * This interface represents the requirements needed from a UI object in 
 * order to be loaded into the {@link ATM} screen, it is required that all 
 * implementing classes also extend {@link Node} or subclass of Node.
 * 
 * Screenable is kept to an interface despite requiring a Node implementation, 
 * so that uses of the ATM can choose the most appropriate class to implement
 * based on their UI requirements. 
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface Screenable
{
    /**
     * Defines if this page should be applicable for being reloaded. 
     * 
     * It is recommended to only return true if the page provides options 
     * and no sensitive information can be seen. 
     * 
     * As mentioned in the {@link ATM} documentation Screenable are only 
     * reloaded from the current ATM session, therefore other users will 
     * not be able to reload the page even if this method returns true.
     * 
     * @return true - if this page is applicable to being reloaded.
     * 
     * @see ATM
     * @see ATM#loadPreviousScreen() 
     */
    public abstract boolean isBackable(); 
    
    /**
     * Returns the {@link ATMEventHandler} which is called when a {@link MenuEvent}
     * occurs on the {@link ATM}.
     * 
     * Screenable handlers are only ever called when the current Screenable
     * is loaded into the ATM screen.
     * 
     * The handler provided will be called in the event that a menu button is 
     * pressed, despite MenuEvent using {@link ATMKeyCode} which defines both menu
     * and number pad keys only menu codes would be used.
     * 
     * It is not recommended that implementations provide handlers that are
     * recursive or require a lot of processing time as this handling of events 
     * is done with the main UI thread.
     * 
     * 
     * @return the {@link ATMEventHandler} which is called when a {@link MenuEvent}
     * occurs on the {@link ATM}.
     * 
     * @see ATMEventHandler
     * @see MenuEvent 
     * @see ATM 
     * @see ATMKeyCode
     * @see #getNumPadHandler() 
     * 
     */
    public abstract ATMEventHandler<MenuEvent> getMenuHandler();
    
    /**
     * Returns the {@link ATMEventHandler} which is called when a {@link NumPadEvent}
     * occurs on the {@link ATM}.
     * 
     * Screenable handlers are only ever called when the current Screenable
     * is loaded into the ATM screen.
     * 
     * The handler provided will be called in the event that a number pad key is 
     * pressed, despite NumPadEvent using {@link ATMKeyCode} which defines both menu
     * and number pad keys only number pad key codes would be used.
     * 
     * It is not recommended that implementations provide handlers that are
     * recursive or require a lot of processing time as this handling of events 
     * is done with the main UI thread.
     * 
     * 
     * @return the {@link ATMEventHandler} which is called when a {@link NumPadEvent}
     * occurs on the {@link ATM}.
     * 
     * @see ATMEventHandler
     * @see NumPadEvent 
     * @see ATM 
     * @see ATMKeyCode
     * @see #getMenuHandler() 
     */
    public abstract ATMEventHandler<NumPadEvent> getNumPadHandler();
}
