package View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Business.IRacingManagerLN;
import Business.RacingManager.Car.Car;
import Business.RacingManager.Championship.Driver;
import Business.RacingManager.RacingManagerLNFacade;

public class UI {
    private static final Scanner scanner = new Scanner(System.in);
    private IRacingManagerLN model;

    public UI(){
        this.model = new RacingManagerLNFacade();
    }

    public String loginOptionHandler(int option) throws SQLException {
        boolean res = false;
        switch(option){
            case 1: res = this.handleCreate(); break;
            case 2: res = this.handleLogin("admin"); break;
            case 3: res = this.handleLogin("player"); break;
            default: System.exit(0);
        }
        if (res){
            if (option == 2) return "admin";
            return "player";
        }
        return "false";
    }

    private boolean handleCreate() throws SQLException {
        clearScreen();
        System.out.print("Type your username -> ");
        String username = this.scanner.next();
        System.out.print("Type your password -> ");
        String password = this.scanner.next();
        return this.model.create_user(username, password);

    }

    public boolean playerOptionHandler(int option) throws SQLException {
        boolean res = false;
        switch(option){
            case 1: playChoice(); break;
            default: System.exit(0);
        }
        return res;
    }

    public boolean adminOptionHandler(int option) throws SQLException, InterruptedException {
        boolean res = false;
        switch(option){
            case 1: this.model.configureTrack(); break;
            case 2: this.model.configureDriver(); break;
            default: System.exit(0);
        }
        return res;
    }

    public void playChoice() throws SQLException {
        System.out.print("Select number of players -> ");
        int option = scanner.nextInt();
        this.model.playersChoice(option);
    }


    private boolean handleLogin(String type) throws SQLException {
        clearScreen();
        System.out.print("Type your username -> ");
        String username = scanner.next();
        System.out.print("Type your password -> ");
        String password = scanner.next();
        return this.model.login(username, password, type);
    }

    public void start() throws SQLException, InterruptedException {
        String login = "false";
        while(!(login.equals("player") || login.equals("admin"))){
            clearScreen();
            int res = loginMenu();
            login = loginOptionHandler(res);
        }
        boolean running = false;
        if (login.equals("player")){
            while(!running){
                clearScreen();
                int res = playerMenu();
                running = playerOptionHandler(res);
            }

        }
        else{
            while(!running){
                clearScreen();
                int res = adminMenu();
                running = adminOptionHandler(res);
            }
        }

    }


    public int loginMenu(){
        clearScreen();
        StringBuilder s = new StringBuilder("                              [Racing Manager]        \n\n");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 1  |  Create account                                                  |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 2  |  Login as admnistrator                                           |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 3  |  Login as player                                                 |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 4  |  Quit                                                            |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n\n   »» Insert option: ");
        System.out.print(s);
        return scanner.nextInt();
    }

    public int adminMenu() {
        clearScreen();
        StringBuilder s = new StringBuilder("                              [Racing Manager]        \n\n");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 1  |  Configure Track                                                 |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 2  |  Configure Driver                                                |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | Other Number  |  Quit                                                 |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n\n   »» Insert option: ");
        System.out.print(s);
        return scanner.nextInt();
    }


    public int playerMenu() {
        clearScreen();
        StringBuilder s = new StringBuilder("                              [Racing Manager]        \n\n");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 1  |  Play                                                            |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | Other Number  |  Quit                                                 |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n\n   »» Insert option: ");
        System.out.print(s);
        return scanner.nextInt();
    }

    public static void printDrivers(List<Driver> d) {
        StringBuilder s = new StringBuilder("                              [Drivers]        \n\n");
        for(int i=0; i<d.size(); i++){
            s.append(i+1).append(" -> ").append(d.get(i).printDriver()).append("\n");
        }
        System.out.println(s);
    }

    public static int chooseDriver() {
        System.out.print("Choose your driver -> ");
        return scanner.nextInt();
    }


    public static void printCars(List<Car> c) {
        System.out.println("----------------------------------------------------------");
        StringBuilder s = new StringBuilder("                              [Cars]        \n\n");
        for(int i=0; i<c.size(); i++){
            s.append(i+1).append(" -> ").append(c.get(i).printCar()).append("\n");
        }
        System.out.println(s);
    }

    public static String chooseUsername(int player){
        printPlayer(player);
        System.out.print("Choose your username: ");
        return scanner.next();

    }

    public static void printPlayer(int player){
        System.out.println("\n                              [Player " + player + "]        \n\n");
    }

    public static int chooseCar() {
        System.out.print("Choose your car -> ");
        return scanner.nextInt();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void waitInput(){
        System.out.println("\n\n Press Enter...");
        String s = scanner.next();
    }

    public static List<String> createTrack() throws InterruptedException {
        List<String> ans = new ArrayList<>();
        clearScreen();
        System.out.print("Type the track's name -> ");
        ans.add(scanner.next());
        System.out.print("Type the track's distance(kms) -> ");
        ans.add(scanner.next());
        System.out.print("Type the track's number of laps -> ");
        ans.add(scanner.next());

        System.out.println("Track created!");
        Thread.sleep(500);

        return ans;

    }

    public static List<String> createDriver() throws InterruptedException {
        List<String> ans = new ArrayList<>();
        clearScreen();
        System.out.println("\n" + "CTS -> indicates whether the driver tends to be better in wet weather (<0.5) or dry weather (>0.5)");
        System.out.println("SVA -> indicates wether the pilot tends to prioritize safety(<0.5) or be more aggressive(>0.5)" + "\n");
        System.out.print("Type the driver's name -> ");
        ans.add(scanner.next());
        System.out.print("Type the driver's country -> ");
        ans.add(scanner.next());
        System.out.print("Type the driver's CTS (value between 0 and 1) -> ");
        ans.add(scanner.next());
        System.out.print("Type the driver's SVA (value between 0 and 1) -> ");
        ans.add(scanner.next());

        System.out.println("Driver created!");
        Thread.sleep(500);

        return ans;

    }

}
