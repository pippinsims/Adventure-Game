import java.util.Scanner;

public class Environment {
    public static void main(String[] args) {
        
        //room r0 is the origin room
        Room r0 = generateMap();

        //r0[i] refers to room i of the rooms connected to r0

        Scanner scanner = new Scanner(System.in);
        String inputStr = "";
        
        //instantiating the player
        Player player = new Player();
        
        Room curRoom = r0;

        while(!inputStr.equals("Quit."))
        { 
            player.setActions(curRoom);
                
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
            player.performAction(Integer.parseInt(inputStr));
        }

        scanner.close();
    }

    private static Room generateMap()
    {
        Room r0 = new Room(new Room[3]);        

        //creating 1-door rooms for each door
        for(int i = 0; i < r0.getNumRooms(); i++)
        {
            r0.setRoom(0, new Room(new Room[]{r0}));
        }

        return r0;
    }
}
