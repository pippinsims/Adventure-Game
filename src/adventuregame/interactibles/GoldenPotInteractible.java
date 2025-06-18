package adventuregame.interactibles;

import java.util.Random;

import adventuregame.Interactible;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.items.GoldenPot;

public class GoldenPotInteractible extends Interactible{

    private int dmg = 0;

    public GoldenPotInteractible(Room room)
    {
        myRoom = room;
        description = GoldenPot.defaultDescription;
        name = GoldenPot.defaultName;
        actionVerb = "Interact with";

        if(new Random().nextInt(2) == 1)
        {
            normLocPrep = "on";
            locReference = "the floor";
        }
        else
        {
            normLocPrep = "in";
            locReference = "the corner";
        }
        actLocPrep = normLocPrep; 
    }

    @Override
    public void action(Unit u) 
    {
        switch(Utils.promptList("How do you interact?", new String[]{"Kick","Take"}))
        {
            case 1:
                Utils.slowPrint("You kick the pot and it ");

                switch (new Random().nextInt(3)) 
                {
                    default:
                        Utils.slowPrintln("goes clattering against the wall.");
                        if(dmg < 3)
                            dmg++;
                        break;

                    case 1:
                        Utils.slowPrintln("rolls across the floor.");
                        break;

                    case 2:
                        Utils.slowPrintln("tumbles, but magically teleports to it's original position, vibrating back into place.");
                        
                        if(dmg > 0)
                        {
                            dmg--;
                            Utils.slowPrintln("It seems oddly smoother than before.");
                        }

                        if(dmg > 0)
                            Utils.slowPrintln("It falls over because it is badly damaged.");
                        
                        break;
                }

                if(dmg > 0)
                    description = GoldenPot.damageDescriptions[dmg];
                else 
                    description = GoldenPot.defaultDescription;

                break;

            case 2:
                Utils.slowPrint("You have recieved a Golden Pot!");
                u.getInventory().addItem(new GoldenPot(dmg));
                myRoom.interactibles.remove(this);
                        
                break;
        }
    }

    @Override
    public Room getRoom() 
    {
        return myRoom;
    }

    @Override
    public void inspectInteractible() 
    {
        Utils.slowPrintln("You take a closer look at this golden pot and notice nothing new.");
    }

    @Override
    public boolean isDoor() {
        return false;
    }
}
