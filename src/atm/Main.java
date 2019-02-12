package atm;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;


public class Main extends Application
{
  public static void main( String args[] )
  {
      Debug.set(true);              // enable debugging
      Debug.trace("ATM main");
      launch(args);
  }
  
  public void start(Stage window) {
        
    // Create the Model, View and Controller objects
    Model model = new Model();
    View  view  = new View();
    Controller controller  = new Controller();
    
    // Link them together so they can talk to each other
    controller.model = model;
    controller.view = view;
    
    view.controller = controller;

    model.addObserver( view );   
    
    view.start(window);        // Show window on screen
    model.display();           // Initialise display 
  }
}
