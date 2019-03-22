package ATMFrame;

import ATMFrame.Advertising.AttractAd;
import ATMFrame.Advertising.ProcessingAd;
import java.util.ArrayDeque;
import java.util.Deque;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;


public class ATMFrame extends BorderPane implements ATM
{
    private final ATMVerticalMenu rightMenu;
    private final ATMVerticalMenu leftMenu;
    private final StackPane screen;
    private final ATMNumPad numPad;
    
    private final Timeline processingTimeline;
    private final AttractAd attractAd;
    private final ProcessingAd processingAd;
    private final Deque<Screenable> history;
    
    public ATMFrame(String attractAds, String processingAdPath)
    {
        super.setId("atm-frame");
        MenuButton rButton1 = new MenuButton(ATMKeyCode.RIGHT_BUTTON_1);
        MenuButton rButton2 = new MenuButton(ATMKeyCode.RIGHT_BUTTON_2);
        MenuButton rButton3 = new MenuButton(ATMKeyCode.RIGHT_BUTTON_3);
        this.rightMenu = new ATMVerticalMenu(true, rButton1, rButton2, rButton3);
        super.setRight(rightMenu);
        MenuButton lButton1 = new MenuButton(ATMKeyCode.LEFT_BUTTON_1);
        MenuButton lButton2 = new MenuButton(ATMKeyCode.LEFT_BUTTON_2);
        MenuButton lButton3 = new MenuButton(ATMKeyCode.LEFT_BUTTON_3);
        this.leftMenu = new ATMVerticalMenu(false, lButton1, lButton2, lButton3);
        super.setLeft(leftMenu);
        this.numPad = new ATMNumPad();
        super.setBottom(numPad);
        this.screen = new StackPane();
        screen.minHeightProperty().isEqualTo(400);
        screen.prefHeightProperty().bind(screen.minHeightProperty());
        screen.maxHeightProperty().bind(screen.minHeightProperty());
        screen.minWidthProperty().isEqualTo(600);
        screen.prefWidthProperty().bind(screen.minWidthProperty());
        screen.maxWidthProperty().bind(screen.minWidthProperty());
        screen.setId("atm-screen");
        super.setCenter(screen);
        
        this.processingTimeline = new Timeline();
        this.history = new ArrayDeque<>();
        this.attractAd = new AttractAd(this, attractAds);
        this.processingAd = new ProcessingAd(this, processingAdPath);
    }
    
    /**
     * Sets the given handler to both the right and left {@link ATMVerticalMenu} 
     * of the ATM.
     * @param handler handler to be applied to both right and left 
     * ATMVerticalMenu of the ATM.
     * 
     * @see ATMVerticalMenu
     * @see #removeMenuHandler(ATMFrame.ATMEventHandler) 
     */
    private void addMenuHandler(ATMEventHandler<MenuEvent>  handler)
    {
        rightMenu.addMenuHandler(handler);
        leftMenu.addMenuHandler(handler);
    }
    
    /**
     * Removes the given handler from both the right and left 
     * {@link ATMVerticalMenu} of the ATM.
     * @param handler handler to be removed from both right and left 
     * ATMVerticalMenu of the ATM.
     * 
     * @see ATMVerticalMenu
     * @see #addMenuHandler(ATMFrame.ATMEventHandler)
     */
    private void removeMenuHandler(ATMEventHandler<MenuEvent> handler)
    {
        rightMenu.removeMenuHandler(handler);
        leftMenu.removeMenuHandler(handler);
    }
    
    /**
     * Sets the given handler to the {@link ATMNumPad}.
     * @param handler handler to be applied to the ATMNumPad.
     * 
     * @see ATMNumPad
     * @see #removeNumPadHandler(ATMFrame.ATMEventHandler) 
     */
    private void addNumPadHandler(ATMEventHandler<NumPadEvent> handler)
    {
        numPad.addNumPadHandler(handler);
    }
    
    /**
     * Removes the given handler from the {@link ATMNumPad}.
     * @param handler handler to be removed from the ATMNumPad.
     * 
     * @see ATMNumPad
     * @see #addNumPadHandler(ATMFrame.ATMEventHandler) 
     */
    private void removeNumPadHandler(ATMEventHandler<NumPadEvent> handler)
    {
        numPad.removeNumPadHandler(handler);
    }
    
    @Override
    public void loadFirstScreen()
    {
        //Retrieve the first screen from history..
        Screenable first = history.getFirst();
        //Discard the rest.
        history.removeIf((s) -> !s.equals(first));
        //Now load to the screen.
        loadToScreen(first);
    }
    
    @Override
    public void loadPreviousScreen()
    {
        //This should never be false...but just incase!
        if(!history.isEmpty())
        {
            //Retrieve the current Screenable from the screen pane.
            //As we are going backwards we cannot use loadToScreen here as it
            //saves the history.
            Screenable current = (Screenable) screen.getChildren().get(0);
            //Detatch handlers manually..
            removeMenuHandler(current.getMenuHandler());
            removeNumPadHandler(current.getNumPadHandler());
            
            //if the first screen is the only one left, then don't remove it.
            Screenable toLoad = history.size() == 1 ? history.peekLast() : history.pollLast();
            //attach handlers
            addMenuHandler(toLoad.getMenuHandler());
            addNumPadHandler(toLoad.getNumPadHandler());
            //set manually..
            screen.getChildren().clear();
            screen.getChildren().add((Node)toLoad);
        }
    }
    
    @Override
    public void loadToScreen(Screenable screenable)
    {
        unhookPreviousScreen();
        
        //If a processing ad is running wait until it finishes before loading
        //the new screen.
        if(processingTimeline.getStatus() == Animation.Status.RUNNING)
            processingTimeline.setOnFinished((e) -> 
            {
                screen.getChildren().clear();
                screen.getChildren().add((Node)screenable);
            });
        else 
        {
            screen.getChildren().clear();
            screen.getChildren().add((Node)screenable);
        }
        
        addMenuHandler(screenable.getMenuHandler());
        addNumPadHandler(screenable.getNumPadHandler());
    }
    
    /**
     * Removes previous {@link Screenable} from the screen pane, if applicable
     * the Screenable is added to the history.
     * 
     * If the screen does not contain any children then this method does nothing.
     * 
     * The removal of the Screenable from the screen pane includes the 
     * handlers set to this ATMFrame object.
     */
    private void unhookPreviousScreen()
    {
        //if a screen is loaded then remove handlers and add the screen to 
        //history if applicable.
        if(screen.getChildren().size() > 0)
        {
            Screenable previous = (Screenable) screen.getChildren().get(0);
            removeMenuHandler(previous.getMenuHandler());
            removeNumPadHandler(previous.getNumPadHandler());
            if(previous.isBackable())
                history.add(previous);
        }
    }

    @Override
    public void idleAd()
    {
        attractAd.load();
        loadToScreen(attractAd);
    }

    @Override
    public void processingAd()
    {
        //Start the processing ad - this starts the loading of the image/video.
        processingAd.load();
        loadToScreen(processingAd);
        
        // Key frames gives an easy way to set a duration to this processing ad
        // allowing the loadToScreen method to append actions after the animation
        // is fininished. This also prevents the main UI thread from being blocked.
        KeyFrame kf1 = new KeyFrame(Duration.seconds(5), (e) -> {});
        processingTimeline.getKeyFrames().clear();
        processingTimeline.getKeyFrames().add(kf1);
        processingTimeline.play();
    }
    

    

    
    
}
