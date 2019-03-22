package ATMFrame;

import javafx.scene.control.Button;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class ATMButton extends Button
{
    
    public ATMButton(ATMKeyCode keyCode, String text)
    {
        super(text);
        setOnAction((e) -> this.fireEvent(new ButtonEvent(keyCode)));
    }
    public ATMButton(ATMKeyCode keyCode)
    {
        this(keyCode, "");
    }
    
    
}
