package atm;




import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.*;

import java.util.*;

class View implements Observer
{
    int H = 420;         // Height of window pixels 
    int W = 500;         // Width  of window pixels 

    // variables for components of the user interface
    Label      message;       // Message area 
    TextField   output1;      // Result area 
    TextArea   output2;       // Result area 
    ScrollPane scrollPane;
    GridPane grid;
    TilePane buttonPane;

    // The controller object that we send messages to
    public Controller controller;

    // main method to set up the user interface
    public void start(Stage window)
    {
        Debug.trace("View::start");

        // create the user interface component objects

        // layout objects
        grid = new GridPane();
        grid.setId("Layout");           // assign an id to be used in css file
        buttonPane = new TilePane();
        buttonPane.setId("Buttons");    // assign an id to be used in css file

        // overall application 
        Scene scene = new Scene(grid, W, H);   
        scene.getStylesheets().add("atm.css"); // tell the app to use this css file

        // controls

        message  = new Label();         // Message bar at the top
        message.setText( "" );          // Blank message initially
        grid.add( message, 0, 0);       // Add to GUI at the top       

        output1  = new TextField();     // text field for numbers      
        output1.setText("");            // Blank initially
        grid.add( output1, 0, 1);       // Add to GUI on second row                      

        output2  = new TextArea();      // multi-line text area
        output2.setText( "" );          // Blank initially
        output2.setEditable(false);     // Read only (for the user)
        scrollPane  = new ScrollPane(); // create a scrolling window
        scrollPane.setContent( output2 );  // put the text area 'inside' the scrolling window
        grid.add( scrollPane, 0, 2);    // add the scrolling window to GUI

        // Buttons - these are layed out on a tiled pane, then
        // the whole pane is added to the main grid as the forth row

        // Button labels - empty strings are spacers
        String labels[] = {
                "7",    "8",  "9",  "",  "Dep",  "",
                "4",    "5",  "6",  "",  "W/D",  "",
                "1",    "2",  "3",  "",  "Bal",  "Fin",
                "CLR",  "0",  "",   "",  "",     "Ent" };

        int LABELS = labels.length;      // # Button Labels

        // make an array of button objects, and add them to the tiled pane
        Button buttons[] = new Button[LABELS];

        for ( int i=0; i<LABELS; i++ )
        {
            String label = labels[i];
            if ( label.length() >= 1 )
            {
                // non-empty string - make a button
                Button b = new Button( label );        
                b.setOnAction( this::buttonClicked ); // set the method to call when pressed
                buttons[i] = b;                       // add to array
                buttonPane.getChildren().add( b );    // and add to tiled pane
            } else {
                // empty string - add an empty text element as a spacer
                buttonPane.getChildren().add( new Text() ); 

            }
        }
        grid.add(buttonPane,0,3); // add the tiled pane of buttons to the grid

        // add the complete GUI to the window and display it
        window.setScene(scene);
        window.show();

        // set the opening message at the top
        message.setText( "Bank" );                     // Opening message
    }

    // This is how the View talks to the Controller
    // This method is called when a button is pressed
    // It fetches the label on the button and passes it to the controller's process method
    public void buttonClicked(ActionEvent event) {
        Button b = ((Button) event.getSource());
        if ( controller != null )
        {
            controller.process( b.getText() );       // Process
        }

    }

    // This is how the Model talks to the View
    // This method gets called BY THE MODEL, whenever the model changes
    // It has to do whatever is required to update the GUI to show the new model status
    @Override
    public void update( Observable o, Object arg )
    {
        // the 'Observable' is actually the Model, and is passed as an argument
        Model model = (Model) o;
        // Method setText will update the display for the two messages
        output1.setText( model.getMessage1() );
        output2.setText( model.getMessage2() );
    }

}

