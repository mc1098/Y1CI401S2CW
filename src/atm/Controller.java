package atm;

public class Controller
{
  public Model model;
  public View  view;
  
  // we don't really need a constructor method, but include one to print a 
  // debugging message if required
  public Controller()
  {
    Debug.trace("Controller constructor");
  }
  
  
  // This is how the View talks to the Controller
  // AND how the Controller talks to the Model
  // This method is called by the View to respond to some user interface event
  // The controller's job is to decide what to do. In this case it just passes
  // the action (which is the label on the button that was pressed) to the 
  // process method of the model
  public void process( String action )
  {
    model.process( action );
  }
 
}
