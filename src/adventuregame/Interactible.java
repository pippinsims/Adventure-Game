package adventuregame;

import java.util.ArrayList;

import adventuregame.interfaces.Describable;
import adventuregame.interfaces.Unit;

public class Interactible implements Describable
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

    protected String getArticle() 
    {
        String a = "the";
        ArrayList<Describable> arr = new ArrayList<>();
        for (Interactible i : myRoom.getInteractibles()) 
        { 
            arr.add(i);
        }
        a = Utils.articleOfDescribableInList(arr, this);

        return a;
    }

    protected boolean isAlone()
    {
        for (Interactible i : myRoom.getInteractibles()) 
        {
            if(this != i && i.getDescription() == getDescription())
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

    public void inspectInteractible()
    {
        throw new UnsupportedOperationException("Unimplemented method 'inspectInteractible'");
    }
}