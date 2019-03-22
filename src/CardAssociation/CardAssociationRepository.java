package CardAssociation;

import Database.ConnectionException;
import java.util.List;


/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public interface CardAssociationRepository
{
    public CardAssociation getById(long id) throws ConnectionException, UnrecognizedCardNumber;
    public List<CardType> getAll() throws ConnectionException;
}
