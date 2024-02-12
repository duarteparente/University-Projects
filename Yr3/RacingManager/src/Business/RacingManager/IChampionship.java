package Business.RacingManager;

import java.sql.SQLException;

public interface IChampionship {
     void playersChoice(int numberPlayers) throws SQLException;
     void configureTrack() throws NumberFormatException, SQLException, InterruptedException;
     void configureDriver() throws NumberFormatException, SQLException, InterruptedException;
}
