import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class SmartHouseTest {

    /**
     * Default constructor for test class SmartBulbTest
     */
    public SmartHouseTest()
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
        SmartHouse smartHouse1 = new SmartHouse();
        assertTrue(smartHouse1!=null);
        Owner owner = new Owner();
        HashMap devices = new HashMap<>();
        HashMap houseparts = new HashMap<>();
        TreeSet bills = new TreeSet();
        Supplier company= new Supplier();
        smartHouse1 = new SmartHouse(owner,devices,houseparts,bills,company);
        assertTrue(smartHouse1!=null);
    }

    @Test
    public void testGetOwner() {
        SmartHouse smartHouse1 = new SmartHouse();
        Owner owner = new Owner();
        HashMap devices = new HashMap<>();
        HashMap houseparts = new HashMap<>();Owner owner1 = new Owner();
        TreeSet bills = new TreeSet();
        Supplier company= new Supplier();
        smartHouse1 = new SmartHouse(owner,devices,houseparts,bills,company);
        assertEquals(owner, smartHouse1.getOwner());
    }

    @Test
    public void testSetOwner(){
        SmartHouse smartHouse1 = new SmartHouse();
        Owner owner1 = new Owner();
        Owner owner2 = new Owner();
        smartHouse1.setOwner(owner1);
        assertEquals(owner1,smartHouse1.getOwner());
        smartHouse1.setOwner(owner2);
        assertEquals(owner2,smartHouse1.getOwner());
    }

    @Test
    public void testGetDevices(){
        SmartHouse smartHouse1 = new SmartHouse();
        Owner owner = new Owner();
        HashMap devices = new HashMap<>();
        HashMap houseparts = new HashMap<>();Owner owner1 = new Owner();
        TreeSet bills = new TreeSet();
        Supplier company= new Supplier();
        smartHouse1 = new SmartHouse(owner,devices,houseparts,bills,company);
        assertEquals(devices,smartHouse1.getDevices());
    }

    @Test
    public void testSetDevices(){
        SmartHouse smartHouse1 = new SmartHouse();
        HashMap devices1 = new HashMap();
        HashMap devices2 = new HashMap();
        smartHouse1.setDevices(devices1);
        assertEquals(devices1,smartHouse1.getDevices());
        smartHouse1.setDevices(devices2);
        assertEquals(devices1,smartHouse1.getDevices());
    }

    @Test
    public void testGetHouseparts(){
        Owner owner = new Owner();
        HashMap devices = new HashMap<>();
        HashMap houseparts = new HashMap<>();Owner owner1 = new Owner();
        TreeSet bills = new TreeSet();
        Supplier company= new Supplier();
        SmartHouse smartHouse1 = new SmartHouse(owner,devices,houseparts,bills,company);
        assertEquals(houseparts,smartHouse1.getHouseParts());
    }

    @Test
    public void testSetHouseparts(){
        SmartHouse smartHouse1 = new SmartHouse();
        HashMap houseparts1 = new HashMap();
        HashMap houseparts2 = new HashMap();
        smartHouse1.setHouseParts(houseparts1);
        assertEquals(houseparts1,smartHouse1.getDevices());
        smartHouse1.setHouseParts(houseparts2);
        assertEquals(houseparts2,smartHouse1.getDevices());
    }

    @Test
    public void testGetBills(){
        SmartHouse smartHouse1 = new SmartHouse();
        Owner owner = new Owner();
        HashMap devices = new HashMap<>();
        HashMap houseparts = new HashMap<>();
        TreeSet bills = new TreeSet();
        Supplier company= new Supplier();
        smartHouse1 = new SmartHouse(owner,devices,houseparts,bills,company);
        assertEquals(bills,smartHouse1.getBills());
    }

    @Test
    public void testSetBills(){
        SmartHouse smartHouse1 = new SmartHouse();
        TreeSet bills1 = new TreeSet();
        TreeSet bills2 = new TreeSet();
        smartHouse1.setBills(bills1);
        assertEquals(bills1,smartHouse1.getBills());
        smartHouse1.setBills(bills2);
        assertEquals(bills2,smartHouse1.getBills());
    }

    @Test
    public void testSmartHouse(){
        SmartHouse house = new SmartHouse();
        SmartBulb bulb1 = new SmartBulb("bulb1",2,true,0.5,2,60);
        SmartBulb bulb2 = new SmartBulb("bulb2",2,false,0.5,2,60);
        SmartCamera cam1 = new SmartCamera("cam1",4,false,0.5,"",400);
        SmartCamera cam2 = new SmartCamera("cam2",4,true,0.5,"",400);
        SmartSpeaker speaker1 = new SmartSpeaker("speaker1",4,true,0.5,"RFM",50,"brand");
        SmartSpeaker speaker2 = new SmartSpeaker("speaker2",4,false,0.5,"RFM",50,"brand");
        house.addRoom("sala");
        house.addRoom("quarto");
        assertTrue(house.hasRoom("sala"));
        assertFalse(house.hasRoom("cozinha"));
        house.addDeviceToRoom("sala",bulb1);
        assertTrue(house.roomHasDevice("sala", bulb1.getId()));
        assertFalse(house.roomHasDevice("sala",bulb2.getId()));
    }
}
