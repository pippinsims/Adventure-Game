package adventuregame;

import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Unit;

public class Interactible extends Describable
{
    public String normalLocPrep;
    public String plurLocPrep; 
    public String actionVerb; //set to "" to make this an un-actionable interactible
    public String actLocPrep;
    public String locReference;
    protected Room myRoom;
    public boolean isEnabled = true;

    public Interactible() {};

    public Interactible(Room r, String name, String description, String preposition, String pluralDescription, String pluralPreposition, String actionVerb, String actionPreposition, String locationReference)
    {
        setDefaults(
            name,
            description,
            preposition,
            pluralDescription,
            pluralPreposition,
            actionVerb,
            actionPreposition
        );

        locReference = locationReference; 

        myRoom = r;
        r.add(this);
        
    }

    protected void setDefaults(String n, String d, String prep, String pd, String pprep, String a, String aprep)
    {
        if(aprep.isEmpty()) aprep = prep;
        if(pprep.isEmpty()) pprep = prep;

        name                    = n;
        description             = d;
        normalLocPrep           = prep;
        pluralDescription       = pd;
        plurLocPrep             = pprep;
        actionVerb              = a; //won't perform action if actionVerb.equals("")
        actLocPrep              = aprep;
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

    public void action(Unit u)
    {
        throw new UnsupportedOperationException("Unimplemented method 'action'");
    }

    public void inspect()
    {
        throw new UnsupportedOperationException("Unimplemented method 'inspectInteractible'");
    }
}