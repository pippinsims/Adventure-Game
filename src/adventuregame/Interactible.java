package adventuregame;

import java.util.ArrayList;

import adventuregame.interfaces.Describable;
import adventuregame.interfaces.Unit;

public class Interactible implements Describable
{
    protected String name, description, actionVerb, actionLocPreposition, normalLocPreposition;
    protected Room myRoom;

    public void action(Unit u);
    public Room getRoom()
    {
        return myRoom;
    }
    
    public String getActionDescription()
    {
        String article = getArticle();
        //TODO check if getArticle works, and work on LOCATIONS
        return actionVerb + " " + article + " " + description + ((!isAlone()) ? locToString() : ""); 
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

    private String locToString()
    {
        //from the wall, on the wall, in the corner, under the table
        throw new UnsupportedOperationException("Unimplemented method 'locToString'");
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

    public String getExposition();
    public void inspectInteractible();
    
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
}