package adventuregame.interactibles;

import java.util.Random;

import adventuregame.Interactible;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.items.GoldenPot;

public class GoldenPotInteractible extends Interactible{

    public GoldenPotInteractible(Room room)
    {
        myRoom = room;
        description = GoldenPot.defaultDescription;
        name = GoldenPot.defaultName;
        actionVerb = "Kick";

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
    public void action(Unit u) {
        System.out.print("You kick the pot and it ");

        switch (new Random().nextInt(3)) 
        {
            default:
                System.out.println("goes clattering against the wall.");
                break;

            case 1:
                System.out.println("rolls across the floor.");
                break;

            case 2:
                System.out.println("tumbles, but magically teleports to it's original position, vibrating back into place.");
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
