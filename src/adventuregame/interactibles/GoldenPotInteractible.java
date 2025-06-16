package adventuregame.interactibles;

import java.util.Random;

import adventuregame.Interactible;
import adventuregame.Room;
import adventuregame.interfaces.Unit;
import adventuregame.items.GoldenPot;

public class GoldenPotInteractible extends Interactible{

    public GoldenPotInteractible(Room room)
    {
        myRoom = room;
        description = GoldenPot.defaultDescription;
        name = GoldenPot.defaultName;
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
    public String getExposition() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExposition'");
    }

    @Override
    public void inspectInteractible() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inspectInteractible'");
    }

    @Override
    public boolean isDoor() {
        return false;
    }
}
