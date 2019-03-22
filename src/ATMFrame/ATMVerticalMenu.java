package ATMFrame;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

/**
 * Vertical Menu Pane with three {@link MenuButton} objects for the {@link ATM}
 * 
 * @see MenuButton
 * @see ATM
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class ATMVerticalMenu extends Parent
{
    
    private final VBox root;
    private final MenuButton button1;
    private final MenuButton button2;
    private final MenuButton button3;
    
    public ATMVerticalMenu(boolean right, MenuButton b1, MenuButton b2, 
            MenuButton b3)
    {
        this.button1 = b1;
        this.button2 = b2;
        this.button3 = b3;
        this.root = new VBox(b1, b2, b3);
        super.getChildren().add(root);
        root.getStyleClass().add("atm-menu");
        setStyleClass(right);
    }
    
    /**
     * Sets the CSS class "right" or "left" to the elements in this UI object, 
     * the former is true when the given boolean is true and the latter when 
     * false.
     * 
     * @param right 
     */
    private void setStyleClass(boolean right)
    {
        String cssClass = right ? "right" : "left";
        
        root.getStyleClass().add(cssClass);
        button1.getStyleClass().add(cssClass);
        button2.getStyleClass().add(cssClass);
        button3.getStyleClass().add(cssClass);
    }
    
    /**
     * Sets the handler to each {@link MenuButton} on this menu.
     * @param handler to be set to each MenuButton on this menu.
     * 
     * @see ATMEventHandler
     * @see MenuEvent
     * @see MenuButton
     * @see #removeMenuHandler(ATMFrame.ATMEventHandler) 
     */
    public void addMenuHandler(ATMEventHandler<MenuEvent> handler)
    {
        button1.addEventHandler(MenuEvent.MENU_EVENT, handler);
        button2.addEventHandler(MenuEvent.MENU_EVENT, handler);
        button3.addEventHandler(MenuEvent.MENU_EVENT, handler);
    }
    
    /**
     * Remove the handler from each {@link MenuButton} on this menu.
     * @param handler to be removed from each MenuButton on this menu.
     * 
     * @see ATMEventHandler
     * @see MenuEvent
     * @see MenuButton
     * @see #addMenuHandler(ATMFrame.ATMEventHandler) 
     */
    public void removeMenuHandler(ATMEventHandler<MenuEvent> handler)
    {
        button1.removeEventHandler(MenuEvent.MENU_EVENT, handler);
        button2.removeEventHandler(MenuEvent.MENU_EVENT, handler);
        button3.removeEventHandler(MenuEvent.MENU_EVENT, handler);
    }
    
    
}
