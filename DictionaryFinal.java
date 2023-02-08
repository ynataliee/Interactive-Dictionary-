import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Scanner;

/*------------NOTE TO CLIENT----------------------------
There is a bug in the program's functionality, when distinct is entered as a parameter
the output will be distinct AND REVERSED as well, for some reason when you call distinct
it reverses the contents, this probably has something to do with the dequeue I used in the method that
reverses the output.
Further when you call reverse it sometimes reverses the content, however if you enter
distinct and then reverse the contents will go back to its original alphabetical order.
 ------------------------------------------------------*/

public class DictionaryFinal {
    static String[] entrySplit;
    static int defs = 0;

    public static void prompt(){
        System.out.println("|");
        System.out.println("PARAMETER HOW-TO, please enter: ");
        System.out.println("1. A search key -then 2. An optional part of speech -then ");
        System.out.println("3. An optional 'distinct' -then 4. An optional 'reverse' ");
        System.out.println("|");
    }
    public static void notFound(){
        System.out.println("|");
        System.out.println("<NOT FOUND> To be considered for the next release. Thank you.");
        System.out.println("|");
    }

    public static void runProgram(){
        int i = 1;//for counting the number of searches
        String userIn;
        Scanner input = new Scanner(System.in);
        System.out.print("Search [" + i + "]:");
        userIn = input.nextLine();
        while(!userIn.equalsIgnoreCase("!q")){
            i++;
            validateUserIn(userIn);
            System.out.print("Search [" + i + "]:");
            userIn = input.nextLine();
        }//while
    }
    public static void validateUserIn(String in){
        if(in == null || in.isBlank() || in.equalsIgnoreCase("!help")){
            prompt();
        }
        else{
            getSearchPar(in);
        }
    }

    //getting input from user and splitting the user entry into an array
    //calls processUserEntry() to check if the keyword entered is in the dictionary
    public static String getSearchPar(String in){

        entrySplit = splitParameters(in);// an ARRAY, storing split userIn
        processUserEntry(entrySplit);

        return entrySplit[0];//for while loop in main, needs to go back to check if user wants to quit program

    }

    //splits the entered user parameters into an array to be processed later
    public static String[] splitParameters(String str){
        String split[] = str.split("\\s");
        return split;
    }

    //checks to see if key entered by user in the dictionary by calling doesKeyExist()
    //if doesKeyExist() returns true, then go to displayUserEntry();
    public static void processUserEntry(String [] split){
        if(doesKeyExist(split[0])== false & !(split[0].equalsIgnoreCase("!q"))){
            notFound();
            prompt();
        }
        else if(split[0].equalsIgnoreCase("!q")){System.out.println(" ");}
        else {
            displayUserEntry(split);
        }
    }

    //checks if key is in dictionary, returns boolean
    public static boolean doesKeyExist(String userIn){
        boolean result = false;
        if(EnumToGuavaMap.map.containsKey(userIn.toUpperCase()) == true){
            result = true;
        }
        return result;
    }

    //CALLS: fillsflagsMap() which gets the flags for split and puts it in the map
    //       goThroughFlags() goes through each flag and displays the output
    //       with the user defined filters
    public static void displayUserEntry(String split[]){
        if(split.length == 1){
            displayWithOneParameter(split[0]);
            return;
        }

        //After calling fillFlagsMap() We can Iterate through the map.
        Multimap<Integer, Integer> flags = fillFlagsMap(split);

        //If we have any values of zero in flags map then we have a parameter
        //that was entered incorrectly
        if(flags.containsValue(0) == true){
            for(int i = 0; i < 4; i++){//use to be i < 4
                if(flags.containsEntry(i,0) == true){
                    errorMsgSender(i,split);
                }//if
            }//for
            goThroughFlagsMap(flags,split);
        }//if
        else {
            goThroughFlagsMap(flags,split);
        }
    }

    //checks each parameter of user input and associates a number with a parameter, using a map
    //where we store key and values like so: <index,flag> where index is the index of the array split
    //from which the flag was set. flag 1 = part of speech, flag 2 = distinct, flag 3= reverse
    public static Multimap<Integer, Integer> fillFlagsMap(String[] split){
        Multimap<Integer, Integer> flags = ArrayListMultimap.create();
        int flag;
        for(int index = 1; index < split.length; index++){
            flag = setFlags(split, index);
            flags.put(index, flag);//
        }
        return flags;
    }

