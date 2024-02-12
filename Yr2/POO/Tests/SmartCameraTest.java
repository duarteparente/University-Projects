import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SmartCameraTest {

    /**
     * Default constructor for test class SmartBulbTest
     */
    public SmartCameraTest()
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
        SmartCamera smartCam1 = new SmartCamera();
        assertTrue(smartCam1!=null);
        smartCam1 = new SmartCamera("c1");
        assertTrue(smartCam1!=null);
        smartCam1 = new SmartCamera("c1", "", 400);
        assertTrue(smartCam1!=null);
        smartCam1 = new SmartCamera("c1",0.5,true,0.5,"", 400);
        assertTrue(smartCam1!=null);
    }

    @Test
    public void testGetResolution() {
        SmartCamera smartCam1 = new SmartCamera();
        smartCam1 = new SmartCamera("c1",0.5,true,0.5, "(1080x720)", 400);
        assertEquals("(1080x720)", smartCam1.getResolution());
    }

    @Test
    public void testSetResolution() {
        SmartCamera smartCam1 = new SmartCamera("c1");
        smartCam1.setResolution("(1080x720)");
        assertEquals("(1080x720)", smartCam1.getResolution());
        smartCam1.setResolution("(720x720)");
        assertEquals("(720x720)",smartCam1.getResolution());
    }

    @Test
    public void testGetFilesize() {
        SmartCamera smartCam1 = new SmartCamera();
        smartCam1 = new SmartCamera("c1",0.5,true,0.5,"", 400);
        assertEquals(400, smartCam1.getFilesize());
    }

    @Test
    public void testSetFilesize() {
        SmartCamera smartCam1 = new SmartCamera("c1");
        smartCam1.setFilesize(400);
        assertEquals(400, smartCam1.getFilesize());
        smartCam1.setFilesize(200);
        assertEquals(200, smartCam1.getFilesize());
    }

}
