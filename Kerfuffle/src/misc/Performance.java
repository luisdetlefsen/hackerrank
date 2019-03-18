
package misc;

import java.util.Stack;

/**
 *
 * @author luisdetlefsen
 */
public class Performance {
    private static Stack<Long> startTimes = new Stack<>();
    public static String resultPrefix = "Time taken: ";
    
    public static void startCounting(){
        startTimes.push(System.currentTimeMillis());
    }
    
    public static long stopCounting(){
        Long l = startTimes.pop();
        return System.currentTimeMillis() - l;
    }
    
    public static String stopCountingStr() {
        Long l = startTimes.pop();
        return resultPrefix + (System.currentTimeMillis() - l) + " ms.";
    }
    
}
