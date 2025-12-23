package adventuregame.interactibles;

import java.util.Random;

import adventuregame.Interactible;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.dynamicitems.GoldenPot;

public class GoldenPotInteractible extends Interactible {

    private GoldenPot self;

    public GoldenPotInteractible(GoldenPot self, Room room)
    {
        this.self = self;
        name = self.name;
        description = self.getDescription();
        myRoom = room;
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
            case 0:
                Utils.slowPrint("You kick the pot and it ");

                switch (new Random().nextInt(3)) 
                {
                    default:
                        Utils.slowPrintln("goes clattering against the wall.");
                        if(self.dmg < 3)
                            self.dmg++;
                        break;

                    case 1:
                        Utils.slowPrintln("rolls across the floor.");
                        break;

                    case 2:
                        Utils.slowPrintln("tumbles, but magically teleports to it's original position, vibrating back into place.");
                        
                        if(self.dmg > 0)
                        {
                            self.dmg--;
                            Utils.slowPrint("It seems oddly smoother than before.");
                            if(self.dmg != 0) Utils.slowPrintln(" It falls over because it is badly damaged.");
                        }
                        
                        break;
                }

                description = self.getDescription();
                break;

            case 1:
                Utils.slowPrint("You have received a Golden Pot!");
                self.collectItem(u);
                        
                break;
        }
    }

    @Override
    public void inspect() 
    {
        Utils.slowPrintln("You take a closer look at this golden pot and notice nothing new.");
    }

    @Override public String getPluralDescription() { return self.getPluralDescription(); }
}
