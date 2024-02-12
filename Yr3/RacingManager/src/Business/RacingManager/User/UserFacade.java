package Business.RacingManager.User;

import Business.RacingManager.IUser;
import Database.UserDAO;

import java.sql.SQLException;

public class UserFacade implements IUser{

    UserDAO userDAO;
    User user;

    public UserFacade () {
        this.userDAO = UserDAO.getInstance();
    }

    public boolean login (String user, String password, String type) throws SQLException {
        boolean res = false;
        if (userDAO.userExists(user)){
            if (userDAO.checkPassword(user,password,type)){
                this.user = userDAO.getUser(user);
                return true;
            }

        }
        return res;
    }

    public boolean create_user(String user, String password) throws SQLException {
        if (!userDAO.userExists(user)){
            userDAO.setUser(user,password);
            return true;
        }
        return false;
    }

}
