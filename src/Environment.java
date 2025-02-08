import java.util.Scanner;
import java.io.File;

public class Environment 
{
    public static Scanner scanner = new Scanner(System.in);
    private static Room r0;
    public static void main(String[] args) 
    {
        //room r0 is the current room
        generateMap();

        //r0[i] refers to room i of the rooms connected to r0

        String inputStr = "";
        
        //instantiating the player
        Player player = new Player();

        while(!inputStr.equals("Quit."))
        {
            player.setActions(r0);
                
            //exposition
            slowPrintln("You're in " + r0.getDescription() + ".");
            if(r0.getNumInteractibles() > 0)
            {
                String descriptor = "There is a ";
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
                String descriptor = "There is a ";
                for(int i = 0; i<r0.getNumEnemies(); i++)
                {
                    descriptor += (r0.getEnemy(i).getDescription()) + ((i<r0.getNumEnemies()-2) ? ", a " :
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
            performAction(promptList("You can:", player.getActionDescriptions()) - 1, player);

            if (r0.getEnemies() != null)
            {
                for (Enemy e : r0.getEnemies()) {
                    performAction(e);   
                }
            }
        }
        scanner.close();
    }

    //public static void describeList(String desclist list desc qre 49)

    private static void generateMap()
    {
        r0 = new Room(new Room[3], new Interactible[2], "a dimly lit room.\nThere is a faint foul odor... \nThe patchwork on the wall depicts of a redheaded lunatic. \n\"Lord Gareth the Mad.\"\nThe room is gifted");        

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
    }

    private static void performAction(int i, Player p)
    {   
        switch(p.actions.get(i))
        {
            case DOOR:
                r0 = r0.getRoom(promptList("Which door traveler?", r0.getNumExits(), "Try door &") - 1);
                
                break;

            case FIGHT:
                String[] attackTypes = new String[]{"Punch"};
                int attackDamage = 0;
            
                if(attackTypes[promptList("How will you vanquish yoerer foeee??", attackTypes) - 1].equals("Punch"))
                    attackDamage = 1;

                int chosenEnemyIndex = promptList("Which fooeeoee meets thine bloodtherstey eyee?", r0.getNumEnemies(), "Fight enemy &") - 1;
                if(r0.getEnemy(chosenEnemyIndex).receiveDamage(attackDamage))
                {
                    slowPrintln("You have murdered the " + r0.getEnemy(chosenEnemyIndex).getDescription(), 250);
                    r0.slayEnemy(chosenEnemyIndex);
                }

                //SHOULD INFORM THE PLAYER ABOUT THE FIGHT
                break;
            case INSPECT:
                String s = readFile("mad_king.txt");
                System.out.print(s);
                System.out.println("\n"+ "Press enter to continue");
                scanner.nextLine();
                break;
            case CAST:
                String[] spellTypes = new String[]{"brain aneurysm"};
                int spellDamage = 0;

                System.out.println("Focus...");
                System.out.print("Speak: ");
                String spell = scanner.nextLine(); //MAYBE INPUT SUBSTRING PARSE METHOD LATER DOWN THE LINE
                    
                if (spell.contains("brain aneurysm"))
                {
                    spellDamage = 1000;
                    //idk yet. maybe getEnemies then damage one of them?
                }
                break;
            default:
                break;
        }
    }


    //IDEALLY THERE WILL ONLY BE 1 DEFINITION OF performAction() BUT THAT IS AFTER THE Person INTERFACE
    private static void performAction(Enemy e)
    {   
        switch(e.chooseAction(r0))
        {
            case 1:
                slowPrintln("The " + e.getDescription() + " raises it's fiendish arms and jumps at you with startling dexterity.\nYou have no choice but to die.\nYET YOU LIVE.");
                
                break;

            default:
                break;
        }
    }

    private static int promptList(String question, int listSize, String listPrompts)
    {        
        String[] options = new String[listSize];
        for(int i = 0; i < listSize; i++)
        {
            options[i] = listPrompts;

            //replace '&'s with 1-based indexes
            for(int j = 0; j < listPrompts.length(); j++)
            {
                if(options[i].charAt(j) == '&')
                    options[i] = options[i].substring(0, j) + (i + 1) + options[i].substring(j + 1, options[i].length());
            }
        }
        
        return promptList(question, options);
    }

    private static int promptList(String question, String[] listPrompts)
    {
        System.out.println(question);
        for(int i = 0; i < listPrompts.length; i++) 
        {
            System.out.println("(" + (i + 1) + ") " + listPrompts[i]);
        }

        return forceInputToInt(scanner.nextLine(), listPrompts);
    }

    private static Integer forceInputToInt(String s, String[] options)
    {    
        Integer inputInt = null;
        if(!s.equals("Quit."))
        {
            do
            {
                try 
                {
                    inputInt = Integer.parseInt(s);
                }   
                catch(Exception e)
                {
                    String question = "[Incorrect input!]";
                    if (s.contains("fuck"))             
                        question = "Yeah okay fuck you too man, I'm just trying to do my job."; 

                    inputInt = promptList(question, options);
                } 
            } while(inputInt == null);
        }
        return inputInt;
    }

    private static void slowPrint(String output, int sleepDuration)
    {   
        for(int i = 0; i < output.length(); i++)
        {   
            try{
                if(output.charAt(i) == '\n')
                    Thread.sleep(sleepDuration*5);
                Thread.sleep(sleepDuration);
            }
            catch(Exception e){}

            System.out.print(output.charAt(i));
        }
    }

    private static void slowPrintln(String output, int sleepDuration)
    {
        slowPrint(output+'\n', sleepDuration);
    }

    private static void slowPrintln(String output)
    {
        slowPrintln(output, 1); //50 for real
    }

    private static String readFile (String fileName)
    {   
        String completeString = "";
        try {
            File file = new File(fileName);
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                completeString += fileScanner.nextLine() + "\n";
            }
            fileScanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return completeString;
    }
}
