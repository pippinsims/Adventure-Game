public class Environment extends InteractionUtil
{
    public static Room r0;
    public static void main(String[] args) 
    {
        //room r0 is the current room
        generateMap();

        //r0[i] refers to room i of the rooms connected to r0
        
        //instantiating the player
        Player player = new Player();

        Room savedRoom = null;
        while(true)
        {
            player.setActions(r0);
            
            if(r0 != savedRoom)
            {
                savedRoom = r0;                
                //exposition
                InteractionUtil.slowPrintln("You're in " + r0.getDescription() + ".");
            }
            
            String descriptor;
            if(r0.getNumInteractibles() > 0)
            {
                descriptor = "There is a ";
                for(int i = 0; i<r0.getNumInteractibles(); i++)
                {
                    descriptor += (r0.getInteractible(i).getDescription()) + ((i<r0.getNumInteractibles()-2) ? ", a " :
                                                                              (i == r0.getNumInteractibles()-2) ? ", and a " :
                                                                              ".");
                    // if(i < r0.getNumInteractibles()-2)
                    //     descriptor += (", a ");
                    // else if(i == r0.getNumInteractibles()-2)
                    //     descriptor += (", and a ");
                    // else
                    //     descriptor += (".");
                }
                slowPrintln(descriptor);
            }


            if(r0.getNumEnemies() > 0)
            {
                descriptor = "There is a ";
                for(int i = 0; i<r0.getNumEnemies(); i++)
                {
                    descriptor += (r0.getEnemy(i).getRandomDescription()) + ((i<r0.getNumEnemies()-2) ? ", a " :
                                                                       (i == r0.getNumEnemies()-2) ? ", and a " :
                                                                       ".");
                    // if(i < r0.getNumEnemies()-2)
                    //     descriptor += (", a ");
                    // else if(i == r0.getNumEnemies()-2)
                    //     descriptor += (", and a ");
                    // else
                    //     descriptor += (".");
                }
                slowPrintln(descriptor);
            }


            //lists available actions, lets the player choose, then performs chosen action.
            if(!player.performAction(promptList("You can:", player.getActionDescriptions()) - 1))
                break;

            if (r0.getEnemies() != null)
            {
                for (Enemy e : r0.getEnemies()) {
                    performAction(e);   
                }
            }
        }
        scanner.close();
    }

    private static void generateMap()
    {
        r0 = new Room(new Room[3], new Interactible[3], "a dimly lit room.\nThere is a faint foul odor...\nThe patchwork on the wall depicts of a redheaded lunatic. \n\"Lord Gareth the Mad.\"\nThis room is gifted");        

        //creating 1-door rooms for each door
        for(int i = 0; i < r0.getNumExits(); i++)
        {
            r0.setRoom(i, new Room(new Room[]{r0}));
        }

        for (int i = 0; i < 1; i++) 
        {
            r0.addEnemy(new Enemy(3));
        }

        for(int i = 0; i < r0.getNumInteractibles(); i++)
        {
            r0.setInteractible(i, new Torch(true));
        }
        r0.setInteractible(2, new ViewablePicture("mad_king.txt"));
    }

    //IDEALLY THERE WILL ONLY BE 1 DEFINITION OF performAction() BUT THAT IS AFTER THE Person INTERFACE
    private static void performAction(Enemy e)
    {   
        switch(e.chooseAction(r0))
        {
            case 1:
                slowPrintln("The " + e.getModifiedDescription("scary") + " raises it's fiendish arms and jumps at you with startling dexterity.\nYou have no choice but to die.\nYET YOU LIVE.");
                
                break;

            default:
                break;
        }
    }
}
