import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Worker implements Runnable{

    private final TaggedConnection c;
    private DataHandler data;
    private String username; // username do cliente

    public Worker (TaggedConnection c, DataHandler data) {
        this.c = c;
        this.data = data;
        this.username = null;
    }


    public void quit() throws IOException {
        this.data.removeTaggedConnection(this.username);
        Frame frame = new Frame(7,null);
        this.c.send(frame);
        this.c.close();
    }


    public void signUp(List<byte[]> data) throws IOException{

        String username = new String(data.get(0));
        String password = new String(data.get(1));

        if (this.data.userExists(username)){ /* Verificar se user já existe */
            Frame send = new Frame(8, null);
            this.c.send(send);
        }
        else{
            this.username = username;
            List<byte[]> result = new ArrayList<>();
            result.add(username.getBytes());
            Frame send = new Frame(0, result);
            this.c.send(send);
            this.data.addUser(username, password);
            this.data.addTaggedConnecion(username, this.c);
        }

    }


    public void login(List<byte[]> data) throws IOException{

        String username = new String(data.get(0));
        String password = new String(data.get(1));

        if (!this.data.userExists(username)){ /* Verificar se user não existe */
            Frame send = new Frame(9, null);
            this.c.send(send);
        }
        else if (!this.data.validatePassword(username, password)){
            Frame send = new Frame(10, null);
            this.c.send(send);
        }
        else{
            this.username = username;
            List<byte[]> result = new ArrayList<>();
            result.add(username.getBytes());
            Frame send = new Frame(1, result);
            this.c.send(send);
            this.data.addTaggedConnecion(username, this.c);
            
        }
    }


    public void listScooters(List<byte[]> data) throws IOException{
        int D = Integer.parseInt(new String(data.get(0)));
        String pos = new String(data.get(1));
        List<String> res = this.data.scootersInDistance(D, pos);
        List<byte[]> result = new ArrayList<>();
        result.add(String.valueOf(res.size()).getBytes()); 
        for (String i : res){
            result.add(i.getBytes());
        }
        Frame send = new Frame(2, result);
        this.c.send(send);
    }


    public void listRewards(List<byte[]> data) throws IOException{
        String pos = new String(data.get(0));
        Map<String, Integer> res = this.data.positionRewardList(pos);
        List<byte[]> result = new ArrayList<>();
        result.add(pos.getBytes());
        result.add(String.valueOf(res.size()).getBytes());
        for (Map.Entry<String, Integer> e : res.entrySet()){
            result.add(e.getKey().getBytes());
            result.add(String.valueOf(e.getValue()).getBytes());
        }
        Frame send = new Frame(3, result);
        this.c.send(send);
    }


    public void bookScooter(List<byte[]> data) throws IOException{
        int D = Integer.parseInt(new String(data.get(0)));
        String pos = new String(data.get(1));
        List<String> scooters = this.data.scootersInDistance(D, pos);
        List<byte[]> result = new ArrayList<>();
        if (scooters.size() == 0){
            result.add("none".getBytes());
        }
        else{
            List<String> res = this.data.scooterBooking(scooters, pos, this.username);
            result.add(res.get(0).getBytes());
            result.add(res.get(1).getBytes());
        }
        Frame send = new Frame(4, result);
        this.c.send(send);
        Rewards rewards = new Rewards();
        new Thread(rewards).start();
    }


    public void parkScooter(List<byte[]> data) throws IOException{
        String finalpos = new String(data.get(0));
        String code = new String(data.get(1));
        long time = System.currentTimeMillis(); // registar o momento de estacionamento
        String initialpos = this.data.findScooter(code);
        if (initialpos.equals("none")){
            Frame send = new Frame(11, null); // código de reserva errado
            this.c.send(send);
        }
        else{
            long duration = this.data.getUsageDuration(initialpos, code, time);
            List<byte[]> result = new ArrayList<>();
            result.add(initialpos.getBytes());
            result.add(finalpos.getBytes());
            int reward = this.data.checkReward(initialpos, finalpos);
            if (reward == 1){
                reward = 0;
            }
            result.add(String.valueOf(reward).getBytes());
            result.add(String.valueOf(duration).getBytes());
            int distance = Util.distance(finalpos, initialpos);
            double cost = (0.000015*duration) + (0.15*distance);
            result.add(String.valueOf(distance).getBytes());
            result.add(String.valueOf(cost).getBytes());
            this.data.updatePoints(this.username, reward);
            int totalPoints = this.data.getUserPoints(this.username);
            result.add(String.valueOf(totalPoints).getBytes());
            this.data.parkScooter(initialpos, code, finalpos);
            Frame send = new Frame(5, result);
            this.c.send(send);
            Rewards rewards = new Rewards();
            new Thread(rewards).start();
        }

    }


    public void notifications(List<byte[]> data) throws IOException{
        String pos = new String(data.get(0));
        NotificationHandler not = new NotificationHandler(pos, this.username);
        new Thread(not).start();
        Frame send = new Frame(6, null);
        this.c.send(send);
    }

    
    public void run() {
        boolean running = true;
        try {

            while(running){
                Frame received = this.c.receive();
                switch(received.tag){
                    case 0 -> signUp(received.data);
                    case 1 -> login(received.data);
                    case 2 -> listScooters(received.data);
                    case 3 -> listRewards(received.data);
                    case 4 -> bookScooter(received.data);
                    case 5 -> parkScooter(received.data);
                    case 6 -> notifications(received.data);
                    case 7 -> { running = false; quit();}
                    
                
                }


            }



        }
        catch (IOException e){
            e.printStackTrace();
        }
       
        
    }
    
}
