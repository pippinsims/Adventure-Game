import java.io.*;
import java.util.Scanner;
public class Enemy {
    //Something
    public static void main (String []args)
    {
        Scanner kbd = new Scanner(System.in);

        int silly = 10;
        String name = "jester";
        int bells = 3;
        while(silly != 19)
        { 
            silly = kbd.nextInt();
            System.out.println("You're fat and weird.");
        }


        String dumb = "You're the dumnbest ever.";
        while(dumb == "You're the dumnbest ever.") //the message is written twice jsut for you
        {
            dumb += 1;
        }

        if(dumb == "You're the dumbest ever.")
            dumb = "What is wrong with reality?";
        
        //I am weird
    }
}
