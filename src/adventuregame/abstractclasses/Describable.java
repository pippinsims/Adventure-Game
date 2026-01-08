package adventuregame.abstractclasses;

import java.util.HashMap;
import java.util.Map;

import adventuregame.Game;
import adventuregame.Player;

public abstract class Describable
{
    protected String description = "", pluralDescription = "", name;
    protected Map<String,String> descMap = new HashMap<>();
    protected Map<String,String> pDescMap = new HashMap<>();

    public final String getName() { return name; }
    
    public String getDescription() { 
        if(Game.cur instanceof Player && descMap.containsKey(Game.cur.getName())) return descMap.get(Game.cur.getName());
        else return description;
    }

    public String getPluralDescription() { 
        if(Game.cur instanceof Player && pDescMap.containsKey(Game.cur.getName())) return pDescMap.get(Game.cur.getName());
        else return pluralDescription; 
    }
}
