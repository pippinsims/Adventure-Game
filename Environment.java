import java.util.Scanner;

public class Environment {

    private static Room r0;
    public static void main(String[] args) {
        
        //room r0 is the current room
        r0 = generateMap();

        //r0[i] refers to room i of the rooms connected to r0

        Scanner scanner = new Scanner(System.in);;
        String inputStr = "";
        
        //instantiating the player
        Player player = new Player();

        while(!inputStr.equals("Quit."))
        {
            player.setActions(r0);
                
            //exposition////////////
                System.out.println("You're in " + r0.getDescription() + ".");

                //list actions available
                System.out.println("You can:");
                String[] actionDescriptions = player.getActionDescriptions();
                for(int i = 1; i <= actionDescriptions.length; i++)
                {
                    System.out.println("(" + i + ") " + actionDescriptions[i - 1]);
                }
            //could be a method?////
            
            inputStr = scanner.nextLine();
            
            //we now have a running game loop.
            
            //SHOULD BE COMPLETELY ERROR-TRAPPED
            if (inputNumberCheck(inputStr, actionDescriptions))
                performAction(Integer.parseInt(inputStr), player); 

           
        }
        scanner.close();
    }

    private static Room generateMap()
    {
        Room r0 = new Room(new Room[3], new Enemy[1], new Interactible[2], "a dimly lit room.\nThere is a faint foul odor... \nThe patchwork on the wall depicts of a redheaded lunatic. \nLord Gareth the Mad.\nThe room is gifted");        

        //creating 1-door rooms for each door
        for(int i = 0; i < r0.getNumRooms(); i++)
        {
            r0.setRoom(i, new Room(new Room[]{r0}));
        }

        return r0;
    }

    private static void performAction(int i, Player p)
    {   
        i--;
        Scanner kbd = new Scanner(System.in);
        //PERFORM SOME ACTION
        switch(p.actions.get(i)){
            case DOOR:
                System.out.println("Which door traveler?");

                String[] options = new String[r0.getNumRooms()];
                for (int j=0; j < options.length; j++) {
                    options [j] = "Try door " + (j+1);
                    System.out.println(options[j]);
                }

                String doahnumma = "";
                //we haev a glitch in el system
                //doahnumma = kbd.nextLine();
                doahnumma = "2";

                if (inputNumberCheck(doahnumma, options))
                {
                    r0 = r0.getRoom(Integer.parseInt(doahnumma));
                }
                break;
            default:
                break;
        
            

        }
        kbd.close();
    }
    private static boolean inputNumberCheck(String s, String[] options)
    {
        Scanner kbd = new Scanner(System.in);
    
        boolean falseInput = true;
        do
        {      
            try {
                Integer.parseInt(s);
                falseInput = false;
            }   
            catch(Exception e)
            {
                if (s.contains("fuck"))
                {              
                    System.out.println("Yeah okay fuck you too man, I'm just trying to do my job"); 
                }
                for(int i = 1; i <= options.length; i++)
                    {
                        System.out.println("(" + i + ") " + options[i - 1]);
                    }
                falseInput = true;
                s = kbd.nextLine();
            } 
        } while(falseInput);
        kbd.close();
        return !falseInput;
    }

}
