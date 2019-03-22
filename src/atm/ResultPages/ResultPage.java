package atm.ResultPages;

import ATMFrame.Screenable;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface ResultPage extends Screenable
{
    public void update(boolean result, String text);
}
