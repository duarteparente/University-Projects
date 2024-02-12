import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class BillTest {

    /**
     * Default constructor for test class SmartBulbTest
     */
    public BillTest()
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
        Bill bill = new Bill();
        assertTrue(bill!=null);
        Owner o = new Owner();
        bill=new Bill(LocalDate.now(),LocalDate.parse("2022-05-10"),0,"company",o);
        assertTrue(bill!=null);
    }

    @Test
    public void testGetStartDate(){
        Owner o = new Owner();
        Bill bill = new Bill(LocalDate.now(),LocalDate.parse("2022-05-10"),0,"company",o);
        assertEquals(LocalDate.now(),bill.getStartDate());
    }

    @Test
    public void testSetStartDate(){
        Owner o = new Owner();
        Bill bill = new Bill(LocalDate.now(),LocalDate.parse("2022-05-10"),0,"company",o);
        LocalDate newdate = LocalDate.parse("2022-05-07");
        bill.setStartDate(newdate);
        assertEquals(newdate,bill.getStartDate());
    }

    @Test
    public void testGetFinalDate(){
        Owner o = new Owner();
        Bill bill = new Bill(LocalDate.now(),LocalDate.parse("2022-05-10"),0,"company",o);
        assertEquals(LocalDate.parse("2022-05-10"),bill.getFinalDate());
    }

    @Test
    public void testSetFinalDate(){
        Owner o = new Owner();
        Bill bill = new Bill(LocalDate.now(),LocalDate.parse("2022-05-10"),0,"company" ,o);
        LocalDate newdate = LocalDate.parse("2022-05-15");
        bill.setFinalDate(newdate);
        assertEquals(newdate,bill.getFinalDate());
    }

    @Test
    public void testGetValue(){
        Bill bill = new Bill();
        assertEquals(0,bill.getValue());
    }

    @Test
    public void testSetValue(){
        Bill bill = new Bill();
        double newvalue=1;
        bill.setValue(newvalue);
        assertEquals(1,bill.getValue());
    }

    @Test
    public void testGetCompany(){
        Owner o = new Owner();
        Bill bill = new Bill(LocalDate.now(),LocalDate.parse("2022-05-10"),0,"company",o);
        assertEquals("company",bill.getCompany());
    }

    @Test
    public void testSetCompany(){
        Owner o = new Owner();
        Bill bill = new Bill(LocalDate.now(),LocalDate.parse("2022-05-10"),0,"company", o);
        String newcompany="company2";
        bill.setCompany(newcompany);
        assertEquals("company2",bill.getCompany());
    }

}
