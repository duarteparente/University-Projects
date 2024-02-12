package Business.RacingManager;

import java.sql.SQLException;

public interface IUser {

    boolean login (String user, String password, String Type) throws SQLException;

    boolean create_user(String user, String password) throws SQLException;
    
}
