import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OwnerTest {

    /**
     * Default constructor for test class SmartBulbTest
     */
    public OwnerTest()
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
    public void testConstructor(){
        Owner owner = new Owner();
        assertTrue(owner!=null);
        owner = new Owner("name", 123456789);
        assertTrue(owner!=null);
    }

    @Test
    public void testGetName(){
        Owner owner = new Owner("name", 123456789);
        assertEquals("name",owner.getName());
    }

    @Test
    public void testSetName(){
        Owner owner = new Owner("name", 123456789);
        String newname="name2";
        owner.setName(newname);
        assertEquals("name2",owner.getName());
    }

    @Test
    public void testGetNIF(){
        Owner owner = new Owner("name", 123456789);
        assertEquals(123456789,owner.getNIF());
    }

    @Test
    public void testSetNIF(){
        Owner owner = new Owner("name", 123456789);
        int newnif=987654321;
        owner.setNIF(newnif);
        assertEquals(987654321,owner.getNIF());
    }




}
