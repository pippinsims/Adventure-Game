import java.util.Scanner;

public class Environment {
    public static void main(String[] args) {
        
        //room r0 is the origin room
        Room r0 = generateMap();

        //r0[i] refers to room i of the rooms connected to r0

        Scanner kbd = new Scanner(System.in);
        String inputStr = "";
        
        //instantiating the player
        Player player = new Player();
        
        while(!inputStr.equals("Quit."))
        { 
            player.setActions();
                
            //exposition////////////
                System.out.println("You're in " + r0.getDescription() + ".");

                //list actions available
                System.out.println("You can:");
                int numActions = player.getActions().length;
                for(int i = 1; i <= numActions; i++)
                {
                    System.out.print("(" + i + ") ");
                    switch(i)
                    {
                        case 1: System.out.println("Do nothing."); break;
                        case 2: System.out.println("Open door 1."); break;
                        case 3: System.out.println("Open door 2."); break;
                        case 4: System.out.println("Open door 3."); break;
                    }
                }
            //could be a method?////

            inputStr = kbd.nextLine();
            //we now have a running game loop.
        }
    }

    private static Room generateMap()
    {
        Room r0 = new Room(new Room[3]);        

        //creating rooms for each door
        for(int i = 0; i < 3; i++)
        {
            r0.setRoom(0, new Room(new Room[]{r0}));
        }

        return r0;
    }
}
