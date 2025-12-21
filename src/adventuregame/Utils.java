package adventuregame;
import java.io.File;
import java.util.*;

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
        return "aeiou".indexOf(c) > -1;
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

    public static void slowPrintDescList(ArrayList<? extends Describable> arr)
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

    private static Map<Describable, Integer> genDescMap(ArrayList<? extends Describable> arr)
    {
        Map<Describable, Integer> m = new HashMap<>();
        for (Describable d : arr) m.put(d, m.getOrDefault(d, 0) + 1);
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
        slowPrintln(output, currentPrintDelay);
    }

    public static void slowPrintln()
    {
        slowPrintln("", currentPrintDelay);
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
        slowPrint(msg + ((cur <  size - 2) ? ", "     :
                         (cur == size - 2) ? ", and " :
                                             "."));
    }

    public static void slowPrintlnAsListEntryWithArticles(String msg, int size, int cur)
    {
        slowPrint(articleOf(msg) + " " + msg + ((cur <  size - 2) ? ", "     :
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

    public static String[] descriptionsOf(ArrayList<? extends Describable> d)
    {
        int s = d.size();
        String[] strings = new String[s];
        for(int i = 0; i < s; i++) strings[i] = d.get(i).getDescription();
        return strings;
    }

    public static String[] namesOf(ArrayList<? extends Describable> d)
    {
        int s = d.size();
        String[] strings = new String[s];
        for(int i = 0; i < s; i++) strings[i] = d.get(i).getName();
        return strings;
    }

    public static String[] inspectsOf(ArrayList<Describable> all)
    {
        String[] strings = new String[all.size()];
        for(int i = 0; i < strings.length; i++)
        {
            Describable d = all.get(i);
            strings[i] = d instanceof Player ? d.getName() : d.getDescription();
        }
        return strings;
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

    /**
     * Prints {@code question}, then prints {@code listPrompts} using {@link #printOptions}. Returns the index of the chosen answer from the list.
     * @param question The question for the player
     * @param listPrompts The list of answers to said {@code question}
     * @return An {@code int}, the output of {@link #confirmInput}, denoting which prompt was chosen, 1-based.
     */
    public static int promptList(String question, String[] listPrompts)
    {
        System.out.println(question);
        printOptions(listPrompts);

        return confirmInput(scanner.nextLine(), listPrompts) - 1;
    }

    public static String articleOfDescribableInList(ArrayList<? extends Describable> arr, Describable d)
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

    /**
     * Forces valid user input with an error-trapped while-loop, re-prompting on bad input. Valid input is an {@code int} that is in {@code options} bounds, 1-based
     * @param inputString The string to check
     * @param options The list to use for bounds check, and re-prompt
     * @return A valid {@code int} once the user inputs one
     */
    public static Integer confirmInput(String inputString, String[] options)
    {    
        Integer inputInt = null;
        do
        {
            try 
            {
                inputInt = Integer.parseInt(inputString);
                if(inputInt > options.length || inputInt < 1)
                    throw new Exception();
            }   
            catch(Exception e)
            {
                inputInt = null;
                String question = "Incorrect input! Please input a given answer number."; //TODO: maybe "b" for back, "l" for last, "f" for first, maybe chars for specific commands like ("i" for Inventory) and allow stringing them together?

                System.out.println(question);
                printOptions(options);

                inputString = scanner.nextLine();
            } 
        } while(inputInt == null);
        return inputInt;
    }

    public static final class Tuple<T1, T2> 
    {
        private final T1 t1;
        private final T2 t2;

        public Tuple(T1 t1, T2 t2) 
        {
            this.t1 = t1;
            this.t2 = t2;
        }

        public T1 t1() 
        {
            return t1;
        }

        public T2 t2()
        {
            return t2;
        }

        @Override
        public boolean equals(Object o) 
        {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Tuple<?, ?> t = (Tuple<?, ?>)o;
            return t1.equals(t.t1) && t2.equals(t.t2);
        }
    }

    public static int linearFind(String[] arr, String s)
    {
        for(int i = 0; i < arr.length; i++) if(arr[i].equals(s)) return i;
        return -1;
    }
}
