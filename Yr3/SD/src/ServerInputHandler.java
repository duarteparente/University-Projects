import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerInputHandler implements Runnable{

    private TaggedConnection c;
    UI ui;
    private String username;

    public ServerInputHandler(TaggedConnection c, ClientInputHandler clih, UI ui) {
        this.c = c;
        this.ui = ui;
    }


    public void run() {
        this.ui.menuLogin(null);
        try {
            boolean running = true;
            while(running){
                Frame received = this.c.receive();
                switch(received.tag){
                    case 0, 1 -> { 
                        this.username = new String(received.data.get(0));
                        if (received.tag == 0){
                            this.ui.accountCreated(this.username);
                        }
                        else this.ui.loginSuccess(this.username);
                    }
                    case 2 -> {
                        int size = Integer.parseInt(new String(received.data.get(0)));
                        List<String> l = new ArrayList<>();
                        for (int i=1; i<size; i++){
                            l.add(new String(received.data.get(i)));
                        }
                        this.ui.printPositions(l);
                        this.ui.waitInput();
                    }
                    case 3 -> {
                        String pos = new String(received.data.get(0));
                        
                        int size = Integer.parseInt(new String(received.data.get(1)));
                        System.out.println(size);
                        Map<String, Integer> rewards = new HashMap<>();
                        for (int i=2; i<size; i+=2){
                            String p = new String(received.data.get(i));
                            int r = Integer.parseInt(new String(received.data.get(i+1)));
                            rewards.put(p,r);
                        }
                        this.ui.printRewards(pos, rewards);
                    }
                    case 4 -> {
                        String pos = new String(received.data.get(0));
                        if (pos.equals("none")) this.ui.message(" Couldn't finish booking! No available scooters!");
                        else{
                            String code = new String(received.data.get(1));
                            this.ui.bookingSuccess(pos, code);
                        }
                    }
                    case 5 -> {
                        String initialpos = new String(received.data.get(0));
                        String finalpos = new String(received.data.get(1));
                        int reward = Integer.parseInt(new String(received.data.get(2)));
                        long duration = Long.parseLong(new String(received.data.get(3)));
                        int distance = Integer.parseInt(new String(received.data.get(4)));
                        double cost = Double.parseDouble(new String(received.data.get(5)));
                        int totalPoints = Integer.parseInt(new String(received.data.get(6))); 
                        this.ui.parkingSuccess(initialpos, finalpos, reward, duration, distance, cost, totalPoints);
                    }
                    case 6 -> {
                        if (received.data.isEmpty()){
                            this.ui.message(" Notifications turned on!");
                        }
                        else{
                            this.ui.message(" Attention! A new reward available matched your request!");
                        }
                    }
                    case 7 -> running = false;
                    case 8 -> this.ui.menuLogin("Error creating account! User already exists!");
                    case 9 -> this.ui.menuLogin("Couldn't log in! User doesn't exist!");
                    case 10 -> this.ui.menuLogin("Couldn't log in! Incorrect Password!");
                    case 11 -> this.ui.message(" Invalid Code! Couldn't park the scooter!");

                    
                    
                
                }


            }




        } catch (IOException e) {
            
            e.printStackTrace();
        }
        
        
    }

    
}
