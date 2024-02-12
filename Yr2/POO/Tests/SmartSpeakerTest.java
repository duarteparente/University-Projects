import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SmartSpeakerTest {

    /**
     * Default constructor for test class SmartBulbTest
     */
    public SmartSpeakerTest()
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
        SmartSpeaker smartSpeaker1 = new SmartSpeaker();
        assertTrue(smartSpeaker1!=null);
        smartSpeaker1 = new SmartSpeaker("s1");
        assertTrue(smartSpeaker1!=null);
        smartSpeaker1 = new SmartSpeaker("s1", "chanel",10,"brand");
        assertTrue(smartSpeaker1!=null);
        smartSpeaker1 = new SmartSpeaker("s1", 0.5, true,0.5,"chanel",10,"brand");
        assertTrue(smartSpeaker1!=null);
    }

    @Test
    public void testGetVolume() {
        SmartSpeaker smartSpeaker1 = new SmartSpeaker("s1",0.5, true, 0.5, "chanel",10,"brand");
        assertEquals(10, smartSpeaker1.getVolume());
    }

    @Test
    public void testGetChannel() {
        SmartSpeaker smartSpeaker1 = new SmartSpeaker("s1", 0.5, true, 0.5, "chanel",10,"brand");
        assertEquals("chanel", smartSpeaker1.getChannel());
    }

    @Test
    public void testSetChannel() {
        SmartSpeaker smartSpeaker1 = new SmartSpeaker("s1", 0.5, true, 0.5,"chanel1",10,"brand");
        smartSpeaker1.setChannel("chanel2");
        assertEquals("chanel2", smartSpeaker1.getChannel());
    }

    @Test
    public void testGetBrand(){
        SmartSpeaker smartSpeaker1 = new SmartSpeaker("s1", 0.5, true, 0.5, "chanel",10,"brand");
        assertEquals("brand", smartSpeaker1.getBrand());
    }

    @Test
    public void testSetBrand() {
        SmartSpeaker smartSpeaker1 = new SmartSpeaker("s1", 0.5, true, 0.5, "chanel1",10,"brand");
        smartSpeaker1.setBrand("brand2");
        assertEquals("brand2", smartSpeaker1.getBrand());
    }


}
