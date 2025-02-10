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

    private static boolean performAction(int i, Player p)
    {   
        switch(p.actions.get(i))
        {
            case DOOR:
                r0 = r0.getRoom(promptList("Which door traveler?", r0.getNumExits(), "Try door &") - 1);
                
                break;

            case FIGHT:
                String[] attackTypes = new String[]{"Punch"};
                int attackDamage = 0;
                int chosenAttackType = promptList("How will you vanquish yoerer foeee??", attackTypes) - 1;
                
                int chosenEnemyIndex;
                if(r0.getNumEnemies() > 1)
                    chosenEnemyIndex = promptList("Which fooeeoee meets thine bloodtherstey eyee?", r0.getNumEnemies(), "Fight enemy &") - 1;
                else
                    chosenEnemyIndex = 0;

                switch(attackTypes[chosenAttackType])
                {
                    case "Punch": 
                        attackDamage = 1;
                        System.out.println("You heave a mighty blow at the " + r0.getEnemy(chosenEnemyIndex).getModifiedDescription("sad") + " and deal a serious " + attackDamage + " damage!");
                        break;
                    
                    default:
                        break;
                }
                
                if(r0.getEnemy(chosenEnemyIndex).receiveDamage(attackDamage))
                {
                    slowPrintln("You have murdered the " + r0.getEnemy(chosenEnemyIndex).getRandomDescription(), 250);
                    r0.slayEnemy(chosenEnemyIndex);
                }

                break;

            case INSPECT:
                String[] interactiblesDescriptions = new String[r0.getNumInteractibles()];
                for(int j = 0; j < interactiblesDescriptions.length; j++)
                {
                    interactiblesDescriptions[j] = r0.getInteractible(j).getDescription();
                }
                Interactible chosenInteractible = r0.getInteractible(promptList("There are a few objects in the room:", interactiblesDescriptions) - 1);

                switch (chosenInteractible.getDescription()) {
                    case "flaming stick":
                        slowPrintln("You take a closer look at this flaming stick and you notice that it is a burning torch, providing light and warmth!");
                        break;
                    
                    case "depiction": //(if description == depiction chosenInteractible must be an instance of ViewablePicture)
                        ViewablePicture chosenPicture = (ViewablePicture)chosenInteractible;
                        slowPrintln("You take a closer look at the depiction:\n");
                        String s = readFile(chosenPicture.getFileName());
                        System.out.println(s);
                        System.out.println("Press enter to continue");
                        scanner.nextLine();

                        break;
                
                    default:
                        break;
                }
                
                break;

            case TALK:
                System.out.println("What do you say?");
                String s = scanner.nextLine();
                if(s.equals("Quit."))
                    return false;
                else if(s.contains("stop"))
                    ; //here somehow perhaps make enemy react
                else
                    slowPrintln("Interesting...\nWell, that does nothing.");
                                   
                break;

            case CAST:
                String[] spellTypes = new String[]{"brain aneurysm"};
                int spellDamage = 0;

                slowPrintln("Focus...");
                slowPrintln("Speak: ");
                String spell = scanner.nextLine(); //MAYBE INPUT SUBSTRING PARSE METHOD LATER DOWN THE LINE
                    
                if (spell.contains("brain aneurysm"))
                {
                    spellDamage = 1000;
                    //idk yet. maybe getEnemies then damage one of them?
                }
                else
                {
                    slowPrintln("The energy within you contorts strangely, almost choking you.");
                    slowPrintln("Your head spins and it takes everything to not let it hit the cold, hard floor");
                    slowPrintln("The energy within you stabilizes");
                    //Maybe does damage for wrong input here?
                }
                break;

            default:
                break;
        }

        return true;
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
