package adventuregame;
import java.io.File;
import java.util.*;
import java.util.concurrent.*;

import adventuregame.abstractclasses.Describable;

public class Utils {

    public static Random rand = new Random();
    public static Scanner scanner = new Scanner(System.in);
    public static final int MAX_PRINT_DELAY = 1; //50 for normal gameplay
    public static int currentPrintDelay = MAX_PRINT_DELAY;

    public static String[] names1 = new String[]{"Bo","Kua","An","Lis","Yi"};
    public static String[] names2 = new String[]{"sandual","\'hananah","mon","tio","narsh","poaf","duan"};

    public static final BlockingQueue<String> in = new LinkedBlockingQueue<>();

    static { new Thread(() -> { while (true) in.add(Utils.scanner.nextLine()); }, "stdin").start(); }

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
            throw new UnsupportedOperationException("File couldn't be read correctly!");
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
        Map<Describable, Integer> m = countsOf(arr);

        int i = 0;
        String a, d;
        for (Map.Entry<Describable, Integer> e : m.entrySet())
        {
            Interactible k = e.getKey() instanceof Interactible ? (Interactible)e.getKey() : null;
            if(e.getValue() > 1)
            {
                d = e.getKey().getPluralDescription();
                if(k != null) d += " " + k.plurLocPrep + " " + k.locReference;
                a = (i == 0 ? "There are " : "") + e.getValue();
            }
            else
            {
                d = e.getKey().getDescription();
                if(k != null) d += " " + k.normalLocPrep + " " + k.locReference;
                a = (i == 0 ? "There is " : "") + Utils.articleOf(d);
            }
       
            Utils.slowPrintlnAsListEntry(a + " " + d, m.size(), i);

            i++;
        }
    }

    public static void slowPrintNameList(ArrayList<? extends Describable> arr)
    {
        Map<Describable, Integer> m = countsOf(arr);

        int i = 0;
        String a, d;
        for (Map.Entry<Describable, Integer> e : m.entrySet())
        {
            Interactible k = e.getKey() instanceof Interactible ? (Interactible)e.getKey() : null;
            if(e.getValue() > 1)
            {
                d = e.getKey().getName() + "s";
                if(k != null) d += " " + k.plurLocPrep + " " + k.locReference;
                a = (i == 0 ? "There are " : "") + e.getValue() + " ";
            }
            else
            {
                d = e.getKey().getName();
                if(k != null) d += " " + k.normalLocPrep + " " + k.locReference;
                a = (i == 0 ? "There is " : "");
            }
       
            Utils.slowPrintlnAsListEntry(a + d, m.size(), i);

            i++;
        }
    }

    public static Map<Describable, Integer> countsOf(ArrayList<? extends Describable> arr)
    {
        Map<Describable, Integer> m = new LinkedHashMap<>(); //preserve insertion order
        for (Describable d : arr) m.put(d, m.getOrDefault(d, 0) + 1); //based on Description
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
        for(char c : output.toCharArray())
        {  
            try
            {
                if(c == '\n' || c == '.') Thread.sleep(sleepDuration * 5);
                else Thread.sleep(sleepDuration);
            }
            catch(InterruptedException e)
            {
                throw new Error(e.getMessage() + " while trying to slowPrint '" + output + "'");
            }

            System.out.print(c);
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
        slowPrint(msg + ((cur <  size - 2) ? ", " :
                         (cur == size - 2) ? ", and " :
                                             ".\n"));
    }

    public static void printOptions(String[] options)
    {
        String cur = "";
        int min = 0;
        for(int i = 0; i < options.length + 1; i++) if(i == options.length || !cur.equals(options[i]))
        {
            if(!cur.isEmpty()) System.out.println("(" + (i - min > 1 ? min + 1 + "-" : "") + i + ") " + cur);
            min = i;
            cur = options[i % options.length];
        }
        //TODO use commas if not in a row, check each arraylist for adjacent numbers, then print all items, singles or ranges, separated by commas
        // Map<String, ArrayList<Integer>> grouped = new LinkedHashMap<>(); //preserves insertion order
        // for (int i = 0; i < options.length; i++) {
        //     if(!grouped.containsKey(options[i])) grouped.put(options[i], new ArrayList<>());
        //     grouped.get(options[i]).add(i);
        // }
        // String cur = "";
        // int min = 0;
        // for(Map.Entry<String,ArrayList<Integer>> ent : grouped.entrySet())
        // {
        //     ArrayList<Integer> arr = ent.getValue();
        //     for(int i = 1; i < arr.size() + 1; i++)
        //     {
        //         if(i == arr.size() || arr.get(i) != arr.get(i - 1) + 1)
        //         {
        //         }
        //         if(!cur.isEmpty()) System.out.println("(" + (i - min > 1 ? min + 1 + "-" : "") + i + ") " + cur);
        //         min = i;
        //         cur = options[i % options.length];
        //     }
        // }
    }

    public static String[] descriptionsOf(ArrayList<? extends Describable> d)
    {
        int s = d.size();
        String[] strs = new String[s];
        for(int i = 0; i < s; i++) strs[i] = d.get(i).getDescription();
        return strs;
    }

    public static String[] namesOf(ArrayList<? extends Describable> d)
    {
        int s = d.size();
        String[] strs = new String[s];
        for(int i = 0; i < s; i++) strs[i] = d.get(i).getName();
        return strs;
    }

    public static String[] inspectTitlesOf(ArrayList<Describable> all) //just because the interactibles' descriptions are so fun
    {
        String[] strings = new String[all.size()];
        for(int i = 0; i < strings.length; i++)
        {
            Describable d = all.get(i);
            strings[i] = d instanceof Player ? d.getName() : d.getDescription();
        }
        return strings;
    }

    public static String[] actionDescsOf(ArrayList<Interactible> inters)
    {
        // for(Interactible i:inters) System.out.println(i.getRoom());
        String[] d = new String[inters.size()];
        for(int i = 0; i < d.length; i++) d[i] = inters.get(i).getActionDescription();
        return d;
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
     * Prints {@code question}, then, if provided, prints {@code listPrompts} using {@link #printOptions}. Returns the index of the chosen answer from the list.
     * @param question The question for the player
     * @param listPrompts The list of answers to said {@code question}
     * @return An {@code int}, the output of {@link #confirmInput}, the index of the chosen prompt, -1 if none provided.
     */
    public static int promptList(String question, String[] listPrompts)
    {
        System.out.println(question);
        if(listPrompts == null) return -1;
        
        printOptions(listPrompts);


        String in = scanloop();
        if(in == null) return -1;
        
        try {
            return confirmInput(in, listPrompts) - 1;
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    public static <T extends Describable> T promptList(String question, ArrayList<T> prompters)
    {
        System.out.println(question);
        String[] listPrompts = descriptionsOf(prompters);
        if(listPrompts == null) return null;
        
        printOptions(listPrompts);

        String in = scanloop();
        if(in == null) return null;

        try {
            return prompters.get(confirmInput(in, listPrompts) - 1);
        } catch (NoSuchElementException e) {
            return prompters.get(0);
        }
    }

    public static String advancedPromptList(String[] questions, String[][] listPrompts, int index)
    {
        //String[] questions = {"q1","q2"}
        //String[][] listprompts = 
        // {
        //     {"a","b","c"},
        //     {"d","e","f"},
        //     {"g","h", ""}
        // }
        // -> {"a","d","g"} or
        //    {"b","e","h"} or 
        //    {"c","f"} depending on index

        System.out.println(questions[cap(index, questions.length-1)]);
        ArrayList<String> outPromptsAsList = new ArrayList<>();
        int i = cap(index, listPrompts[0].length - 1), l = 0;
        while(l < listPrompts.length && !listPrompts[l][i].isEmpty()) outPromptsAsList.add(listPrompts[l++][i]);
        
        String[] outPrompts = outPromptsAsList.toArray(new String[0]);
        printOptions(outPrompts);

        String in = scanloop();
        if(in == null) return null;

        try {
            return outPrompts[confirmInput(in, outPrompts) - 1];
        } catch (NoSuchElementException e) {
            return outPrompts[0];
        }
    }

    public static String articleOfDescribableInList(ArrayList<? extends Describable> arr, Describable d)
    {
        for (Describable d1 : arr) if(d.equals(d1)) return articleOf(d.getDescription()); //compare by description
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
                String question = "Incorrect input! Please input a given answer number."; //TODO: maybe "b" for back (yeah that'd be hard to add but also great), "l" for last, "f" for first, maybe chars for specific commands like ("i" for Inventory) and allow stringing them together?

                System.out.println(question);
                printOptions(options);

                inputString = scanloop();
            } 
        } while(inputInt == null);
        return inputInt;
    }

    public static final class Tuple<T1, T2> 
    {
        public final T1 t1;
        public final T2 t2;

        public Tuple(T1 t1, T2 t2) 
        {
            this.t1 = t1;
            this.t2 = t2;
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

    public static boolean contains(String toCheck, String[] potentials)
    {
        for(String s : potentials) if(s.equals(toCheck)) return true;
        return false;
    }

    public static boolean contains(ArrayList<? extends Describable> arr, Object toFind)
    {
        for(Object o : arr) { if(o == toFind) return true; }
        return false;
    }

    public static int cap(int n, int at)
    {
        return n > at ? at : n;
    }

    // public static void restartScanner()
    // {
    //     System.out.println("Restarting Scanner");
    //     scanner.close();
    //     scanner = new Scanner(System.in);
    // }

    public static <T> boolean contains(List<?> l, Class<T> c) 
    {
        for (Object i : l) if (i.getClass().equals(c.getClass())) return true;
        return false;
    }

    public static <T, K extends T> K getFirst(ArrayList<? extends T> arr, Class<K> type) 
    {
        for (T element : arr) if(type.isInstance(element)) return type.cast(element);
        return null;
    }

    public static String scan() throws InterruptedException 
    { 
        return in.poll(2000, TimeUnit.MILLISECONDS);
    }

    public static String scanloop()
    {
        String s; 
        try { while ((s = scan()) == null); }
        catch (InterruptedException e) { return null; }
        return s;
    }
}