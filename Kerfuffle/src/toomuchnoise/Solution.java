package toomuchnoise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Solution {

    static boolean debug = false; //to display time taken on each function
    private final Scanner scanner = new Scanner(System.in);

    private String[] readInput() {
        Integer N = Integer.valueOf(scanner.nextLine()); 
        String[] words = scanner.nextLine().split(" "); //will be ordered by length to increase performance        
        return words;
    }

    private String readMessage() {
        return scanner.nextLine();
    }

    private Map<Character, List<String>> sortWords(final String[] words) {
        final Map<Character, List<String>> sortedWords = new HashMap<>();
        for (String s : words) {
            List<String> t = sortedWords.get(s.charAt(0));
            if (t == null) {
                t = new ArrayList<>();
            }
            t.add(s);
            sortedWords.put(s.charAt(0), t);
        }
        for (Character c : sortedWords.keySet()) {
            sortedWords.get(c).sort(Comparator.comparingInt(String::length));
            Collections.reverse(sortedWords.get(c));
        }
        return sortedWords;
    }

    private String removeNoise(final Map<Character, List<String>> orderedWords, final String msgWithNoise) {
        final StringBuffer result = new StringBuffer();

        char[] chars = msgWithNoise.toCharArray();
        final int msgLength = msgWithNoise.length();
        for (int i = 0; i < chars.length; i++) {
            List<String> options = orderedWords.get(chars[i]);
            if (options == null) {
                continue;
            }

            for (String s : options) {
                int optionLength = s.length();
                int finalIdx = i + optionLength;

                if (finalIdx <= msgLength && s.compareTo(new String(Arrays.copyOfRange(chars, i, finalIdx))) == 0) {
                    i += optionLength - 1; //substract one since it will be incremented in the for loop
                    result.append(" ").append(s);
                    break;
                }
            }
        }
        return result.toString().trim();
    }

    public String solve() {
        //1. Read the input
        long t1 = System.currentTimeMillis();        
        String[] words = readInput();
        String messageWithNoise = readMessage();
        if (debug) {
            System.out.println("Time taken to read input: " + (System.currentTimeMillis() - t1) + "ms");
        }

        //2. Sort the input, to increase performance
        t1 = System.currentTimeMillis();                
        Map<Character, List<String>> sortedWords = sortWords(words);
        if (debug) {
            System.out.println("Time taken to sort words: " + (System.currentTimeMillis() - t1)+ "ms");
        }

        //3. Find the hidden message
        t1 = System.currentTimeMillis();                
        String result = removeNoise(sortedWords, messageWithNoise);
        if (debug) {
            System.out.println("Time taken to remove noise: " + (System.currentTimeMillis() - t1)+ "ms");
        }
        return result;
    }

    public static void main(String[] args) {
        Solution s = new Solution();
        long t1 = System.currentTimeMillis();
        System.out.println(s.solve());
        if (debug) {
            System.out.println("Total time to solve: " + (System.currentTimeMillis() - t1)+ "ms");
        }

    }

}
