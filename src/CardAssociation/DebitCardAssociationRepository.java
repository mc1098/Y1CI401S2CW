package CardAssociation;

import Database.ConnectionException;
import Database.RsToObject;
import Database.SQLDatabaseComponent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This repository contains the details for querying
 * a SQL database and initialising a {@link DebitCardAssociation}.
 * 
 * The repository is designed so that all the {@link CardType} objects can 
 * be called from this repository and used in order to validate a given account
 * number with a specific card type and the {@link CardType#id} public member
 * can be used when requesting DebitCardAssociations by id.
 * 
 * The SQL querying in this repository is kept vendor independent allowing 
 * queries to be sent to the given {@link SQLDatabaseComponent} which contains 
 * specific vendor implementation details.
 * 
 * @see DebitCardAssociation
 * @see CardType
 * @see SQLDatabaseComponent
 * 
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class DebitCardAssociationRepository implements CardAssociationRepository
{
    private final SQLDatabaseComponent database;
    private final CardBankAccountRepository accountRepository;
    private final RsToObject<CardAssociation> rsToCardAssociation;
    private final RsToObject<List<CardType>> rsToCardTypes;
    
    public DebitCardAssociationRepository(SQLDatabaseComponent database, 
            CardBankAccountRepository accountRepository)
    {
        this.database = database;
        this.accountRepository = accountRepository;
        this.rsToCardAssociation = new RsToCardAssociation();
        this.rsToCardTypes = new RsToCardTypes();
    }
    
    /**
     * Retrieves a {@link CardAssociation} associated with the given id.
     * 
     * @param id to search for in the CardAssociation database.
     * @return Return a {@link CardAssociation} associated with the given id.
     * @throws UnrecognizedCardNumber thrown when the Id given is not recognized.
     * @throws ConnectionException thrown when there was an error trying to access
     * the database.
     */
    @Override
    public CardAssociation getById(long id) 
            throws UnrecognizedCardNumber, ConnectionException
    {
        String query = String.format("select * from card_association ca "
                + "where ca.card_association_id = %d", id);
        
        try 
        {
            CardAssociation ca = database.query(query, rsToCardAssociation);
            if(ca == null)
                throw new UnrecognizedCardNumber(String.format("No Card "
                        + "information found with an id of %d", id));
            return ca;
        } catch(SQLException ex)
        {
            throw new ConnectionException(ex);
        }
    }

    @Override
    public List<CardType> getAll() throws ConnectionException
    {
        String query = "select ca.card_association_name, ct.card_association_id,"
                + " ct.account_number_prefix from card_type ct\n" +
            "inner join card_association ca on ca.card_association_id = ct.card_association_id";
        
        try
        {
            return database.query(query, rsToCardTypes);
            
        } catch (SQLException ex)
        {
            throw new ConnectionException(ex);
        }
        
    }
    
    /**
     * Internal class for initialising a {@link CardAssociation} from a
     * {@link ResultSet}. 
     */
    class RsToCardAssociation implements RsToObject<CardAssociation>
    {

        @Override
        public CardAssociation initObject(ResultSet rs) throws SQLException
        {
            if(!rs.next())
                return null;
            
            long id = rs.getLong(1);
            String name = rs.getString(2);
            
            return new DebitCardAssociation(accountRepository, id, name);
        }
        
    }
    
    /**
     * Internal class for initialising the list of {@link CardType} objects from a
     * {@link ResultSet}. 
     */
    class RsToCardTypes implements RsToObject<List<CardType>>
    {

        @Override
        public List<CardType> initObject(ResultSet rs) throws SQLException
        {
            List<CardType> list = new ArrayList<>();
            List<String> prefixes = new ArrayList<>();
            
            if(!rs.next())
                return list;
            
            String name = rs.getString(1);
            long card_assoc_id = rs.getLong(2);
            prefixes.add(rs.getString(3));
            
            while(rs.next())
            {
                //While name is the same we are adding prefixes from unormalized resultset. 
                if(!name.equals(rs.getString(1)))
                {
                    list.add(new CardType(card_assoc_id, name, 
                            prefixes.toArray(new String[prefixes.size()])));
                    prefixes.clear();
                    
                    name = rs.getString(1);
                    card_assoc_id = rs.getInt(2);
                }
                
                prefixes.add(rs.getString(3));
            }
            
            list.add(new CardType(card_assoc_id, name, 
                    prefixes.toArray(new String[prefixes.size()])));
            
            return list;
            
        }
        
    }
    
    
}

/*
        American Express	34, 37
        China UnionPay	62, 88
        Diners ClubCarte Blanche	300-305
        Diners Club International	300-305, 309, 36, 38-39
        Diners Club US & Canada	54, 55
        Discover Card	6011, 622126-622925, 644-649, 65
        JCB	3528-3589
        Laser	6304, 6706, 6771, 6709
        Maestro	5018, 5020, 5038, 5612, 5893, 6304, 6759, 6761, 6762, 6763, 0604, 6390
        Dankort	5019
        MasterCard	50-55
        Visa	4
        Visa Electron	4026, 417500, 4405, 4508, 4844, 4913, 4917
        */