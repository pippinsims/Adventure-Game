package adventuregame;

import java.util.Random;

import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Unit;

public class Interactible extends Describable
{
    public String name;
    protected String description;
    public String normalLocPrep;
    protected String pluralDescription;
    public String plurLocPrep; 
    public String actionVerb; //set to "" to make this an un-actionable interactible
    public String actLocPrep;
    protected String randomDescription;
    protected String randomPluralDescription;
    public String locReference;
    protected Room myRoom;
    public boolean isEnabled = true;

    public Interactible() {};

    public Interactible(Room r, String name, String description, String preposition, String pluralDescription, String pluralPreposition, String actionVerb, String actionPreposition, String randomDescription, String randomPluralDescription, String locationReference)
    {
        myRoom = r;
        r.add(this);
        setDefaults(name,description,preposition,pluralDescription,pluralPreposition,actionVerb,actionPreposition,randomDescription,randomPluralDescription);
        locReference = locationReference; 
    }

    protected void setDefaults(String n, String d, String prep, String pd, String pprep, String a, String aprep, String rd, String rpd)
    {
        if(aprep.isEmpty()) aprep = prep;
        if(pprep.isEmpty()) pprep = prep;
        if(rd.isEmpty()){ 
            rd = description;
            rpd = pd;
        }

        name                    = n;
        description             = d;
        normalLocPrep           = prep;
        pluralDescription       = pd;
        plurLocPrep             = pprep;
        actionVerb              = a; //won't perform action if actionVerb.equals("")
        actLocPrep              = aprep;
        randomDescription       = rd;
        randomPluralDescription = rpd;
    }

    protected void setDefaults(String n, String d, String prep, String pd, String pprep, String a, String aprep, String[] rd, String[] rpd)
    {
        if(rd.length == 0)      setDefaults(n, d, prep, pd, pprep, a, aprep, d, pd);
        else 
        {
            int r = new Random().nextInt(rd.length);
            if(rpd.length == 0) setDefaults(n, d, prep, pd, pprep, a, aprep, rd[r], pd);
            else                setDefaults(n, d, prep, pd, pprep, a, aprep, rd[r], rpd[r]);
        }
    }

    public void enable() { isEnabled = true; }
    public void disable() { isEnabled = false; }
    public boolean isEnabled() { return isEnabled; }

    public Room getRoom()
    {
        return myRoom;
    }

    public void setRoom(Room room)
    {
        myRoom = room;
    }
    
    public String getActionDescription()
    {
        return actionVerb + " " + getArticle() + " " + getDescription() + " " + actLocPrep + " " + locReference; 
    }

    protected String getArticle() 
    {
        return isAlone() ? "the" : Utils.articleOf(getDescription());
    }

    /**
     * Check whether this interactible is the only of it's kind
     * @return true if there are no other interactibles in myRoom that have the same description
     */
    protected boolean isAlone()
    {
        for (Interactible i : myRoom.interactibles) if(this != i && this.equals(i)) return false;
        return true;
    }
    
    @Override
    public String getPluralDescription()
    {
        if(Environment.curPlayer.getName().equals("Laur")) return randomPluralDescription;
        else return pluralDescription;
        
    }

    @Override
    public String getDescription()
    {
        if(Environment.curPlayer.getName().equals("Laur")) return randomDescription;
        else return description;
    }

    @Override
    public String getName() 
    {
        return name;
    }

    public void action(Unit u)
    {
        throw new UnsupportedOperationException("Unimplemented method 'action'");
    }

    public void inspect()
    {
        throw new UnsupportedOperationException("Unimplemented method 'inspectInteractible'");
    }
}