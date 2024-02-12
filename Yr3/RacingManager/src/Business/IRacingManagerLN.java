package Business;

import java.sql.SQLException;

public interface IRacingManagerLN {

    boolean login(String user, String password, String type) throws SQLException;

    boolean create_user(String user, String password) throws SQLException;

    void playersChoice(int numberPlayers) throws SQLException;

    void configureTrack() throws NumberFormatException, SQLException, InterruptedException;

    void configureDriver() throws NumberFormatException, SQLException, InterruptedException;

}
