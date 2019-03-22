package ATMFrame;


/**
 * An interface to be used by UI of applications using the ATM Frame, for directing
 * the display of different {@link Screenable} items.
 * 
 * The ATM interface allows developers to choose when and what type of Ad can be 
 * played at any time during the runtime of the ATM, the actual specific ads being
 * played are based on their respective relative folder under the advertising directory.
 * 
 * The ATM Frame will display menu buttons and number pad keys - it also has a
 * central screen which displays Screenable objects.
 * 
 * @see Screenable
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface ATM
{
    /**
     * Load ads from file to the ATM screen, which will continue to play until 
     * a user interacts with the ATM. Once  the user interacts with the ATM the 
     * first {@link Screenable} object is  loaded.
     * 
     * Implementations of this method will perform this without blocking the 
     * main UI thread. Usage of this method can therefore be called without the 
     * need of another thread.
     * 
     * @see Screenable
     * @see #processingAd() 
     * 
     * 
     */
    public void idleAd();
    
    /**
     * Load ads from file to the ATM screen, which will 
     * continue to play until another ATM method is called to change the 
     * current {@link Screenable}.
     * 
     * The main difference between this method and {@link #idleAd()} is that 
     * this method will ignore inputs made by the user and it is down to the 
     * user of this method to change the current Screenable object.
     * 
     * Implementations of this method will perform this without blocking the 
     * main UI thread. Usage of this method can therefore be called without the 
     * need of another thread.
     * 
     * @see Screenable
     * @see #idleAd()  
     * 
     */
    public void processingAd();
    
    /**
     * Load the first {@link Screenable} object from it's history, 
     * this should be the login screen.
     * 
     * Implementations are required to maintain the safety of this call as it 
     * should not result in an exception or error. However implementations do 
     * not have to enforce that the login screen is the first screen.
     * 
     * @see Screenable
     * @see #loadPreviousScreen() 
     * @see #loadToScreen(ATMFrame.Screenable) 
     * 
     */
    public void loadFirstScreen();
    
    /**
     * Load the previous applicable {@link Screenable} to the ATM screen. 
     * Screenable implementation define if they are applicable, as such adverts
     * and result pages are not applicable. 
     * 
     * Implementations of this method will only load previous Screenable objects
     * within that session, therefore new users are unable to load previous
     * users screens. It therfore expected behaviour that calling this method
     * would quickly become synonymous with the method {@link #loadFirstScreen()}.
     * 
     * As with {@link #loadFirstScreen()} method implementations are required to 
     * maintain the safety of this call as it should not result in an exception 
     * or error.
     * 
     * 
     * @see Screenable
     * @see #loadFirstScreen() 
     * @see #loadToScreen(ATMFrame.Screenable) 
     * 
     */
    public void loadPreviousScreen();
    
    /**
     * Load the given {@link Screenable} object to the ATM screen. 
     * 
     * The previous Screenable is checked whether it is applicable for the method
     * {@link #loadPreviousScreen()}. 
     * 
     * @param screenable Screenable object to be loaded to the ATM screen.
     * 
     * @see Screenable
     * @see #loadFirstScreen() 
     * @see #loadPreviousScreen() 
     * 
     */
    public void loadToScreen(Screenable screenable);
}
