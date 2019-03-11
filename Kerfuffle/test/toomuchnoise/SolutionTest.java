package toomuchnoise;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author luisdetlefsen
 */
public class SolutionTest {

    public SolutionTest() {
    }

    // Retrieve the data from a file since we have very long strings.
    private String[] loadCaseInput(String t) {
        String[] result = new String[4];
        Properties prop = new Properties();
        try (InputStream in = new FileInputStream("properties.properties")) {
            prop.load(in);
            result[0] = prop.getProperty("testn" + t);
            result[1] = prop.getProperty("testwords" + t);
            result[2] = prop.getProperty("testmessage" + t);
            result[3] = prop.getProperty("testresult" + t);
        } catch (Exception ex) {
            Logger.getLogger(SolutionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private String[] loadInput(String t) {
        String[] in = loadCaseInput(t);
        String input = in[0];
        input += System.lineSeparator();
        input += in[1];
        input += System.lineSeparator();
        input += in[2];
        input += System.lineSeparator();

        String[] result = new String[2];
        result[0] = input;
        result[1] = in[3];
        return result;
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
    public void testSample() {
        System.out.println("Testing the example");
        //Arrange
        String input = "7";
        input += System.lineSeparator();
        input += "place work a is lucid to great";
        input += System.lineSeparator();
        input += "revolucididentificriticismaggreaturecomplacehievementomorroworkshop";
        input += System.lineSeparator();

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String expResult = "lucid is a great place to work";
        Solution instance = new Solution();

        //Act
        String result = instance.solve();

        //Assert
        assertEquals(expResult, result);
    }

    @Test
    public void testLongestWordFirst() {
        System.out.println("Testing longest word first");
        //Arrange
        String input = "5";
        input += System.lineSeparator();
        input += "work workshop workout workplace house";
        input += System.lineSeparator();
        input += "workshopahousebcdworkoutplaceworkplacework";
        input += System.lineSeparator();

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String expResult = "workshop house workout workplace work";
        Solution instance = new Solution();

        //Act
        String result = instance.solve();

        //Assert
        assertEquals(expResult, result);
    }

    @Test
    public void testAdjacentWords() {
        System.out.println("Testing adjacent words");
        //Arrange
        String input = "5";
        input += System.lineSeparator();
        input += "one two four three five";
        input += System.lineSeparator();
        input += "onetwothreefourfive";
        input += System.lineSeparator();

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String expResult = "one two three four five";
        Solution instance = new Solution();

        //Act
        String result = instance.solve();

        //Assert
        assertEquals(expResult, result);
    }

    @Test
    public void testUnexpectedResult() {
        System.out.println("Testing unexpected result");
        //Arrange
        String input = "5";
        input += System.lineSeparator();
        input += "this is a bad test";
        input += System.lineSeparator();
        input += "revolucididentificriticismaggreaturecomplacehievementomorroworkshop";
        input += System.lineSeparator();

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String expResult = "this should not match the generated output";
        Solution instance = new Solution();

        //Act
        String result = instance.solve();

        //Assert
        assertNotEquals(expResult, result);
    }

//    @Ignore
    @Test
    public void testCase1() {
        System.out.println("Solving test #1");
        //Arrange               
        String[] in = loadInput("1");
        String input = in[0];

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String expResult = in[1];
        Solution instance = new Solution();

        //Act
        String result = instance.solve();

        //Assert
        assertEquals(expResult, result);
    }

    @Test
    public void testCase2() {
        System.out.println("Solving test #2");
        //Arrange           
        String[] in = loadInput("2");
        String input = in[0];

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String expResult = in[1];
        Solution instance = new Solution();

        //Act
        String result = instance.solve();

        //Assert
        assertEquals(expResult, result);
    }

//    @Ignore
    @Test
    public void testCase3() {
        System.out.println("Solving test #3");
        //Arrange
        String[] in = loadInput("3");
        String input = in[0];

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String expResult = in[1];
        Solution instance = new Solution();

        //Act
        String result = instance.solve();

        //Assert
        assertEquals(expResult, result);
    }

    @Test
    public void testCase4() {
        System.out.println("Solving test #4");
        //Arrange        
        String[] in = loadInput("4");
        String input = in[0];

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String expResult = in[1];
        Solution instance = new Solution();

        //Act
        String result = instance.solve();

        //Assert
        assertEquals(expResult, result);
    }
}