    public static int setFlags(String [] split, int index){
        int flag = 0;
        switch (index){
            case 1:
                if(checkParameterPOS(split[index]) == 1 ) {
                    flag = 1;
                }
                else if(checkParameterDis(split[index]) == 2){
                    flag = 2;
                }
                else if(checkParameterRev(split[index]) == 3){
                    flag = 3;
                }
                else{
                    flag = 0;
                }
                break;
            case 2:
                if(checkParameterDis(split[index]) == 2){
                    flag = 2;
                }
                else if(checkParameterRev(split[index]) == 3){
                    flag = 3;
                }
                else flag = 0;
                break;
            case 3:
                if(checkParameterRev(split[index]) == 3){
                    flag = 3;
                }
                else flag = 0;
                break;
        }
        return flag;
    }

    //we go through the flags and call the correct
    //way to display the user's desired output, flag 1 = part of speech parameter
    //                                          flag 2 = distinct parameter, flag 3 = reverse parameter.
    public static void goThroughFlagsMap(Multimap <Integer, Integer> flags, String [] split){
        Multimap <String, String> filteredMap = ArrayListMultimap.create();
        String key = split[0];//the word or key for out dictionary
        if(flags.containsValue(2)){
            getDistinctValues(key,flags,split);
        }
        else{
            filteredMap = getAllDefs(key);
            filteredMap = getDefsWithPOS(key,filteredMap,flags,split[1]);
            filteredMap = getReverseNotDistinct(key,filteredMap,flags);
            if(!flags.containsValue(3)){
                displayFiltered(filteredMap, key);
            }//if
        }//else
    }

    public static void errorMsgSender(int i, String [] split){
        if (i == 1){
            errorParam2(split[1]);
        }
        else if(i == 2){
            errorParam3(split[2]);
        }
        else if(i == 3){
            errorParam4(split[3]);
        }
    }

    //Checking if parameter 2 is a part of speech
    public static int checkParameterPOS(String p){
        int i = 0;
        if(p.equalsIgnoreCase("adjective")||p.equalsIgnoreCase("adverb")||p.equalsIgnoreCase("conjunction")
                ||p.equalsIgnoreCase("interjection")||p.equalsIgnoreCase("noun")||p.equalsIgnoreCase("preposition")
                ||p.equalsIgnoreCase("pronoun")||p.equalsIgnoreCase("verb")){
            //displayWithPOS();
            i = 1;
        }
        return i;
    }

    public static int checkParameterDis(String str){
        int i =0;
        if(str.equalsIgnoreCase("distinct")){
            i = 2;
        }
        return i;
    }

    public static int checkParameterRev(String str){
        int i =0;
        if(str.equalsIgnoreCase("reverse")){
            //result = true;
            i = 3;
        }
        return i;
    }


    //displays the keyword only with user chosen part of speech
    public static Multimap<String,String> getDefsWithPOS(String word, Multimap<String, String> map,Multimap <Integer, Integer> flags, String pos){
        if(flags.containsValue(1)) {
            Multimap<String, String> onlyPOS = ArrayListMultimap.create();
            for (Map.Entry<String, String> pair : map.entries()) {
                if(pair.getKey().equalsIgnoreCase(pos)){
                    onlyPOS.put(pair.getKey(), pair.getValue());
                }
            }//for
            map = onlyPOS;
        }//if
        return map;
    }//method end

    //reversing the entries when distinct is not a parameter
    public static Multimap<String,String> getReverseNotDistinct(String word,Multimap<String,String> map, Multimap <Integer, Integer> flags){
        //System.out.println("---------Calling getReverseNotDistinct() method--------");
        if(flags.containsValue(3)) {
            Keyword value = Keyword.valueOf(word.toUpperCase());
            Deque<String> pos = new ArrayDeque<>(); //POS
            Deque<String> defs = new ArrayDeque<>();//def
            //populating pos and defs dequeue with values from map so that when popped they are in reverse order.
            for (Map.Entry<String, String> pair : map.entries()) {
                pos.addFirst(pair.getKey());
                defs.addFirst(pair.getValue());
            }//for
            int size = pos.size();
            System.out.println("|");
            for(int i = 0; i < size; i++){
                System.out.println(value.name() + " [" + pos.pop() + "]: " + defs.pop());
            }
            System.out.println("|");
        }//if
        return map;
    }

