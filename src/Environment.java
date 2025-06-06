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
            
            if(r0 != savedRoom) //if room is not saved
            {
                savedRoom = r0;                
                //exposition
                InteractionUtil.slowPrintln("You're in " + r0.getDescription() + ".");
            }
            
            String descriptor;
            if(r0.getNumInteractibles() > 0)
            {
                descriptor = "There is a ";
                for(int i = 0; i < r0.getNumInteractibles(); i++)
                {
                    //what if there are two of the exact same thing
                    descriptor += (r0.getInteractible(i).getExposition()) + ((i<r0.getNumInteractibles()-2) ? ", a " :
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
                for(int i = 0; i < r0.getNumEnemies(); i++)
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
                for (int i = 0; i < r0.getNumEnemies(); i++) 
                {
                    System.out.print("Enemy (" + (i + 1) + "): ");
                    r0.getEnemy(i).chooseAction(r0);
                }
            }
        }
        scanner.close();
    }

    private static void generateMap()
    {
        r0 = new Room(new Room[3], "a dimly lit room.\nThere is a faint foul odor...\nThe patchwork on the wall depicts of a redheaded lunatic. \n\"Lord Gareth the Mad.\"", new Interactible[3]);
        r0.setDoorMsg("This room is gifted with");       
        
        Room mossyRuin = new Room(new Room[2], "a room with shrooms, a shroom room if you will.\n       \t\t\t\tAre you afraid of large spaces? Becausesss there's a mush-a-room if you catch my drift, oh and this room is cursed with");
        r0.setDoor(0, mossyRuin, Room.direction.NORTH);
        mossyRuin.setDoor(0, r0, Room.direction.SOUTH);
        mossyRuin.setDoor(1, new Room(new Room[1]), Room.direction.SOUTH);
        mossyRuin.getRoom(1).setDoor(0, mossyRuin, Room.direction.NORTH);

        r0.setDoor(1, new Room(new Room[1]), Room.direction.WEST);
        r0.getRoom(1).setDoor(0, r0, Room.direction.EAST);

        Room treasureRoom = new Room(new Room[1], "a room filled to the brim in a plentious manner. Old swords and worn chalices adorned with gems sparkle and set your heart in motion with");
        r0.setDoor(2, new Room(new Room[2]), Room.direction.EAST);
        r0.getRoom(2).setDoor(0, r0, Room.direction.WEST);
        r0.getRoom(2).setDoor(1, treasureRoom, Room.direction.SOUTH);
        treasureRoom.setDoor(0, r0.getRoom(2), Room.direction.NORTH);

        //treasureRoom.appendRoom(mossyRuin, Room.direction.WEST);
        //treasureRoom.setRoom(2, new Room(new Room[], {treasureRoom}))

        
        
        
       mossyRuin.addEnemy(new Enemy(2, new Inventory(2), 1, 99999, "Mushroom"));

        for (int i = 0; i < 4; i++) 
        {
            r0.addEnemy(new Enemy(3));
        }

        for(int i = 0; i < r0.getNumInteractibles(); i++)
        {
            r0.setInteractible(i, new Torch(true, (i + 1) * 2));
        }
        r0.setInteractible(2, new ViewablePicture("mad_king.txt", 2));

        //PROBABLY SHOULD MAKE A "WALL ENTITY" FOR DOOR, VIEWABLEPICTURE, WINDOW, ETC...
        //NOT TORCH THOUGH, IT IS JUST AN INANIMATE ENTITY BECAUSE IT CAN BE ON THE FLOOR


    }

    public static void playerAttackEnemy(int index, int amount, String type)
    {
        if(r0.getEnemy(index).receiveDamage(amount, type))
        {
            InteractionUtil.slowPrintln("You have murdered the " + r0.getEnemy(index).getRandomDescription(), 250);
            r0.slayEnemy(index);
        }
    }
}
