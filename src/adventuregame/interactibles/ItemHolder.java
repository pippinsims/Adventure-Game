package adventuregame.interactibles;

import adventuregame.Effect;
import adventuregame.Environment;
import adventuregame.Interactible;
import adventuregame.QuickTimeEvent;
import adventuregame.Room;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;

public class ItemHolder extends Interactible {
    
    public Item item;

    public ItemHolder(Item i, Room r, String preposition, String location)
    {
        if(i.isDynamicItem()) throw new UnsupportedOperationException("DynamicItem x must be in a room as x.interactible, not ItemHolder(x.item)");
        setDefaults(
            i.getName(),
            i.getDescription(),
            preposition, 
            i.getPluralDescription(), 
            "", 
            "Take", 
            "from", 
            new String[0], 
            new String[0]
        );

        item = i;
        locReference = location;
        myRoom = r;
        r.add(this);
    }

    @Override
    public void action(Unit u)
    {
        switch(item.getName())
        {
            case "Cledobl":
                if(u.getName().equals("Laur"))
                {
                    System.out.println(u.getName() + " took " + name + " " + actLocPrep + " " + locReference);
                    myRoom.remove(this);
                    u.getInventory().add(item);
                }
                else
                {
                    System.out.println(u.getName() + " takes the sword by the handle... ");
                    if(!new QuickTimeEvent(
                        u,
                        item, 
                        new String[] {"YOU FEEL EXTREME PAIN. YOU ARE DYING"},
                        new String[][] {
                            {"Do nothing."        , "Freeze."},
                            {"Let go."            , "Let go."},
                            {"Cry out."           , "Cry out."},
                            {"Relax your grip."   , "PULL HARDER."}, //TODO ideaaaaally i'd make this a Node-based structure not unlike Dialogue, what i have right now is like sort of halfway there
                            {"Pry hand violently.", ""},
                            {"Pull harder."       , ""}
                        },
                        new String[] {"Cry out.", "Pry hand violently."},
                        new String[] {"Pull harder."},
                        new Effect(Effect.Type.VITALITYDRAIN, 10, (int)(u.getMaxHealth()/10))
                    ).run()) if(u.getHealth() > 0) Environment.kill(u);
                }
                break;
            default:
                System.out.println(u.getName() + " took " + name + " " + actLocPrep + " " + locReference);

                myRoom.remove(this);
                u.getInventory().add(item);
                break;
        }
    }

    @Override
    public void inspect()
    {
        System.out.println(name + ": " + description);
    }
}
