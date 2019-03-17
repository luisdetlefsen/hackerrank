
package floorislava;

import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author luisdetlefsen
 */
public class NodeTest {
    
    public NodeTest() {
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
    public void testCompareToEquals() {
        System.out.println("compareTo");
        Node o = new Node();
        Node instance = new Node();
        o.x = 1d;
        o.y = 1d;
        instance.x = 1d;
        instance.y = 1d;
        
        int expResult = 0;
        int result = instance.compareTo(o);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCompareToAgtB() {
        System.out.println("compareTo");
        Node o = new Node();
        Node instance = new Node();
        o.x = 1d;
        o.y = 1d;
        instance.x = 1d;
        instance.y = 2d;
        
        int expResult = 1;
        int result = instance.compareTo(o);
        assert(result >= expResult);
        
    }
    
    @Test
    public void testCompareToAltB() {
        System.out.println("compareTo");
        Node o = new Node();
        Node instance = new Node();
        o.x = 1d;
        o.y = 1d;
        instance.x = 1d;
        instance.y = 0d;
        
        int expResult = -1;
        int result = instance.compareTo(o);
        assert(result <= expResult);
    }

    @Test
    public void testPrint() {
        System.out.println("print");
        Node instance = new Node();       
        instance.print();
        assertEquals(1, 1);
    }
    
}
