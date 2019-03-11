package misc;

/**
 *
 * @author luisdetlefsen
 */
public class Misc {

    static public boolean fastCompare(char[] s1, char[] s2) {
        int l = s1.length;
        for (int i = 0; i < l / 2 + 1; i++) {
            if (s1[i] != s2[i] || s1[l - i - 1] != s2[l - i - 1]) {
                return false;
            }
        }
        return true;
    }

    static public boolean fastCompareOffset(char[] s1, char[] s2, int s2Offset) {
        int l = s1.length;
        for (int i = 0; i < l / 2 + 1; i++) {
            if (s1[i] != s2[i + s2Offset] || s1[l - i - 1] != s2[l - i - 1 + s2Offset]) {
                return false;
            }
        }
        return true;
    }

}
