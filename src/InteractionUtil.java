import java.io.File;
import java.util.Scanner;

public class InteractionUtil {

    public static Scanner scanner = new Scanner(System.in);

    public static String readFile (String fileName)
    {
        String completeString = "";
        try 
        {
            Scanner fileScanner = new Scanner(new File("./src/" + fileName));
            while (fileScanner.hasNextLine()) {
                completeString += fileScanner.nextLine() + '\n';
            }
            fileScanner.close();
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return completeString;
    }

    public static void slowPrint(String output)
    {
        slowPrint(output, 1);
    }

    public static void slowPrintln(String output, int sleepDuration)
    {
        slowPrint(output+'\n', sleepDuration);
    }

    public static void slowPrintln(String output)
    {
        slowPrintln(output, 1); //50 for real
    }

    public static void slowPrint(String output, int sleepDuration)
    {   
        for(int i = 0; i < output.length(); i++)
        {   
            try
            {
                if(output.charAt(i) == '\n')
                    Thread.sleep(sleepDuration*5);
                Thread.sleep(sleepDuration);
            }
            catch(Exception e) 
            {
                e.printStackTrace();
            }

            System.out.print(output.charAt(i));
        }
    }

    public static void printOptions(String[] options)
    {
        for(int i = 0; i < options.length; i++) 
        {
            System.out.println("(" + (i + 1) + ") " + options[i]);
        }
    }

    public static int promptList(String question, int listSize, String listPrompts)
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

    public static int promptList(String question, String[] listPrompts)
    {
        System.out.println(question);
        printOptions(listPrompts);

        return forceInputToInt(scanner.nextLine(), listPrompts);
    }

    public static Integer forceInputToInt(String s, String[] options)
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

                System.out.println(question);
                printOptions(options);

                s = scanner.nextLine();
            } 
        } while(inputInt == null);
        return inputInt;
    }
}
