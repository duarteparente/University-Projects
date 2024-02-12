package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Business.RacingManager.Championship.Track;

public class TracksDAO {

    private static TracksDAO singleton = null;

    public static TracksDAO getInstance() {
        if (TracksDAO.singleton == null) {
            TracksDAO.singleton = new TracksDAO();
        }
        return TracksDAO.singleton;
    }

    public List<Track> getTracks() throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();
        List<Track> tracks = new ArrayList<>();
        String name = null;
        int distance;
        int nrLaps;

        ResultSet rs = st.executeQuery("SELECT * FROM tracks");
        while (rs.next()) {
            name = rs.getString("Name");
            distance = rs.getInt("Distance");
            nrLaps = rs.getInt("NrLaps");

            Track track = new Track(name,distance,nrLaps);
            tracks.add(track);
        }
        return tracks;
    }

    public void setTrack(String name,int distance,int nrlaps) throws SQLException {
        Connection con = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        Statement st = con.createStatement();

        st.executeUpdate("INSERT INTO tracks(Name,Distance,NrLaps)"+
                "VALUES('"+name+"','"+distance+"','"+nrlaps+"')"+
                "ON DUPLICATE KEY UPDATE Name=Name");
    }

}
