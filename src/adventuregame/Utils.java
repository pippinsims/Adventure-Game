package adventuregame;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;

import adventuregame.abstractclasses.Describable;

public abstract class Utils {

    public static Random rand = new Random();
    public static Scanner scanner = new Scanner(System.in);
    public static final int MAX_PRINT_DELAY = 1; //50 for normal gameplay
    public static int currentPrintDelay = MAX_PRINT_DELAY;

    public static String[] names1 = new String[]{"Bo","Kua","An","Lis","Yi"};
    public static String[] names2 = new String[]{"sandual","\'hananah","mon","tio","narsh","poaf","duan"};

    public static String readFile (String fileName)
    {
        String completeString = "";
        try 
        {
            Scanner fileScanner = new Scanner(new File("src/adventuregame/asciiart/" + fileName));
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

    private static boolean isVowel(char c)
    {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }

    public static String articleOf(String str)
    {
        str = str.toLowerCase();
        char c = str.charAt(0);

        //if first is vowel, or if first is y and second isn't vowel
        if(isVowel(c) || (c == 'y' && !isVowel(str.charAt(1))))
            return "an";
        else
            return "a";
    }

    public static void slowPrintDescList(ArrayList<Describable> arr)
    {
        Map<Describable, Integer> m = genDescMap(arr);

        int i = 0;
        String a, d;
        for (Map.Entry<Describable, Integer> e : m.entrySet())
        {
            if(e.getValue() > 1)
            {
                d = e.getKey().getPluralDescription();
                a = ((i == 0) ? "There are " : "") + e.getValue();
            }
            else
            { 
                d = e.getKey().getDescription();
                a = ((i == 0) ? "There is " : "") + Utils.articleOf(d);
            }
       
            Utils.slowPrintlnAsListEntry(a + " " + d, m.size(), i);

            i++;
        }
    }

    private static Map<Describable, Integer> genDescMap(ArrayList<Describable> arr)
    {
        Map<Describable, Integer> m = new HashMap<>();
        for (Describable d : arr) 
        {
            m.put(d, m.getOrDefault(d, 0) + 1);
        }

        return m;
    }

    public static void slowPrint(String output)
    {
        slowPrint(output, currentPrintDelay);
    }

    public static void slowPrintln(String output, int sleepDuration)
    {
        slowPrint(output + '\n', sleepDuration);
    }

    public static void slowPrintln(String output)
    {
        slowPrintln(output, currentPrintDelay); //50 for real
    }

    public static void slowPrintln()
    {
        slowPrintln("", currentPrintDelay); //50 for real
    }

    public static void slowPrint(String output, int sleepDuration)
    {   
        for(int i = 0; i < output.length(); i++)
        {   
            try
            {
                if(output.charAt(i) == '\n' || output.charAt(i) == '.')
                    Thread.sleep(sleepDuration * 5);
                Thread.sleep(sleepDuration);
            }
            catch(Exception e) 
            {
                e.printStackTrace();
            }

            System.out.print(output.charAt(i));
        }
    }

    public static void slowPrintAsList(String msg, int size, int cur)
    {
        slowPrint(msg + ((cur < size - 2)  ? ", " :
                         (cur == size - 2) ? ", and " :
                                             "."));
    }

    public static void slowPrintlnAsListEntryWithArticles(String msg, int size, int cur)
    {
        slowPrint(articleOf(msg) + " " + msg + ((cur < size - 2)  ? ", " :
                                                (cur == size - 2) ? ", and " :
                                                                    ".\n"));
    }

    public static void slowPrintlnAsListEntry(String msg, int size, int cur)
    {
        slowPrint(msg + ((cur < size - 2)  ? ", " :
                         (cur == size - 2) ? ", and " :
                                             ".\n"));
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

        return confirmInput(scanner.nextLine(), listPrompts);
    }

    public static String articleOfDescribableInList(ArrayList<Describable> arr, Describable d)
    {
        for (Describable describable : arr) 
        {
            if(d.equals(describable)) //compare by description
            {
                return articleOf(d.getDescription());
            }
        }

        return "the";
    }

    public static Integer confirmInput(String s, String[] options)
    {    
        Integer inputInt = null;
        do
        {
            try 
            {
                inputInt = Integer.parseInt(s);
                if(inputInt - 1 >= options.length || inputInt < 1)
                    throw new Exception();
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
