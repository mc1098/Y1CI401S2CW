package atm.ResultPages;

import ATMFrame.ATMEventHandler;
import ATMFrame.MenuEvent;
import ATMFrame.NumPadEvent;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class SimpleResultPage extends VBox implements ResultPage
{
    private final TextArea text;
    private boolean result;
    
    private final ATMEventHandler<MenuEvent> menuHandler;
    private final ATMEventHandler<NumPadEvent> numPadHandler;
    
    public SimpleResultPage(ResultController controller)
    {
        this.text = new TextArea();
        text.setWrapText(true);
        text.setId("result-text");
        
        
        super.getChildren().add(text);
        super.setAlignment(Pos.CENTER);
        
        this.menuHandler = (e) -> {};
        this.numPadHandler = (e) -> 
        {
            switch(e.getKeyCode())
            {
                case ENTER: 
                    controller.onEnter(result); break;
                case CANCEL: 
                    controller.onCancel(); break;
                case CLEAR: 
                    controller.onClear(); break;
            }
        };
    }
    
    @Override
    public void update(boolean result, String message)
    {
        this.text.setText(message);
    }

    @Override
    public boolean isBackable() {return false;}

    @Override
    public ATMEventHandler<MenuEvent> getMenuHandler() {return menuHandler;}

    @Override
    public ATMEventHandler<NumPadEvent> getNumPadHandler() {return numPadHandler;}
    
}
