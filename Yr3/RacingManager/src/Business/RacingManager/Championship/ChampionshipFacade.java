package Business.RacingManager.Championship;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Business.RacingManager.Car.Car;
import Database.CarDAO;
import Database.DriverDAO;
import Business.RacingManager.IChampionship;
import Database.TracksDAO;
import View.UI;

public class ChampionshipFacade implements IChampionship{

    private Championship championship;
    private DriverDAO drivers;
    private CarDAO cars;
    private TracksDAO tracks;


    public ChampionshipFacade(){
        this.drivers = DriverDAO.getInstance();
        this.cars = CarDAO.getInstance();
        this.tracks = TracksDAO.getInstance();
    }

    public void playersChoice(int numberPlayers) throws SQLException {

        List<Driver> d = this.drivers.getDrivers();
        List<Car> c = this.cars.getCars();

        Map<String, Entry> entries = new HashMap<>();


        for(int i=0; i<numberPlayers; i++){
            UI.clearScreen();
            UI.printDrivers(d);
            UI.printCars(c);
            String username = UI.chooseUsername(i+1);
            int opd = UI.chooseDriver();
            Driver driver = d.get(opd-1);
            int opc = UI.chooseCar();
            Car car = c.get(opc-1);
            Entry e = new Entry(car,driver);
            entries.put(username, e);
            d.remove(driver);
            c.remove(car);
        }

        List<Track> tracks = this.tracks.getTracks();


        this.championship = new Championship("Taça Pistão",tracks,entries);
        simulate();

    }

    public void configureTrack() throws NumberFormatException, SQLException, InterruptedException {
        List<String> ans = UI.createTrack();
        this.tracks.setTrack(ans.get(0), Integer.parseInt(ans.get(1)), Integer.parseInt(ans.get(2)));
    }

    public void configureDriver() throws NumberFormatException, SQLException, InterruptedException {
        List<String> ans = UI.createDriver();
        this.drivers.setDriver(ans.get(0), ans.get(1), Float.parseFloat(ans.get(2)), Float.parseFloat(ans.get(3)));
    }

    public void simulate(){
        this.championship.simulate();

    }

}