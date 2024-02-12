import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UI {

    private Scanner scanner;
    public UI() {
        this.scanner = new Scanner(System.in);
    }

    public void clean(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void emptySpace(int n){
        for(int i=0; i<n; i++)
            System.out.println();
    }

    public void init(){
        this.clean();
        System.out.println("--------------------------------- [trotUM] ---------------------------------");
    }

    public void printLine(String type){
        switch (type) {
            case "list" -> System.out.println("---------------- [Locations with Scooters] -----------------");
            default -> System.out.println();
        }

    }

    public void intro(int n){
        emptySpace(n);
        System.out.println("                              t   rrr  ooo   t                      ");
        System.out.println("                             ttt  r    o o  ttt                     ");
        System.out.println("                              t   r    o o   t                      ");
        System.out.println("                              t   r    o o   t                      ");
        System.out.println("                              tt  r    ooo   tt                     ");
        emptySpace(n+1);
        System.out.println("                             U     U    M     M                     ");
        System.out.println("                             U     U    MM   MM                     ");
        System.out.println("                             U     U    M M M M                     ");
        System.out.println("                             U     U    M  M  M                     ");
        System.out.println("                             U     U    M     M                     ");
        System.out.println("                             U     U    M     M                     ");
        System.out.println("                             U     U    M     M                     ");
        System.out.println("                             U     U    M     M                     ");
        System.out.println("                             U     U    M     M                     ");
        System.out.println("                             UUUUUUU    M     M                     ");
        emptySpace(n);
    }

    public void menuLogin(String message) {
        init();
        System.out.println("+--------------------------------------------------------------------------+");
        System.out.println("| Log In[L]                      Sign Up[S]                        Quit[Q] |");
        System.out.println("+--------------------------------------------------------------------------+");
        intro(3);
        if (message != null){
            System.out.println(message + "\n\n");
        }
        System.out.print("Choose your option: ");
    }

    public String getInput(){
        String type = this.scanner.nextLine();
        String result = "";
        switch (type) {
            case "" ->  result = "menu";
            case "L" -> result = "login";
            case "S" -> result = "signup";
            case "Q", "6" -> result = "quit";
            case "1" -> result = "list";
            case "2" -> result = "rewards";
            case "3" -> result = "book";
            case "4" -> result = "park";
            case "5" -> result = "notification";
            default ->  result = "else";
        }
        return result;

    }

    public String tryBack() {
        System.out.print("Try again [T] / Go Back [B]: ");
        String message = this.scanner.nextLine();

        if(!message.equals("T") && !message.equals("B"))
            tryBack();

        return message;
    }

    public String getField(String type){
        switch (type) {
            case "usernameL" -> System.out.print("Type your username: ");
            case "passwordL" -> System.out.print("Type your password: ");
            case "usernameR" -> System.out.print("Choose an username: ");
            default -> System.out.print("Choose a password: ");
        }

        return this.scanner.nextLine();
    }

    public String getDistance(){
        System.out.print("Choose the distance: ");
        return this.scanner.nextLine();
    }

    public String getReservationCode(){
        System.out.print("Enter the reservation code: ");
        return this.scanner.next();
    }

    public String getPosition(){
        System.out.println("Choose your position (x,y):");
        System.out.print(" X » ");
        String s;
        s = this.scanner.next();
        System.out.print(" Y » ");
        s += ",";
        s += this.scanner.next();
        return s;
    }

    public void menu() {
        this.clean();
        init();
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Locations With Available Scooters [1]");
        System.out.println("Rewards Available                 [2]");
        System.out.println("Book a Scooter                    [3]");
        System.out.println("Park a Scooter                    [4]");
        System.out.println("Turn on notifications             [5]");
        System.out.println("Quit                              [6]");
        intro(2);
        
        System.out.print("Choose your option: ");
    }


    public void printPositions(List<String> pos){
        this.clean();
        System.out.println("------------------------------------------------------------\n");
        System.out.println("«» Locations with available scooters:\n");
        for (int i=0; i<pos.size(); i++){
            System.out.println("    » (" + pos.get(i) + ")");
        }
    }

    public void waitInput() {
        System.out.print("\n\nPress Enter to continue.....");
    }

    public void accountCreated(String username) {
        System.out.println("\n Account successfully created! Welcome [" + username + "].\n");
        this.waitInput();
    }

    public void loginSuccess(String username){
        System.out.println("\n Welcome back [" + username + "] !\n");
        this.waitInput();
    }


    public void printRewards(String pos, Map<String, Integer> rewards){
        this.clean();
        System.out.println("------------------------------------------------------------\n");
        if (rewards.size() == 0){
            System.out.println(" No rewards availabe for position (" + pos + ")\n");
        }
        else{
            System.out.println("«» Rewards availabe for position (" + pos + "):");
            System.out.println(" Destination -> Reward");
            for (Map.Entry<String, Integer> e : rewards.entrySet()){
                System.out.println(" (" + e.getKey() + ") -> " + e.getValue());
            }
        }
        this.waitInput();
    }

    public void message(String message) {
        this.clean();
        System.out.println("------------------------------------------------------------\n");
        System.out.println(message);
        this.waitInput();
    }

    public void bookingSuccess(String pos, String code) {
        this.clean();
        System.out.println("-----------------------------------------------------------------------------------------\n");
        System.out.println(" Booking complete! You can find your scooter in position (" + pos + ")");
        System.out.println("\n Here is your reservation code: " + code);
        System.out.println("\n » Please note that you will have to present this code in order to park the scooter.");
        System.out.println("\n   (Don't forget it and don't share it with anyone)");
        this.waitInput();
    }

    public void parkingSuccess(String initialpos, String finalpos, int reward, long duration, int distance, double cost, int totalPoints){
        this.clean();
        System.out.println("---------------------------------------------------------------\n");
        System.out.println(" Scooter parked! Check your ride details:\n");
        System.out.println("    » From: (" + initialpos + ")");
        System.out.println("    » To: (" + finalpos + ")");
        Duration d = Duration.ofMillis(duration);
        long seconds = d.getSeconds();
        long MM = (seconds % 3600) / 60;
        long SS = seconds % 60;
        String time = String.format("%02d:%02d", MM, SS);
        System.out.println("    » Duration (minutes): " + time);
        System.out.println("    » Distance: " + distance);
        System.out.println("    » Cost: " + cost + "\u20ac");
        System.out.println("    » Reward: " + reward + " points\n");
        System.out.println("> You currently have " + totalPoints + " points.");
        this.waitInput();
    }
}