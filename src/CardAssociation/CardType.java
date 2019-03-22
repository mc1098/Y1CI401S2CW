package CardAssociation;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Max Cripps <43726912+mc1098@users.noreply.github.com>
 */
public class CardType
{
    public final long id;
    public final String card;
    public final String[] prefixes;
    
    public CardType(long id, String card, String...prefixes)
    {
        this.id = id;
        this.card = card;
        this.prefixes = prefixes;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof CardType))
            return false;
        
        CardType ct = (CardType) o;
        
        return (this.id == ct.id && 
                this.card.equals(ct.card) && 
                Arrays.equals(this.prefixes, ct.prefixes));
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 13 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 13 * hash + Objects.hashCode(this.card);
        hash = 13 * hash + Arrays.deepHashCode(this.prefixes);
        return hash;
    }
}
