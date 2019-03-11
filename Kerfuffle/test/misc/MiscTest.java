package misc;

import static junit.framework.Assert.assertEquals;
import static misc.Misc.fastCompare;
import static misc.Misc.fastCompareOffset;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author luisdetlefsen
 */
public class MiscTest {

    public MiscTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFastCompareOffset() {
        String s1 = "abcdef";
        String s2 = "zzzabcdef";

        boolean r = fastCompareOffset(s1.toCharArray(), s2.toCharArray(), 3);

        assertEquals(r, true);
    }

    @Test
    public void testFastCompareOffsetFalse() {
        String s1 = "abcdef";
        String s2 = "zzzabhdef";

        boolean r = fastCompareOffset(s1.toCharArray(), s2.toCharArray(), 3);

        assertEquals(r, false);
    }

    @Test
    public void testFastCompare() {
        String s1 = "abcdef";
        String s2 = "abcdef";

        boolean r = fastCompare(s1.toCharArray(), s2.toCharArray());

        assertEquals(r, true);
    }

    @Test
    public void testFastCompare2() {
        String s1 = "abcdef";
        String s2 = "abcdeg";

        boolean r = fastCompare(s1.toCharArray(), s2.toCharArray());

        assertEquals(r, false);
    }

    @Test
    public void testFastCompare3() {
        String s1 = "abddef";
        String s2 = "abcdef";

        boolean r = fastCompare(s1.toCharArray(), s2.toCharArray());

        assertEquals(r, false);
    }

    @Test
    public void testFastCompare4() {
        String s1 = "abcde";
        String s2 = "abcde";

        boolean r = fastCompare(s1.toCharArray(), s2.toCharArray());

        assertEquals(r, true);
    }

    @Test
    public void testFastCompare5() {
        String s1 = "abcde";
        String s2 = "abdde";

        boolean r = fastCompare(s1.toCharArray(), s2.toCharArray());

        assertEquals(r, false);
    }

}
