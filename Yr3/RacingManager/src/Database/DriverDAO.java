package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Business.RacingManager.Championship.Driver;

public class DriverDAO {

    private static DriverDAO singleton = null;

    public static DriverDAO getInstance() {
            if (DriverDAO.singleton == null) {
                DriverDAO.singleton = new DriverDAO();
            }
    return DriverDAO.singleton;
    }

    public List<Driver> getDrivers() throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();
        List<Driver> drivers = new ArrayList<>();
        String nome = null;
        String country = null;
        String CTS = null;
        String SVA = null;

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

    public void setDriver(String name,String country,double cts,double sva) throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();

        st.executeUpdate("INSERT INTO drivers(Name,Country,CTS,SVA)"+
                "VALUES('"+name+"','"+country+"','"+cts+"','"+sva+"')"+
                "ON DUPLICATE KEY UPDATE Name=Name");
    }

}

