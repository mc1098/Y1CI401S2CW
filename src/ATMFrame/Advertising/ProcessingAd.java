package ATMFrame.Advertising;

import ATMFrame.ATM;
import ATMFrame.ATMEventHandler;
import ATMFrame.MenuEvent;
import ATMFrame.NumPadEvent;
import ATMFrame.Screenable;
import java.io.File;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * A UI Ad wrapper for "Processing" ads. 
 * 
 * A Processing ad is a static image shown when the {@link ATM} is processing a
 * request.
 * The ad is not to be skipped..
 * 
 * This class looks in a local file at the path given on initialisation for 
 * an ad to load in this wrapper. It will pick the ad at random.
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class ProcessingAd extends StackPane implements Screenable
{
    
    private final File mediaDirectory;
    
    private final StackPane mediaPane;
    
    private final ATMEventHandler<MenuEvent> menuHandler;
    private final ATMEventHandler<NumPadEvent> numPadHandler;
    
    private final Random random;
    
    public ProcessingAd(ATM atm, String processingAdPath)
    {
        this.mediaDirectory = new File(processingAdPath);
        //check that the path is a directory otherwise throw exception.
        if(!mediaDirectory.isDirectory())
            throw new IllegalArgumentException(String.format("Media file path (%s) "
                    + "is not a directory!", processingAdPath));
        
        Text text = new Text("Your request is being processed");
        StackPane textContainer = new StackPane(text);
        textContainer.getStyleClass().add("info");
        textContainer.setMinHeight(40);
        textContainer.setPrefHeight(40);
        
        this.mediaPane = new StackPane();
        
        VBox container = new VBox(20, textContainer, mediaPane);
        
        super.getChildren().add(container);
        super.setId("processing-ad");
        
        this.menuHandler = (e)->{};
        this.numPadHandler = (e)->{};
        this.random = new Random();
    }
    
    /**
     * Returns a random file from the member field directory.
     * @return Retrieves a random file from the member field directory.
     */
    private File randomFile()
    {
        int r = random.nextInt(mediaDirectory.list().length);
        return mediaDirectory.listFiles()[r];
    }
    
    /**
     * Loads the random ad to viewed.
     * 
     * The method loads the random Image and displays it indefinitely. 
     */
    public void load()
    {
        Image img = new Image(randomFile().toURI().toString());
        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(500);
        imageView.setFitHeight(300);
        mediaPane.getChildren().clear();
        mediaPane.getChildren().add(imageView);
    }

    @Override
    public boolean isBackable() {return false;}

    @Override
    public ATMEventHandler<MenuEvent> getMenuHandler() {return menuHandler;}

    @Override
    public ATMEventHandler<NumPadEvent> getNumPadHandler() {return numPadHandler;}
    
}
