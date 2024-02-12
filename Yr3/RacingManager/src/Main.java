import java.sql.SQLException;


import Database.DB;
import View.UI;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException {

        DB.initDB();
        UI ui = new UI();
        ui.start();
        
        
    }
}
