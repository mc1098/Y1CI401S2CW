package atm.Choice;


/**
 * This class is an extension of the {@link ChoiceController}
 * when the choices are numerical amounts and this interface allows the controller
 * to receive custom amount inputs.
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface AmountChoiceController extends ChoiceController
{
    public void customAmount(int amount);
}
