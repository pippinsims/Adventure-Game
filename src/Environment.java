import java.util.Scanner;

public class Environment {
    public static Scanner scanner = new Scanner(System.in);
    private static Room r0;
    public static void main(String[] args)
    {        
        //room r0 is the current room
        r0 = generateMap();

        //so r0[i] refers to room i of the rooms connected to r0

        String inputStr = "";
        
        //instantiating the player
        Player player = new Player();

        while(!inputStr.equals("Quit."))
        {
            player.setActions(r0);
                
            //exposition
            System.out.println("You're in " + r0.getDescription() + ".");

            //list actions available
            String[] actionDescriptions = promptList("You can:", player.getActionDescriptions());
            
            inputStr = scanner.nextLine();
            
            //we now have a running game loop.
            performAction(forceInputToInt(inputStr, actionDescriptions), player);
        }
        scanner.close();
    }

    private static Room generateMap()
    {
        Room r0 = new Room(new Room[3], new Enemy[1], new Interactible[2], "a dimly lit room.\nThere is a faint foul odor... \nThe patchwork on the wall depicts of a redheaded lunatic. \nLord Gareth the Mad.\nThe room is gifted");        

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
        //PERFORM SOME ACTION
        switch(p.actions.get(i-1))
        {
            case DOOR:
                String[] doorOptions = promptList("Which door traveler?", r0.getNumExits(), "Try door &");

                String inputStr = scanner.nextLine();
                r0 = r0.getRoom(forceInputToInt(inputStr, doorOptions));
                
                break;

            case FIGHT:
                String[] fightOptions = promptList("How will you vanquish yoerer foeee??", new String[]{"Punch"});
                
                inputStr = scanner.nextLine(); //would actually be a call to scanner but scanner doesn't work
                if(forceInputToInt(inputStr, fightOptions) != null)
                {
                    fightOptions = promptList("Which fooeeoee meets thine bloodtherstey eyee?", r0.getEnemies().length,"Fight enemy &");

                    inputStr = scanner.nextLine();
                    r0.getEnemy(forceInputToInt(inputStr, fightOptions) - 1).receiveDamage(1);
                }

                break;

            default:
                break;
        }
    }

    private static String[] promptList(String question, int listSize, String listPrompts)
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

    private static String[] promptList(String question, String[] listPrompts)
    {
        System.out.println(question);
        
        for(int i = 0; i < listPrompts.length; i++) 
        {
            System.out.println("(" + i + ") " + listPrompts[i]);
        }

        return listPrompts;
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
                if (s.contains("fuck"))             
                    System.out.println("Yeah okay fuck you too man, I'm just trying to do my job."); 

                for(int i = 1; i <= options.length; i++)
                {
                    System.out.println("(" + i + ") " + options[i - 1]);
                }

                s = scanner.nextLine(); //breaks
            } 
        } while(inputInt == null);

        return inputInt - 1; //input is 1-based
    }
}
