package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class DB {


    public static void initDB() throws ClassNotFoundException, SQLException {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement st = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS drivers (" +
                    "Name varchar(45) NOT NULL PRIMARY KEY," +
                    "Country varchar(45) NOT NULL," +
                    "CTS decimal(2,1) NOT NULL," +
                    "SVA decimal(2,1) NOT NULL)";
            st.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS tracks (" +
                    "Name varchar(45) NOT NULL PRIMARY KEY," +
                    "Distance int NOT NULL," +
                    "NrLaps int NOT NULL)";
            st.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS cars (" +
                    "Class varchar(5) NOT NULL, " +
                    "Brand varchar(45) NOT NULL," +
                    "Model varchar(45) NOT NULL PRIMARY KEY," +
                    "Tires varchar(10) NOT NULL," +
                    "EngCapacity int NOT NULL," +
                    "Power int NOT NULL," +
                    "Reliability decimal(3,2)," +
                    "PAC decimal(3,2) NOT NULL," +
                    "DNF boolean NOT NULL," +
                    "TIME decimal(4,2)," +
                    "EletricEngPower int," +
                    "DetTax decimal(3,2)," +
                    "Tunning int)";
            st.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "Username varchar(45) NOT NULL PRIMARY KEY," +
                    "Password varchar(45) NOT NULL," +
                    "Type varchar(10) NOT NULL)";
            st.executeUpdate(sql);

        }
        predefineDB();
    }


    public static void predefineDB_drivers() throws SQLException{

            Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO drivers(Name,Country,CTS,SVA)"+
                              "VALUES('Nando Worries','United Kingdom',0.6,0.3),"+
                                        "('Sharl Leglerg','Monaco',0.5,0.2),"+
                                        "('Carlitos Science','Spain',0.4,0.4),"+
                                        "('Luis Honeymoon','United Kingdom',0.8,0.7),"+
                                        "('Anthony Extravaganza','Italy',0.3,0.2),"+
                                        "('Nikita Justspin','Russia',0.1,0.1),"+
                                        "('Ayrton Senna','Brazil',1,0.9),"+
                                        "('Alain Prostate','France',0.7,0.6),"+
                                        "('Yuki Tamagochi','Japan',0.4,0.5),"+
                                        "('Nick Debries','Netherlands',0.3,0.4),"+
                                        "('Michael Chewbacca','Germany',0.8,0.9),"+
                                        "('Nicki Cauda','Austria',0.6,0.8),"+
                                        "('Zhou No Guanyu','China',0.3,0.3),"+
                                        "('Pierre Petroly','France',0.5,0.5),"+
                                        "('Min Verstoppen','Netherlands',0.7,0.8),"+
                                        "('Miki Rakuten','Finland',0.5,0.6),"+
                                        "('Lance Troll','Canada',0.2,0.3),"+
                                        "('Fernando Mocho','Spain',0.5,0.6),"+
                                        "('James Blunt','United Kingdom',0.7,0.7),"+
                                        "('Nicholas Lapfifty','Canada',0.2,0.1)"+
                                        "ON DUPLICATE KEY UPDATE Name=Name");
    }

    public static void predefineDB_cars() throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();
        st.executeUpdate("INSERT INTO cars(Class,Brand,Model,Tires,EngCapacity,Power,Reliability,PAC,DNF,EletricEngPower,DetTax)"+
                            "VALUES('C1','Fiat','La Pata','Wet',6000,900,0.95,0.5,0,null,null),"+
                                     "('C2','Range Rover','Evoque','Hard',5000,187,0.80,0.4,0,null,null),"+
                                     "('GT','Red Bull Racing','RB16B','Softs',4000,900,1,0.7,0,null,0.35),"+
                                     "('SC','Mazda','Subito','Hards',2500,100,null,0.5,0,null,null),"+
                                     "('C1H','McLaren','Senna','Softs',8000,850,0.95,0.5,0,500,null),"+
                                     "('C2H','Ferrari','F40','Wet',8000,780,0.80,0.6,0,460,null),"+
                                     "('GTH','Mercedes-AMG','GT Black Series','Hards',7500,700,1,0.4,0,320,0.45),"+
                                     "('GT','Seat','Ibiza','Hards',3500,150,1,0.4,0,null,0.30),"+
                                     "('C1','Chevrolet','Impala','Softs',6000,200,0.95,0.5,0,null,null)"+
                                     "ON DUPLICATE KEY UPDATE Brand=Brand");
    }


    public static void predefineDB_users() throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();
        st.executeUpdate("INSERT INTO users(Username,Password,Type)"+
                            "VALUES('G0dMeister','benfica1904','admin'),"+
                                   "('T0pZen','ronaldothegoat','admin'),"+
                                   "('Cheater','sougey','admin'),"+
                                   "('samsepiOl','siruza','admin')"+
                                   "ON DUPLICATE KEY UPDATE Username=Username");
    }

    public static void predefineDB_tracks() throws SQLException{
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();
        st.executeUpdate("INSERT INTO tracks(Name,Distance,NrLaps)"+
                            "VALUES('Abu Dhabi',5554,58),"+
                                  "('Autódromo do Estoril',4182,60),"+
                                  "('Monza',5793,53)"+
                                  "ON DUPLICATE KEY UPDATE Name=Name");
    }

    public static void predefineDB() throws SQLException{
        predefineDB_drivers();
        predefineDB_tracks();
        predefineDB_users();
        predefineDB_cars();
    }

    
    public static void deleteDB(){
        try {
            Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
            Statement st = con.createStatement();
            //ResultSet resultSet =
            st.executeUpdate("DELETE FROM drivers");
            st.executeUpdate("DROP TABLE tracks");
            st.executeUpdate("DROP TABLE cars");
            st.executeUpdate("DROP TABLE championships");
            st.executeUpdate("DROP TABLE users");
            //st.executeUpdate("INSERT INTO drivers(Name,Country,CTS,SVA) VALUES('Hamilton','England',0.4,0.8) ON DUPLICATE KEY UPDATE Name=Name");

        }catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    //public boolean containsValue(Object value) {
    //    Driver t = (Driver) value;
    //    return this.containsValue(t.getName());
    //}
/*
    public List<Driver> get() throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();
        List<Driver> drivers = new ArrayList<>();
        String nome = null;
        String country = null;
        String CTS = null;
        String SVA = null;

       // List<String> drivers = new ArrayList<>();
        ResultSet rs = st.executeQuery("SELECT * FROM drivers");
        while (rs.next()) {
            nome = rs.getString("Name");
            country = rs.getString("Country");
            CTS = rs.getString("CTS");
            SVA = rs.getString("SVA");
            float CTS_float = Float.parseFloat(CTS);
            float SVA_float = Float.parseFloat(SVA);
            Driver driver = new Driver(nome, country, CTS_float, SVA_float);
            drivers.add(driver);
        }

        return drivers;
    }

 */


}
