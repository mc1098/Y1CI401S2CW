package ATMFrame.Advertising;

import ATMFrame.ATM;
import ATMFrame.ATMEventHandler;
import ATMFrame.MenuEvent;
import ATMFrame.NumPadEvent;
import ATMFrame.Screenable;
import java.io.File;
import java.util.Random;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * A UI Ad wrapper for "Attract" ads. 
 * 
 * Attract ads is a 10 second ad shown when the {@link ATM} is in an idle state.
 * The ad is able to be skipped so when a user presses any button the ATM should 
 * be called to load the first page.
 * 
 * This class looks in a local file at the path given on initialisation for 
 * an ad to load in this wrapper. It will pick the ad at random and when that
 * ad is finished then another will be chosen in the same way.
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class AttractAd extends VBox implements Screenable
{
    private final File mediaFile;
    
    private MediaPlayer player;
    private final StackPane mediaPane;
    
    private final ATMEventHandler<MenuEvent> menuHandler; 
    private final ATMEventHandler<NumPadEvent> numPadHandler;
    
    private final Random random;
    
    public AttractAd(ATM atm, String mediaSrc)
    {
        this.mediaFile = new File(mediaSrc);
        //check that the path is a directory otherwise throw exception.
        if(!mediaFile.isDirectory())
            throw new IllegalStateException(String.format("Media file path (%s) "
                    + "is not a directory!", mediaSrc));
        
        this.mediaPane = new StackPane();
        Text text = new Text("PRESS ANY BUTTON TO CONTINUE");
        StackPane textContainer = new StackPane(text);
        textContainer.getStyleClass().add("info");
        textContainer.setMinHeight(40);
        textContainer.setPrefHeight(40);
        
        super.getChildren().addAll(mediaPane, textContainer);
        super.setId("attract-ad");
        
        //Set default handlers that will skip the ad when any button is pressed.
        this.menuHandler = (e) -> {end(); atm.loadFirstScreen();};
        this.numPadHandler = (e) -> {end(); atm.loadFirstScreen();};
        
        //For use when finding a random add.
        this.random = new Random();
    }
    
    /**
     * Returns a random file from the member field directory.
     * @return Retrieves a random file from the member field directory.
     */
    private File randomFile()
    {
        int r = random.nextInt(mediaFile.list().length);
        return mediaFile.listFiles()[r];
    }
    
    /**
     * Loads the random ad to viewed.
     * 
     * The method loads the random Media and plays the media for 10 seconds. 
     * The media is muted and once finished the method is called again within
     * the KeyFrame.
     * 
     * Usage of the KeyFrames allows us to essentially call a recursive action
     * without causing the Stack to overflow and because it does not run on the 
     * main UI thread it does not block that thread.
     */
    public void load() 
    {
        mediaPane.getChildren().clear();
        Media media = new Media(randomFile().toURI().toString());
        
        player = new MediaPlayer(media);
        player.setMute(true);
        player.setAutoPlay(true);
        player.setStopTime(Duration.seconds(10));
        //On end call this function again.
        player.setOnEndOfMedia(() -> load());
        MediaView mediaView = new MediaView(player);
       
        mediaView.setPreserveRatio(false);
        mediaView.setFitWidth(600);
        mediaView.setFitHeight(360);
        mediaPane.getChildren().add(mediaView);
    }
    
    /**
     * Stops the media player if it has been set.
     */
    private void end()
    {
        if(player != null)
            player.stop();
    }

    @Override
    public boolean isBackable() {return false;}

    @Override
    public ATMEventHandler<MenuEvent> getMenuHandler() {return menuHandler;}

    @Override
    public ATMEventHandler<NumPadEvent> getNumPadHandler() {return numPadHandler;}
    
}
