package atm.Choice;

/**
 * Controller to be used by the UI Page in order to read the available option
 * names as well as call the appropriate option when a selection is made by 
 * the user.
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface ChoiceController
{
    /**
     * Returns the name of the menu option n, where n is the number provided.
     * 
     * If the number is not within the menu option range then an empty String
     * is returned.
     * @param n Number of the option to return the name of.
     * @return Retrieves the name of the menu option n, where n is the number 
     * provided.
     * 
     */
    public String getMenuOption(int n);
    
    /**
     * Performs the option associated with this option number.
     */
    public void onOption1();
    
    /**
     * Performs the option associated with this option number.
     */
    public void onOption2();
    
    /**
     * Performs the option associated with this option number.
     */
    public void onOption3();
    
    /**
     * Performs the option associated with this option number.
     */
    public void onOption4();
    
    /**
     * Performs the option associated with this option number.
     */
    public void onOption5();
    
    /**
     * Performs the option associated with this option number.
     */
    public void onOption6();
    
    /**
     * Call to load the previous available page, if no page is available then 
     * the first page will be loaded instead.
     */
    public void back();
}
