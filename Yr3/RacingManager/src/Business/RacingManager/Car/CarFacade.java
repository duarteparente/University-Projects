package Business.RacingManager.Car;

import Business.RacingManager.ICar;
import Database.CarDAO;

public class CarFacade implements ICar{

    CarDAO carDAO;
    Car car;

    public CarFacade(){
        this.carDAO = CarDAO.getInstance();
    }
    
}
