import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientInputHandler implements Runnable{

    private final TaggedConnection c;
    UI ui;

    public ClientInputHandler(TaggedConnection c, UI ui) {
        this.c = c;
        this.ui = ui;
    }


    public void quit() throws IOException {
        Frame frame = new Frame(7,null);
        this.c.send(frame);
    }

    

    public void loginRegist(int tag) throws IOException{
        String usrtype = "usernameR";
        String pswtype = "passwordR";
        if (tag == 1) {
            usrtype = "usernameL";
            pswtype = "passwordL";
        }
        String username = this.ui.getField(usrtype);
        String password = this.ui.getField(pswtype);

        List<byte[]> data = new ArrayList<>();
        data.add(username.getBytes());
        data.add(password.getBytes());

        Frame frame = new Frame(tag, data);
        this.c.send(frame);

    }


    public void listOrBookScooters(int tag) throws IOException{
        String D = this.ui.getDistance();
        String pos = this.ui.getPosition();
        List<byte[]> data = new ArrayList<>();
        data.add(D.getBytes());
        data.add(pos.getBytes());
        Frame frame = new Frame(tag, data);
        this.c.send(frame);
    }


    public void listRewards() throws IOException{
        String pos = this.ui.getPosition();
        List<byte[]> data = new ArrayList<>();
        data.add(pos.getBytes());
        Frame frame = new Frame(3, data);
        this.c.send(frame);
    }

    public void parkScooter() throws IOException{
        String pos = this.ui.getPosition();
        String code = this.ui.getReservationCode();
        List<byte[]> data = new ArrayList<>();
        data.add(pos.getBytes());
        data.add(code.getBytes());
        Frame frame = new Frame(5, data);
        this.c.send(frame);
    }

    public void notification() throws IOException{
        String pos = this.ui.getPosition();
        List<byte[]> data = new ArrayList<>();
        data.add(pos.getBytes());
        Frame frame = new Frame(6, data);
        this.c.send(frame);   
    }
   

    public void run(){
        boolean running = true;
        try{
            while (running){

                String answer = this.ui.getInput();
                switch(answer){
                    case "menu" -> this.ui.menu();
                    case "login" -> loginRegist(1);
                    case "signup" -> loginRegist(0);
                    case "quit" -> { quit(); running = false; }
                    case "list" -> listOrBookScooters(2);
                    case "rewards" -> listRewards();
                    case "book" ->  listOrBookScooters(4);
                    case "park"-> parkScooter(); 
                    case "notification" -> notification();
                    default -> System.out.println("Invalid Option!");
                }
            } 

        }
        catch(IOException e){
            e.printStackTrace();
        }
            
    }
    

        
        
}

    
    

