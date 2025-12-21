package adventuregame;

import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Unit;

public class Interactible extends Describable
{
    protected String name, description, actionVerb, actLocPrep, normLocPrep, locReference;
    protected Room myRoom;

    public Room getRoom()
    {
        return myRoom;
    }
    
    public String getActionDescription()
    {
        String article = getArticle();
        return actionVerb + " " + article + " " + description + ((!isAlone()) ? " that is " + actLocPrep + " " + locReference : ""); 
    }

    public String getExposition()
    {
        String article = getArticle();
        return article + " " + description + " " + normLocPrep + " " + locReference;
    }

    public String getPluralDescription()
    {
        throw new UnsupportedOperationException("Unimplemented method 'getPluralDescription'");
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
        for (Interactible i : myRoom.interactibles) 
        {
            if(this != i && this.equals(i))
                return false;
        }
        return true;
    }
    
    public boolean isWallInteractible() 
    { 
        return false; 
    }

    public boolean isDoor() 
    { 
        return false; 
    }
    
    @Override
    public String getDescription() 
    {
        return description;
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

    protected String getRandomDescription()
    {
        return description;
    }
}