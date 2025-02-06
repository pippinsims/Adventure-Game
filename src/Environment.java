import java.util.Scanner;

public class Environment 
{
    public static Scanner scanner = new Scanner(System.in);
    private static Room r0;
    public static void main(String[] args) 
    {
        //room r0 is the current room
        r0 = generateMap();

        //r0[i] refers to room i of the rooms connected to r0

        String inputStr = "";
        
        //instantiating the player
        Player player = new Player();

        while(!inputStr.equals("Quit."))
        {
            player.setActions(r0);
                
            //exposition
            slowPrintln("You're in " + r0.getDescription() + ".");

            //lists available actions, lets the player choose, then performs chosen action.
            performAction(promptList("You can:", player.getActionDescriptions()), player);

            if (r0.getEnemies() != null)
            {
                for (Enemy e : r0.getEnemies()) {
                    performAction(e);   
                }
            }
        }
        scanner.close();
    }

    private static Room generateMap()
    {
        Room r0 = new Room(new Room[3], new Enemy[1], new Interactible[2], "a dimly lit room.\nThere is a faint foul odor... \nThe patchwork on the wall depicts of a redheaded lunatic. \n\"Lord Gareth the Mad.\"\nThe room is gifted");        

        //creating 1-door rooms for each door
        for(int i = 0; i < r0.getNumExits(); i++)
        {
            r0.setRoom(i, new Room(new Room[]{r0}));
        }

        for (int i = 0; i < r0.getEnemies().length; i++) 
        {
            r0.setEnemy(i, new Enemy(3));
        }

        for(int i = 0; i < r0.getNumInteractibles(); i++)
        {
            //construct interactibles
        }

        return r0;
    }

    private static void performAction(int i, Player p)
    {   
        switch(p.actions.get(i))
        {
            case DOOR:
                r0 = r0.getRoom(promptList("Which door traveler?", r0.getNumExits(), "Try door &"));
                
                break;

            case FIGHT:
                String[] attackTypes = new String[]{"Punch"};
                int attackDamage = 0;
            
                if(attackTypes[promptList("How will you vanquish yoerer foeee??", attackTypes)].equals("Punch"))
                    attackDamage = 1;

                r0.getEnemy(promptList("Which fooeeoee meets thine bloodtherstey eyee?", r0.getNumEnemies(), "Fight enemy &")).receiveDamage(attackDamage);;

                //SHOULD INFORM THE PLAYER ABOUT THE FIGHT

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
                slowPrintln("The enemy raises it's fiendish arms and jumps at you with startling dexterity.\nYou have no choice but to die.\nYET YOU LIVE.");
                
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

        return forceInputToInt(scanner.nextLine(), listPrompts) - 1;
    }

    private static Integer forceInputToInt(String s, String[] options)
    {    
        Integer inputInt = null;
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

                promptList(question, options);

                s = scanner.nextLine();
            } 
        } while(inputInt == null);

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
        slowPrintln(output, 50);
    }
}
