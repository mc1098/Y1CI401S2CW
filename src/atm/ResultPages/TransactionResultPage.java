package atm.ResultPages;

import ATMFrame.ATMEventHandler;
import ATMFrame.MenuEvent;
import ATMFrame.NumPadEvent;
import ATMFrame.Screenable;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class TransactionResultPage extends TableView<String[]> implements Screenable
{
    private final ResultController controller;
    private final ATMEventHandler<MenuEvent> menuHandler;
    private final ATMEventHandler<NumPadEvent> numPadHandler;
    
    public TransactionResultPage(ResultController controller)
    {
        this.controller = controller;
        this.menuHandler = (e) -> {};
        this.numPadHandler = (e) -> 
        {
            switch(e.getKeyCode())
            {
                case ENTER: 
                    controller.onEnter(true); break;
                case CANCEL: 
                    controller.onCancel(); break;
                case CLEAR: 
                    controller.onClear(); break;
            }
        };
        
        
        TableColumn<String[], String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory((p) -> new SimpleStringProperty(p.getValue()[0]));
        dateColumn.prefWidthProperty().bind(super.widthProperty().divide(5.1));
        
        
        TableColumn<String[], String> locationColumn = new TableColumn("Description");
        locationColumn.setCellValueFactory((p) -> new SimpleStringProperty(p.getValue()[1]));
        locationColumn.prefWidthProperty().bind(super.widthProperty().divide(5));
        
        TableColumn<String[], String> moneyOutColumn = new TableColumn("Money Out");
        moneyOutColumn.setCellValueFactory((p) -> new SimpleStringProperty(p.getValue()[2]));
        moneyOutColumn.prefWidthProperty().bind(super.widthProperty().divide(5));
        
        TableColumn<String[], String> moneyInColumn = new TableColumn("Money In");
        moneyInColumn.setCellValueFactory((p) -> new SimpleStringProperty(p.getValue()[3]));
        moneyInColumn.prefWidthProperty().bind(super.widthProperty().divide(5));
        
        TableColumn<String[], String> balanceColumn = new TableColumn("Balance");
        balanceColumn.setCellValueFactory((p) -> new SimpleStringProperty(p.getValue()[4]));
        balanceColumn.prefWidthProperty().bind(super.widthProperty().divide(5));
        
        super.getColumns().addAll(dateColumn, locationColumn, moneyOutColumn, 
                moneyInColumn, balanceColumn);
        
        
    }

    @Override
    public boolean isBackable() {return false;}

    @Override
    public ATMEventHandler<MenuEvent> getMenuHandler() {return menuHandler;}

    @Override
    public ATMEventHandler<NumPadEvent> getNumPadHandler() {return numPadHandler;}
    
}
