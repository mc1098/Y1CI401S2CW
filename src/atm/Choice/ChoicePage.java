package atm.Choice;

import ATMFrame.ATMEventHandler;
import ATMFrame.ATMKeyCode;
import ATMFrame.MenuEvent;
import ATMFrame.NumPadEvent;
import ATMFrame.Screenable;
import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class ChoicePage<T extends ChoiceController> extends VBox implements Screenable
{
    protected final T controller;
    
    private final Text leftOption1;
    private final Text leftOption2;
    private final Text leftOption3;
    private final Text rightOption1;
    private final Text rightOption2;
    private final Text rightOption3;
    
    protected ATMEventHandler<MenuEvent> menuHandler;
    protected ATMEventHandler<NumPadEvent> numPadHandler;
    
    public ChoicePage(T controller)
    {
        this.controller = controller;
        GridPane options = new GridPane();
        options.getStyleClass().add("choice-view");
        super.getChildren().add(options);
        
        this.leftOption1 = new Text(controller.getMenuOption(0));
        GridPane.setHalignment(leftOption1, HPos.LEFT);
        
        this.leftOption2 = new Text(controller.getMenuOption(1));
        GridPane.setHalignment(leftOption2, HPos.LEFT);
        
        this.leftOption3 = new Text(controller.getMenuOption(2));
        GridPane.setHalignment(leftOption3, HPos.LEFT);
        
        this.rightOption1 = new Text(controller.getMenuOption(3));
        GridPane.setHalignment(rightOption1, HPos.RIGHT);
        
        this.rightOption2 = new Text(controller.getMenuOption(4));
        GridPane.setHalignment(rightOption2, HPos.RIGHT);
        
        this.rightOption3 = new Text(controller.getMenuOption(5));
        GridPane.setHalignment(rightOption3, HPos.RIGHT);
        
        
        options.addColumn(0, leftOption1, leftOption2, leftOption3);
        options.addColumn(1, rightOption1, rightOption2, rightOption3);
        
        this.menuHandler = new ServiceChoiceMenuHandler();
        this.numPadHandler = new ServiceChoiceNumPadHandler();
    }

    @Override
    public boolean isBackable()
    {
        return true;
    }

    @Override
    public ATMEventHandler<MenuEvent> getMenuHandler()
    {
        return menuHandler;
    }

    @Override
    public ATMEventHandler<NumPadEvent> getNumPadHandler()
    {
        return numPadHandler;
    }
    
    
    class ServiceChoiceMenuHandler implements ATMEventHandler<MenuEvent>
    {

        @Override
        public void handle(MenuEvent e)
        {
            switch(e.getKeyCode())
            {
                case LEFT_BUTTON_1: 
                    controller.onOption1(); break;
                case LEFT_BUTTON_2: 
                    controller.onOption2(); break;
                case LEFT_BUTTON_3: 
                    controller.onOption3(); break;
                case RIGHT_BUTTON_1: 
                    controller.onOption4(); break;
                case RIGHT_BUTTON_2: 
                    controller.onOption5(); break;
                case RIGHT_BUTTON_3: 
                    controller.onOption6(); break;
                    
            }
        }
        
    }
    
    class ServiceChoiceNumPadHandler implements ATMEventHandler<NumPadEvent>
    {

        @Override
        public void handle(NumPadEvent event)
        {
            if(event.getKeyCode() == ATMKeyCode.CANCEL)
                controller.back();
        }
        
    }
    
    
}
