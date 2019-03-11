package toomuchnoise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import static misc.Misc.fastCompareOffset;

public class Solution {

    static boolean debug = true; //to display time taken on each function
    private final Scanner scanner = new Scanner(System.in);

    private String[] readInput() {
        Integer N = Integer.valueOf(scanner.nextLine());
        String[] words = scanner.nextLine().split(" "); //will be ordered by length to increase performance        
        return words;
    }

    private String readMessage() {
        return scanner.nextLine();
    }

    private Map<Character, List<char[]>> sortWords(final String[] words) {
        final Map<Character, List<char[]>> sortedWords = new HashMap<>();
        for (String s : words) {
            List<char[]> t = sortedWords.get(s.charAt(0));
            if (t == null) {
                t = new ArrayList<>();
            }
            t.add(s.toCharArray());
            sortedWords.put(s.charAt(0), t);
        }
        for (Character c : sortedWords.keySet()) {
            sortedWords.get(c).sort(Comparator.comparingInt(x -> x.length));
            Collections.reverse(sortedWords.get(c));
        }
        return sortedWords;
    }

    private String removeNoise(final Map<Character, List<char[]>> orderedWords, final String msgWithNoise) {
        final StringBuffer result = new StringBuffer();

        char[] chars = msgWithNoise.toCharArray();
        final int msgLength = chars.length;// msgWithNoise.length();
        for (int i = 0; i < chars.length; i++) {
            List<char[]> options = orderedWords.get(chars[i]);
            if (options == null) {
                continue;
            }

            for (char[] s : options) {
                int optionLength = s.length;
                int finalIdx = i + optionLength;

//                if (finalIdx <= msgLength && s.hashCode() == (new String(Arrays.copyOfRange(chars, i, finalIdx))).hashCode()) {
//                if (finalIdx <= msgLength && s.compareTo(new String(Arrays.copyOfRange(chars, i, finalIdx))) == 0) {
//if (finalIdx <= msgLength && fastCompare(s,Arrays.copyOfRange(chars, i, finalIdx)) == true) { //pass the whole msg instead of creating a subset
                if (finalIdx <= msgLength && fastCompareOffset(s, chars, i) == true) { //pass the whole msg instead of creating a subset
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
        long t0 = t1;
        String[] words = readInput();
        String messageWithNoise = readMessage();
        if (debug) {
            System.out.println("Time taken to read input: " + (System.currentTimeMillis() - t1) + "ms");
        }

        //2. Sort the input, to increase performance
        t1 = System.currentTimeMillis();
        Map<Character, List<char[]>> sortedWords = sortWords(words);
        if (debug) {
            System.out.println("Time taken to sort words: " + (System.currentTimeMillis() - t1) + "ms");
        }

        //3. Find the hidden message
        t1 = System.currentTimeMillis();
        String result = removeNoise(sortedWords, messageWithNoise);
        if (debug) {
            System.out.println("Time taken to remove noise: " + (System.currentTimeMillis() - t1) + "ms");
        }

        if (debug) {
            System.out.println("Total time to solve: " + (System.currentTimeMillis() - t0) + "ms");
        }
        return result;
    }

    /**
     * For testing: 7 place work a is lucid to great
     * revolucididentificriticismaggreaturecomplacehievementomorroworkshop
     *
     * @param args
     */
    public static void main(String[] args) {
        Solution s = new Solution();
        System.out.println(s.solve());
    }

}
