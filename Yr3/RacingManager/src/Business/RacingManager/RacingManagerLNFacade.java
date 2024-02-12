package Business.RacingManager;

import Business.IRacingManagerLN;

import Business.RacingManager.User.UserFacade;
import Business.RacingManager.Car.CarFacade;
import Business.RacingManager.Championship.ChampionshipFacade;

import java.sql.SQLException;


public class RacingManagerLNFacade implements IRacingManagerLN{

    ICar carFacade;
    IUser userFacade;
    IChampionship championshipFacade;

    public RacingManagerLNFacade() {
        carFacade = new CarFacade();
        userFacade = new UserFacade();
        championshipFacade = new ChampionshipFacade();
    }

    public boolean login(String user, String password, String type) throws SQLException {
        return this.userFacade.login(user,password,type);

    }

    public boolean create_user(String user, String password) throws SQLException {
        return this.userFacade.create_user(user, password);
    }

    public void playersChoice(int numberPlayers) throws SQLException{
        this.championshipFacade.playersChoice(numberPlayers);
    };

    public void configureTrack() throws NumberFormatException, SQLException, InterruptedException {
        this.championshipFacade.configureTrack();
    }
    public void configureDriver() throws NumberFormatException, SQLException, InterruptedException {
        this.championshipFacade.configureDriver();
    }
}