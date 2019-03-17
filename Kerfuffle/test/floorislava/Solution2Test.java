/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorislava;

import java.io.ByteArrayInputStream;
import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author luisdetlefsen
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Solution2Test {
    
     Solution2 instance ;
    
    public Solution2Test() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
//        instance = new Solution2();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCase() {
        System.out.println("Solving test sample");
        //Arrange           
        String in = "3 4 1 16";
        System.setIn(new ByteArrayInputStream(in.getBytes()));

        int expResult = 6;
        instance = new Solution2();

        //Act
        int result = instance.solve();

        //Assert
        assertEquals(expResult, result);
    }

    
    @Test
    public void testCase0() {
        System.out.println("Solving test #0");
        //Arrange           
        String in = "3 3 3 15";
        System.setIn(new ByteArrayInputStream(in.getBytes()));

        int expResult = 4;
        instance = new Solution2();

        //Act
        int result = instance.solve();

        //Assert
        assertEquals(expResult, result);
    }

    
    @Test
    public void testCase1() {
        System.out.println("Solving test #1");
        //Arrange           
        String in = "10 10 2 200";
        System.setIn(new ByteArrayInputStream(in.getBytes()));

        int expResult = 14;
        instance = new Solution2();

        //Act
        int result = instance.solve();

        //Assert
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void testCase2() {
        System.out.println("Solving test #2");
        //Arrange           
        String in = "50 50 7 3700";
        System.setIn(new ByteArrayInputStream(in.getBytes()));

        int expResult = 89;
        instance = new Solution2();

        //Act
        int result = instance.solve();

        //Assert
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void testCase3() {
        System.out.println("Solving test #3");
        //Arrange           
        String in = "100 100 4 20000";
        System.setIn(new ByteArrayInputStream(in.getBytes()));

        int expResult = 142;
        instance = new Solution2();

        //Act
        int result = instance.solve();

        //Assert
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void testCase4() {
        System.out.println("Solving test #4");
        //Arrange           
        String in = "200 200 5 40000";
        System.setIn(new ByteArrayInputStream(in.getBytes()));

        int expResult = -1;
        instance = new Solution2();

        //Act
        int result = instance.solve();

        //Assert
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCase5() {
        System.out.println("Solving test #5");
        //Arrange           
        String in = "170 170 5 50000";
        System.setIn(new ByteArrayInputStream(in.getBytes()));

        int expResult = 261;
        instance = new Solution2();

        //Act
        int result = instance.solve();

        //Assert
        assertEquals(expResult, result);
    }
    
    
}
