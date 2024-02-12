import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SmartBulbTest {

    /**
     * Default constructor for test class SmartBulbTest
     */
    public SmartBulbTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }

    @Test
    public void testContructor() {
        SmartBulb smartBulb1 = new SmartBulb();
        assertTrue(smartBulb1!=null);
        smartBulb1 = new SmartBulb("b1");
        assertTrue(smartBulb1!=null);
        smartBulb1 = new SmartBulb("b1", SmartBulb.Neutral,60);
        assertTrue(smartBulb1!=null);
        smartBulb1 = new SmartBulb("b1", 0.5, true,0.5,SmartBulb.Neutral,60);
        assertTrue(smartBulb1!=null);
    }

    @Test
    public void testGetTone() {
        SmartBulb smartBulb1 = new SmartBulb("b1",0.5, true,0.5, SmartBulb.Cold,60);
        assertEquals(1, smartBulb1.getTone());
        smartBulb1 = new SmartBulb("b1", 0.5,true,0.5,SmartBulb.Neutral,60);
        assertEquals(0, smartBulb1.getTone());
        smartBulb1 = new SmartBulb("b1",0.5,true, 0.5,SmartBulb.Warm,60);
        assertEquals(2, smartBulb1.getTone());
        smartBulb1 = new SmartBulb("b1");
        assertEquals(SmartBulb.Neutral, smartBulb1.getTone());
    }

    @Test
    public void testSetTone() {
        SmartBulb smartBulb1 = new SmartBulb("b1",0.5,true,0.5,SmartBulb.Neutral,60);
        smartBulb1.setTone(2);
        assertEquals(SmartBulb.Warm, smartBulb1.getTone());
        smartBulb1.setTone(10);
        assertEquals(SmartBulb.Warm, smartBulb1.getTone());
        smartBulb1.setTone(-10);
        assertEquals(SmartBulb.Neutral, smartBulb1.getTone());
    }

    @Test
    public void testGetDimension() {
        SmartBulb smartBulb1 = new SmartBulb("b1",0.5,true,0.5,SmartBulb.Cold,60);
        assertEquals(60, smartBulb1.getDimension());
    }

    @Test
    public void testSetDimension() {
        SmartBulb smartBulb1 = new SmartBulb("b1",0.5,true,0.5,SmartBulb.Cold,60);
        smartBulb1.setDimension(60);
        assertEquals(60, smartBulb1.getDimension());
    }

}
