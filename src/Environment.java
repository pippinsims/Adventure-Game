import java.util.ArrayList;

public class Environment extends InteractionUtil
{
    public static Room r0;
    public static void main(String[] args) 
    {
        //room r0 is the current room
        generateMap();
        
        Player player = new Player();

        InteractionUtil.slowPrintln("In this land, you're known as " + player.getName() + ".\nAdventure awaits, " + player.getName() + ".\n");

        Room savedRoom = null;
        while(true)
        {
            System.out.println("--Info--");
            if(r0 != savedRoom) //if room is not saved
            {
                savedRoom = r0;                
                //exposition
                InteractionUtil.slowPrintln("You're in " + r0.getDescription() + ".");
            }

            printInfo();

            System.out.println();

            //lists available actions, lets the player choose, then performs chosen action.
            System.out.println("--" + player.getName() + "'" + (player.getName().charAt(player.getName().length() - 1) != 's' ? "s" : "") + " Turn--");
            
            player.setActions(r0);
            if(!player.performAction(promptList("You can:", player.getActionDescriptions()) - 1))
                break;

            System.out.println();

            //performs the actions of each enemy automatically
            if (r0.getEnemies() != null)
            {
                Enemy cur;
                for (int i = 0; i < r0.getEnemies().size(); i++) 
                {
                    cur = r0.getEnemies().get(i);
                    System.out.println("--" + cur.getName() + "'" + (cur.getName().charAt(cur.getName().length() - 1) != 's' ? "s" : "") + " Turn--");
            
                    cur.chooseAction(r0);
                    System.out.println();
                }
            }
        }
        scanner.close();
    }

    private static void printInfo()
    {
        String descriptor;
            ArrayList<Interactible> inters = r0.getInteractibles();
            if(inters.size() > 0)
            {
                int num = inters.size();
                descriptor = "There is a ";
                for(int i = 0; i < num; i++)
                {
                    //what if there are two of the exact same thing
                    descriptor += (inters.get(i).getExposition()) + ((i < num - 2)  ? ", a " :
                                                                     (i == num - 2) ? ", and a " :
                                                                                      ".");
                }
                slowPrintln(descriptor);
            }

            ArrayList<Enemy> ens = r0.getEnemies();
            if(ens.size() > 0)
            {
                int num = ens.size();
                descriptor = "There is a ";
                for(int i = 0; i < num; i++)
                {
                    descriptor += (ens.get(i).getRandomDescription()) + ((i < num - 2)  ? ", a " :
                                                                         (i == num - 2) ? ", and a " :
                                                                                          ".");
                }
                slowPrintln(descriptor);
            }
    }

    private static void describeList(ArrayList<? extends Describable> l)
    {
        String descriptor;
        if(r0.getInteractibles().size() > 0)
        {
            int num = r0.getInteractibles().size();
            descriptor = "There is a ";
            for(int i = 0; i < num; i++)
            {
                //what if there are two of the exact same thing
                descriptor += (r0.getInteractibles().get(i).getExposition()) + ((i < num - 2)  ? ", a " :
                                                                                (i == num - 2) ? ", and a " :
                                                                                                    ".");
            }
            slowPrintln(descriptor);
        }
    }

    private static void generateMap()
    {
        r0 = new Room("a dimly lit room.\nThere is a faint foul odor...\nThe patchwork on the wall depicts of a redheaded lunatic. \n\"Lord Gareth the Mad.\"");
        r0.setDoorMsg("This room is gifted with");       
        
        Room mossyRuin = new Room("a room with shrooms, a shroom room if you will.\n       \t\t\t\tAre you afraid of large spaces? Becausesss there's a mush-a-room if you catch my drift,");
        mossyRuin.setDoorMsg("oh, and this room is cursed with");
        mossyRuin.getEnemies().add(new Enemy(2, new Inventory(2), 1, 99999, "Mushroom"));
        r0.appendRoom(mossyRuin, Room.direction.NORTH);

        r0.appendRoom(new Room(), Room.direction.WEST);

        r0.appendRoom(new Room(), Room.direction.EAST);

        Room treasureRoom = new Room("a room filled to the brim in a plentious manner. Old swords and worn chalices adorned with gems sparkle and set your heart in motion with");
        r0.getRoom(2).appendRoom(treasureRoom, Room.direction.SOUTH);

        for (int i = 0; i < 4; i++)
        {
            r0.getEnemies().add(new Enemy(3));
        }

        r0.getInteractibles().add(new Torch(true, 2));
        r0.getInteractibles().add(new Torch(true, 4));
        r0.getInteractibles().add(new ViewablePicture("mad_king.txt", 2));

        //PROBABLY SHOULD MAKE A "WALL ENTITY" FOR DOOR, VIEWABLEPICTURE, WINDOW, ETC...
        //NOT TORCH THOUGH, IT IS JUST AN INANIMATE ENTITY BECAUSE IT CAN BE ON THE FLOOR
    }

    public static void playerAttackEnemy(int index, int amount, String type)
    {
        if(r0.getEnemies().get(index).receiveDamage(amount, type))
        {
            InteractionUtil.slowPrintln("You have murdered the " + r0.getEnemies().get(index).getRandomDescription(), 250);
            r0.getEnemies().get(index);
        }
    }
}
