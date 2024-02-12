package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Business.RacingManager.Car.*;

public class CarDAO {

    private static CarDAO singleton = null;

    public static CarDAO getInstance() {
        if (CarDAO.singleton == null) {
            CarDAO.singleton = new CarDAO();
        }
        return CarDAO.singleton;
    }

    public List<Car> getCars() throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();
        List<Car> cars = new ArrayList<>();
        String car_class = null;
        String brand = null;
        String model = null;
        String tires = null;
        int engCapacity;
        int power;
        float reliability;
        float PAC;
        boolean DNF;
        int EletricEngPower;
        float DetTax;
        int tunning;
        Car car = null;

        ResultSet rs = st.executeQuery("SELECT * FROM cars");
        while (rs.next()) {
            car_class = rs.getString("Class");
            brand = rs.getString("Brand");
            model = rs.getString("Model");
            tires = rs.getString("Tires");
            engCapacity = rs.getInt("EngCapacity");
            power = rs.getInt("Power");
            reliability = rs.getFloat("Reliability");
            PAC = rs.getFloat("PAC");
            DNF = rs.getBoolean("DNF");
            EletricEngPower = rs.getInt("EletricEngPower");
            DetTax = rs.getFloat("DetTax");
            tunning = rs.getInt("Tunning");
            switch (car_class) {
                case "C1":
                    car = new C1(brand, model, engCapacity, power, reliability, PAC, tunning);
                case "C1H":
                    car = new C1H(brand, model, engCapacity, power, reliability, PAC, tunning, EletricEngPower);
                case "C2":
                    car = new C2(brand, model, engCapacity, power, reliability, PAC, tunning);
                case "C2H":
                    car = new C2H(brand, model, engCapacity, power, reliability, PAC, tunning, EletricEngPower);
                case "GT":
                    car = new C1(brand, model, engCapacity, power, reliability, PAC, tunning);
                case "GTH":
                    car = new C1H(brand, model, engCapacity, power, reliability, PAC, tunning, EletricEngPower);
                case "SC":
                    car = new C1(brand, model, engCapacity, power, reliability, PAC, tunning);
            }
            cars.add(car);
        }

        return cars;
    }

    public void setCar(String car_class, String brand, String model, String tires, int engcap, int power, double reliability, double pac, double dnf) throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();

        st.executeUpdate("INSERT INTO cars(Class,Brand,Model,Tires,EngCapacity,Power,Reliability,PAC,DNF)" +
                "VALUES('" + car_class + "','" + brand + "','" + model + "','" + tires + "','" + engcap + "','" + power + "','" + reliability + "','" + pac + "','" + dnf + "')" +
                "ON DUPLICATE KEY UPDATE Model=Model");
    }

    public void setHibryd(String car_class, String brand, String model, String tires, int engcap, int power, double reliability, double pac, double dnf, int electengpower) throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();

        st.executeUpdate("INSERT INTO cars(Class,Brand,Model,Tires,EngCapacity,Power,Reliability,PAC,DNF,EletricEngPower)" +
                "VALUES('" + car_class + "','" + brand + "','" + model + "','" + tires + "','" + engcap + "','" + power + "','" + reliability + "','" + pac + "','" + dnf + "','" + electengpower + "')" +
                "ON DUPLICATE KEY UPDATE Model=Model");
    }

    public void setGT(String car_class, String brand, String model, String tires, int engcap, int power, double reliability, double pac, double dnf, float dettax) throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();

        st.executeUpdate("INSERT INTO cars(Class,Brand,Model,Tires,EngCapacity,Power,Reliability,PAC,DNF,DetTax)" +
                "VALUES('" + car_class + "','" + brand + "','" + model + "','" + tires + "','" + engcap + "','" + power + "','" + reliability + "','" + pac + "','" + dnf + "','" + dettax + "')" +
                "ON DUPLICATE KEY UPDATE Model=Model");
    }
}

