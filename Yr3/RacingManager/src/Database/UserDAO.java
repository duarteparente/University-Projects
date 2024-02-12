package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Business.RacingManager.User.User;

public class UserDAO {

    private static UserDAO singleton = null;

    public static UserDAO getInstance() {
        if (UserDAO.singleton == null) {
            UserDAO.singleton = new UserDAO();
        }
        return UserDAO.singleton;
    }

    public List<User> getUsers() throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();
        List<User> users = new ArrayList<>();
        String username = null;
        String password = null;
        String type = null;

        ResultSet rs = st.executeQuery("SELECT * FROM users");
        while (rs.next()) {
            username = rs.getString("Username");
            password = rs.getString("Password");
            type = rs.getString("Type");

            User user = new User(username,password,type);
            users.add(user);
        }
        return users;
    }
    
     public void setUser(String username,String password) throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();

        st.executeUpdate("INSERT INTO users(Username,Password,Type)"+
                            "VALUES('"+username+"','"+password+"','player')"+
                              "ON DUPLICATE KEY UPDATE Username=Username");
    }
    
     public User getUser(String username) throws SQLException{
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();
        String password = null;
        String type = null;
        ResultSet rs = st.executeQuery("SELECT * from users WHERE Username ='"+username+"'");
        rs.next();
        password = rs.getString("Password");
        type = rs.getString("Type");

        return new User(username,password,type);

    }

    public boolean checkPassword(String username,String password,String type) throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();
        String validPass = null;
        String validType = null;

        ResultSet rs = st.executeQuery("SELECT Password FROM users WHERE Username ='"+username+"'");
        rs.next();
        validPass = rs.getString("Password");

        rs = st.executeQuery("SELECT Type from users WHERE Username = '"+username+"'");
        rs.next();
        validType = rs.getString("Type");

        return(validPass.equals(password) && validType.equals(type));
    }

    public boolean userExists(String username) throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery("SELECT Username FROM users");
        while(rs.next()){
            if(rs.getString("Username").equals(username)){
                return true;
            }
        }
        return false;
    }
    

}
