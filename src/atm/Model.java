package atm;

import java.util.*;

public class Model extends Observable
{
    // the ATM model is always in one of three states - watinginf ro an account number, 
    // waiting for a password, or logged in and processing
    // 'enum' (enumerated) allows us to create a new type which just takes specific state values
    enum  State { ACCOUNT_NO, PASSWORD, PROCESSING }

    // variables representing the ATM model
    State state = State.ACCOUNT_NO;
    int  number = 0;       // current number displayed in GUI (as a number, not a string)
    String display1 = null; // Message 1
    String display2 = null; // Message 2
    
    // The ATM talks to a bank, represented by the Bank object.
    LocalBank  bank = new LocalBank();

    public Model()
    {
        Debug.trace("Model constructor");
        // initially, we just set the two message displays to useful opening messages
        display1 =  "Welcome to the ATM\n";
        display2 =  "Welcome: Enter your account number\n" +
        "Followed by \"Ent\"";
    }

    // This is how the Controller talks to the Model - it calls this method
    // to say that a button in the GUI has been pressed
    public void process( String button )
    {
        Debug.trace("VewATM.process State [%-10s] Button = <%s>",
            state.toString(), button );

        // button is the label on the button which has been pressed
        switch ( button )
        {
            case "1" : case "2" : case "3" : case "4" : case "5" :
            case "6" : case "7" : case "8" : case "9" : case "0" :
                // one of the digits - just add it to the number showing in the top message window
                // Then call display to make the View update the user intrface with the new value
                char c = button.charAt(0);
                number = number * 10 + c-'0';           // Build number 
                display1 = "" + number;
                display();                              // Update display
                return;
            case "CLR" :
                // clear the number stored in the model (and update the display)
                number = 0;
                display1 = String.format("%d", number);
                display();                              // Update display
                return;
            case "Ent" :
                // Enter was pressed - what we do depends what state the ATM is in
                switch ( state )
                {
                    case ACCOUNT_NO:
                        // we were waiting for a complete account number - send the number we have
                        // to the bank object, and change to the state where we are expecting a password
                        bank.setAccNumber( number );
                        number = 0;
                        state = State.PASSWORD;
                        display1 = "";
                        display2 = "Now enter your password\n" +
                        "Followed by \"Ent\"";
                        break;
                    case PASSWORD:
                        // we were waiting for a password - send the number we have to the bank as a 
                        // password,
                        bank.setAccPasswd( number );
                        number = 0;
                        display1 = "";
                        // now check the account/password combination. If it's ok go into the PROCESSING
                        // state, otherwise go back to ask for another account number
                        if ( bank.checkValid() )
                        {
                            state = State.PROCESSING;
                            display2 = "Accepted\n" +
                            "Now enter the transaction you require";
                        } else {
                            state = State.ACCOUNT_NO;
                            bank.loggedOut();
                            display2 =  "Error: Please enter your a/c number";
                        }
                        break;
                    default :
                }
                display();                              // Update display
                return;
            // end of processing of ENT button
        }

        // if we get here, then it wasn't a number, or clear or enter that was pressed
        // All the other buttons only work if we are logged in (PROCESSING), so we test for that and
        // set messages appropriately
        if ( state != State.PROCESSING ) 
        {
            state = State.ACCOUNT_NO;
            display1 =  "But you are not logged in\n";
            display2 =  "Welcome: Enter your account number\n" +
            "Followed by \"Ent\"";
            display();                              // Update display
            return;
        }

        // Ok, we are logged in, and have been asked to do something ...
        switch ( button )
        {
            case "W/D" :               // Withdraw action
                display1 = "";
                if ( bank.withdraw( number ) )
                {
                    display2 =   "Withdrawn: " + number;
                } else {
                    display2 =   "You do not have sufficient funds";
                }
                number = 0;
                break;
            case  "Bal" :               // Balance action
                number = 0;
                display2 = "Your balance is: " + bank.getBalance();
                break;
            case "Dep" :               // Deposit action
                bank.deposit( number );
                display1 = "";
                display2 = "Deposited: " + number;
                number = 0;
                break;
            case "Fin" :               // Finish transactions
                // go back to the log in state
                state = State.ACCOUNT_NO;
                number = 0;
                display2 = "Welcome: Enter your account number";
                bank.loggedOut();
                break;
        }
        display();                              // Update display
    }

    // accessor methods for the model's instance variables
    public long getResult()
    {
        return number;
    }

    public String getMessage1()
    {
        return display1;
    }

    public String getMessage2()
    {
        return display2;
    }

    // This is where the Model talks to the View (or several views)
    // setChanged just sets a flag to say the mdoel has changed
    // notifyObservers calls the 'update' method in any view object which has been
    // added as an observer (in this case our View object, which is added in the Main class)
    // unpdate can access the model to find out what has changed and change the GUI as required
    public void display()
    {
        setChanged(); notifyObservers();
    }
}