    //Dsiplaying filtered entries when distinct is not a parameter
    public static void displayFiltered(Multimap <String, String> filtered, String word){
        Keyword value = Keyword.valueOf(word.toUpperCase());
        System.out.println("|");
        for (Map.Entry<String, String> pair : filtered.entries()) {
            System.out.println(value.name() + " [" + pair.getKey() + "]: " + pair.getValue());
        }
        System.out.println("|");
    }

    //Here we create a set map that will only take in distinct values when we iterate through our double array
    //the map will ony take in values that are not null.
    public static SetMultimap<String, String> getDistinctValues(String word, Multimap <Integer, Integer> flags, String[] split){
        //System.out.println("-------Calling getDistinctValues()------");
        SetMultimap<String, String> distinct = HashMultimap.create();//for storing distinct values

        Keyword value = Keyword.valueOf(word.toUpperCase());
        int cols;
        for(int i = 0; i < value.getLen(); i++ ){
            cols = value.getColLen(i);//column length of row i
            for(int j = 1; j < cols; j++) {
                if(value.getIndex(i,j) != null) {
                    distinct.put(value.getPOS(i), value.getIndex(i,j));
                }
            }//for
        }//for

        //Further filtering the maps with the users entered parameters
        distinct = getDistinctPOS(word,distinct, flags, split);
        distinct = getDisReverse(word,distinct,flags);
        if(!flags.containsValue(3)){
            displayDistinctVals(distinct, word);
        }

        //We will not display anything, this function displayDistinctValues() will take in whatever updated or not updated
        //version of distinct that was returned from getDistinct() and getReverse() functions and display.
        //Not going to call this function until we go through all the flags in gothroughFlagsMap() method.
        //displayDistinctVals(distinct, word, pos);
        return distinct;
    }

    //this method takes in a map that is distinct already and if our flag map contains 3 we will apply the filter of
    //part of speech to our map so that we will now only have defintions for a specified part of speech(pos)
    public static SetMultimap<String,String> getDistinctPOS(String word, SetMultimap<String,String> distinct, Multimap <Integer, Integer> flags, String [] split){
        //System.out.println("---------Calling getDistinctPOS()---------");
        SetMultimap<String, String> distinctPOS = HashMultimap.create();
        Keyword value = Keyword.valueOf(word.toUpperCase());
        if(flags.containsValue(1)) {
            for (Map.Entry<String, String> pair : distinct.entries()) {
                if(pair.getKey().equalsIgnoreCase(split[1])){
                   distinctPOS.put(pair.getKey(), pair.getValue());
                }
            }//for
            distinct = distinctPOS;
        }
        /* DEBUGGING ------------------------------------------------------------
        distinct = distinctPOS;
        System.out.println("---Did disitnct get uodated with only user entered pos?");
        for (Map.Entry<String, String> pair : distinct.entries()){
            System.out.println(pair.getKey() + " " + pair.getValue());
        }------------------------------------------------------------------------*/
        return distinct;
    }

    //This method takes in a distinct map of values and reverses it using dequeue.
    public static SetMultimap<String, String> getDisReverse(String word, SetMultimap<String,String> distinct, Multimap <Integer, Integer> flags){
        //System.out.println("---------Calling getDisReverse() method--------");
        if(flags.containsValue(3)) {
            Keyword value = Keyword.valueOf(word.toUpperCase());
            Deque<String> key = new ArrayDeque<>(); //POS
            Deque<String> distinctValue = new ArrayDeque<>();//def
            //populating key and distinct value with values form our distinct map
            for (Map.Entry<String, String> pair : distinct.entries()) {
                key.addFirst(pair.getKey());
                distinctValue.addFirst(pair.getValue());
            }//for
            int size = key.size();
            System.out.println("|");
            //outputting distinct reverse
            for(int i = 0; i < size; i++){
               System.out.println(value.name() + " [" + key.pop() + "]: " + distinctValue.pop());
            }
            System.out.println("|");
        }//if
        return distinct;
    }

    //Used to display all disitnct values once we have filtered it with all of the
    //users entered parameters
    public static void displayDistinctVals(SetMultimap <String, String> distinct, String word){
        //System.out.println("--------Calling displayDistinctValue()--------");
        Keyword value = Keyword.valueOf(word.toUpperCase());
        System.out.println("|");
        for (Map.Entry<String, String> pair : distinct.entries()) {
            System.out.println(value.name() + " [" + pair.getKey() + "]: " + pair.getValue());
        }
        System.out.println("|");
    }

