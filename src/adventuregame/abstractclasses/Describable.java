package adventuregame.abstractclasses;

import java.util.HashMap;
import java.util.Map;

import adventuregame.Environment;
import adventuregame.Player;

public abstract class Describable
{
    protected String description = "", pluralDescription = "", name;
    protected Map<String,String> descMap = new HashMap<>();
    protected Map<String,String> pDescMap = new HashMap<>();

    public final String getName() { return name; }
    
    public final String getDescription() { 
        Player p = Environment.curPlayer;
        if(p != null && descMap.containsKey(p.getName())) return descMap.get(p.getName());
        else return description;
    }

    public final String getPluralDescription() { 
        Player p = Environment.curPlayer;
        if(p != null && pDescMap.containsKey(p.getName())) return pDescMap.get(p.getName());
        else return pluralDescription; 
    }
    

    @Override
    public int hashCode() 
    {
        if(getDescription() == null) throw new UnsupportedOperationException(name + " has no desc");
        return getDescription().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) return true;

        if(obj == null || getClass() != obj.getClass()) return false;

        Describable d = (Describable) obj;
        
        return this.getDescription().equals(d.getDescription());
    }
}
