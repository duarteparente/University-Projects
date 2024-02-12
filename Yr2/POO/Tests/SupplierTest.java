import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SupplierTest {

    /**
     * Default constructor for test class SmartBulbTest
     */
    public SupplierTest()
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
        Supplier supplier = new Supplier();
        assertTrue(supplier!=null);
        supplier = new Supplier("company1",0.15,2.85);
        assertTrue(supplier!=null);
    }

    @Test
    public void testGetCompany(){
        Supplier supplier = new Supplier("company1",0.15,2.85);
        assertEquals("company1",supplier.getCompany());
    }


    @Test
    public void testSetCompany(){
        Supplier supplier = new Supplier("company1",0.15,2.85);
        String newcompany="company2";
        supplier.setCompany(newcompany);
        assertEquals("company2",supplier.getCompany());
    }

    @Test
    public void testGetBasecost(){
        Supplier supplier = new Supplier("company1",0.15,2.85);
        assertEquals(0.15,supplier.getBaseCost());
    }

    @Test
    public void testSetBasecost(){
        Supplier supplier = new Supplier("company1",0.15,2.85);
        double newbc=0.20;
        supplier.setBaseCost(newbc);
        assertEquals(0.20,supplier.getBaseCost());
    }

    @Test
    public void testGetTaxfactor(){
        Supplier supplier = new Supplier("company1",0.15,2.85);
        assertEquals(2.85,supplier.getTaxFactor());
    }

    @Test
    public void testSetTaxfactor(){
        Supplier supplier = new Supplier("company1",0.15,2.85);
        double newtf=2.75;
        supplier.setTaxFactor(newtf);
        assertEquals(2.75,supplier.getTaxFactor());
    }
}