    //getting the row of the part of speech from double array that we have in our Keyword class
    public static int getRowOfPOS(String pos){
        int row = 0;
        if(pos.equalsIgnoreCase("adjective")){
            row = 0;
        }
        else if (pos.equalsIgnoreCase("adverb")){
            row = 1;
        }
        else if (pos.equalsIgnoreCase("conjunction")){
            row = 2;
        }
        else if (pos.equalsIgnoreCase("interjection")){
            row = 3;
        }
        else if(pos.equalsIgnoreCase("noun")){
            row = 4;
        }
        else if(pos.equalsIgnoreCase("preposition")){
            row = 5;
        }
        else if(pos.equalsIgnoreCase("pronoun")){
            row = 6;
        }
        else if(pos.equalsIgnoreCase("verb")){
            row = 7;
        }

        return row;
    }

    //error messages to user
    public static void errorParam2(String errorParam){
        System.out.println("|");
        System.out.println("<The entered 2nd parameter ' " + errorParam + " ' " + "is NOT a part of speech.>");
        System.out.println("<The entered 2nd parameter ' " + errorParam + " ' " + "is NOT 'distinct'.>");
        System.out.println("<The entered 2nd parameter ' " + errorParam + " ' " + "is NOT 'reverse'.>");
        System.out.println("<The entered 2nd parameter ' " + errorParam + " ' " + "was disregarded.>");
        System.out.println("<The entered 2nd parameter should be a part of speech or 'distinct' or 'reverse'.>");
        System.out.println("|");
    }

    public static void errorParam3(String errorParam){
        System.out.println("|");
        System.out.println("<The entered 3rd parameter ' " + errorParam + " ' " + "is NOT 'distinct'.>");
        System.out.println("<The entered 3rd parameter ' " + errorParam + " ' " + "is NOT 'reverse'.>");
        System.out.println("<The entered 3rd parameter ' " + errorParam + " ' " + "was disregarded.>");
        System.out.println("<The entered 3rd parameter should be 'distinct' or 'reverse'.>");
        System.out.println("|");
    }

    public static void errorParam4(String errorParam){
        System.out.println("|");
        System.out.println("<The entered 4th parameter ' " + errorParam + " ' " + "is NOT 'reverse'.>");
        System.out.println("<The entered 4th parameter ' " + errorParam + " ' " + "was disregarded.>");
        System.out.println("<The entered 4th parameter should be 'reverse'.>");
        System.out.println("|");
    }


    //This function gets all the definitions of the dictionary and puts them into a map to be
    //filtered out more depending on the users parameters
    public static Multimap <String, String> getAllDefs(String word){
        //System.out.println("----getAllDefs()----");
        Multimap<String, String> allDefs = ArrayListMultimap.create();
        Keyword value = Keyword.valueOf(word.toUpperCase());
        int cols;
        for (int i = 0; i < value.getLen(); i++) {
            cols = value.getColLen(i);
            for (int j = 1; j < cols; j++) {
                if(value.getIndex(i,j) != null) {
                    allDefs.put(value.getPOS(i), value.getIndex(i,j));
                }
            }
        }
        return allDefs;
    }

    //when the user enters only a keyword as the parmater this method will be called
    public static void displayWithOneParameter(String word){
        //System.out.println("----Calling displayWithOneParameter()----");
        Keyword value = Keyword.valueOf(word.toUpperCase());
        int cols;
        System.out.println("|");
        for (int i = 0; i < value.getLen(); i++) {
            cols = value.getColLen(i);
            for (int j = 1; j < cols; j++) {
                if(value.getIndex(i,j) != null) {
                    System.out.println(value.name() + " [" + value.getPOS(i) + "] : " + value.getIndex(i, j));
                }
            }
        }
        System.out.println("|");
    }//end of method

    public static int getNumDefs(){
        EnumToGuavaMap.map.values().forEach((value) -> {
            defs++;
        });
        return defs;
    }

    public static void welcomeMessage(){
        System.out.println("! Loading data...");
        System.out.println("! Loading completed...");
        System.out.println("===== DICTIONARY 340 JAVA =====");
        System.out.println("Keywords: " + EnumToGuavaMap.map.size());
        System.out.println("Defintions: " + getNumDefs());
    }


    public static void main(String[] args) {
        //loading data
        EnumToGuavaMap.loadData();
        welcomeMessage();
        runProgram();
        System.out.println("\n" + "-----THANK YOU-----");
    }
}
