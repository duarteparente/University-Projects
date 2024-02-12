import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataHandler {

    private static DataHandler dataInstance;

    private final int gridDimension;
    private Map<String, List<String>> rewards;
    private Map<String, List<Scooter>> scooters;
    private Map<String,UserInfo> users; 
    private ReentrantReadWriteLock lock  = new ReentrantReadWriteLock();
    private Condition cond = lock.writeLock().newCondition();


    private DataHandler(){
        this.scooters = new HashMap<>();
        this.gridDimension = 20;
        initScooters(this.gridDimension);
        users = new HashMap<>();
    }

    public static DataHandler getInstance(){
        if (dataInstance == null){
            dataInstance = new DataHandler();
        }
        return dataInstance; 
    }


    public void parkScooter(String pos, String code, String newPos){
        lock.writeLock().lock();
        try{
            for(Scooter s : this.scooters.get(pos)){
                if (s.isTaken() && s.getReservationCode().equals(code)){
                    
                    this.scooters.get(pos).remove(s);
                    if (this.scooters.get(pos).size() == 0) this.scooters.remove(pos);
                    break;
                }
            }
            if (!this.scooters.containsKey(newPos)) this.scooters.put(newPos,new ArrayList<>());
            this.scooters.get(newPos).add(new Scooter());
        }
        finally{
            lock.writeLock().unlock();
        }
    }


    public long getUsageDuration(String pos, String code, long time){
        lock.readLock().lock();
        try{
            for(Scooter s : this.scooters.get(pos)){
                if (s.isTaken() && s.getReservationCode().equals(code)){
                    return s.parking(code, time);
                }
            }
            return 0;
        }
        finally{
            lock.readLock().unlock();
        }
    }

    public int getUserPoints(String username){
        lock.readLock().lock();
        try{
            return this.users.get(username).getPoints();
        }
        finally{
            lock.readLock().unlock();
        }
    }

    public void updatePoints(String username, int points){
        lock.writeLock().lock();
        this.users.get(username).setPoints(points);
        lock.writeLock().unlock();
    }


    public int checkReward(String initialpos, String finalpos){
        lock.readLock().lock();
        try{
            int r = 0;
            if (this.rewards.containsKey(initialpos)){
                r = 1;
                if (this.rewards.get(initialpos).contains(finalpos)){
                    int counter = 0;
                    for(Scooter s : this.scooters.get(initialpos)){
                        if(!s.isTaken()) counter++; 
                    }
                    r = Util.distance(initialpos,finalpos) * counter;
                }
            }
            return r;
        }
        finally{
            lock.readLock().unlock();
        }

    }


    public String findScooter(String code){
        lock.readLock().lock();
        try{
            String s = "none";
            for (Map.Entry<String, List<Scooter>> e : this.scooters.entrySet()){
                for (Scooter sc : e.getValue()){
                    if (sc.isTaken() && sc.getReservationCode().equals(code)){
                        s = e.getKey();
                        break;
                    } 
                }
                if (!s.equals("none")) break;
            }

            return s;
        }
        finally{
            lock.readLock().unlock();
        }
    }


    public List<String> scooterBooking(List<String> scooters, String pos, String username){
        lock.readLock().lock();
        String min = scooters.get(0);
        int minD = Util.distance(min, pos);
        for (int i=1; i<scooters.size(); i++){
            int d = Util.distance(scooters.get(i), pos);
            if (d < minD){
                minD = d;
                min = scooters.get(i);
            }
        }
        lock.readLock().unlock();
        lock.writeLock().lock();
        try{
            List<String> res = new ArrayList<>();
            res.add(min);
            for (Scooter s : this.scooters.get(min)){
                if (!s.isTaken()){
                    res.add(s.reservation(username));
                    break;
                }
            }

            return res;
        }
        finally{
            lock.writeLock().unlock();
        }
    }


    public Map<String, Integer> positionRewardList(String pos){
        lock.readLock().lock();
        try {
            if (this.rewards.containsKey(pos)){
                Map<String, Integer> res = new HashMap<>();
                for (String i : this.rewards.get(pos)){
                    int counter = 0;
                    for(Scooter s : this.scooters.get(pos)){
                        if(!s.isTaken()) counter++; 
                    }
                    if (!i.equals(pos)){
                        int reward = Util.distance(pos,i) * counter;
                        res.put(i,reward);
                    }
                }
                return res;
            } 
            return new HashMap<>();

        }
        finally{
            lock.readLock().unlock();
        }
        
    }

    public void setRewards(Map<String, List<String>> rewards){
        lock.writeLock().lock();
        this.rewards = rewards;
        lock.writeLock().unlock();
    }


    public List<String> noFreeScootersNear(int D){
        lock.readLock().lock();
        try{
            boolean taken;
            List<String> res = new ArrayList<>();
            for (int x = 1; x<=20; x++){
                for(int y = 1; y<=20; y++){
                    String pos = Integer.toString(x) + "," + Integer.toString(y);
                    taken = true;
                    List<String> l = Util.getNearPositions(D, pos);
                    for (String j : l){
                        if (this.scooters.containsKey(j)) {
                            for (Scooter sc : this.scooters.get(j)){
                                if (!sc.isTaken()){
                                    taken = false; 
                                    break;
                                }
                            }
                        }
                    }
                    if(taken) res.add(pos);

                }

            }
            return res;
        }
        finally{
            lock.readLock().unlock();
        }
       
    }



    public List<String> getPositionsMultipleScooters(){
        lock.readLock().lock();
        try{
            List<String> res = new ArrayList<>();
            for (Map.Entry<String, List<Scooter>> e : this.scooters.entrySet()){
                int free_counter = 0;
                for (Scooter s : e.getValue()){
                    if (!s.isTaken()) free_counter++;
                }
                if (free_counter >= 2) res.add(e.getKey());
            }
            return res;
        }
        finally{
            lock.readLock().unlock();
        }

    }


    public void initScooters(int N){
        int numberScooters = (int) Math.floor(Math.random() *(120 - 90 + 1) + 90);
        for (int i = 0; i<numberScooters; i++){
            String s = Util.generateRandomPos(N);
            if (!this.scooters.containsKey(s)) this.scooters.put(s,new ArrayList<>());
            this.scooters.get(s).add(new Scooter());
        }
    }



    public List<String> scootersInDistance(int D, String pos){
        lock.readLock().lock();
        try{
            List<String> res = new ArrayList<>();
            for(Map.Entry<String, List<Scooter>> e : this.scooters.entrySet()){
                for (Scooter s : e.getValue()){
                    if (!s.isTaken()){
                        if (Util.isInDistance(D, e.getKey(), pos)){
                            res.add(e.getKey());
                            break;
                        }

                    }
                }
            }
            return res;
        }
        finally{
            lock.readLock().unlock();
        }
    }


    public boolean userExists(String username){
        lock.readLock().lock();
        try{
            return this.users.containsKey(username);
        }
        finally{
            lock.readLock().unlock();
        }
    }


    public void addUser(String username, String password){
        lock.writeLock().lock();
        UserInfo user = new UserInfo(username, password);
        this.users.put(username, user);
        lock.writeLock().unlock();
    }

    public boolean validatePassword(String username, String Password){
        lock.readLock().lock();
        try{
            return this.users.get(username).checkPassword(Password);
        }
        finally{
            lock.readLock().unlock();
        }
    }


    public int getGridDimension(){
        lock.readLock().lock();
        try{
            return this.gridDimension;
        }
        finally{
            lock.readLock().unlock();
        }
    }



    public void addTaggedConnecion(String user, TaggedConnection taggedConnection) {
        lock.writeLock().lock();
        users.get(user).setConnection(taggedConnection);
        lock.writeLock().unlock();
    }

    public void removeTaggedConnection(String user) {
        lock.writeLock().lock();
        users.get(user).setConnection(null);
        lock.writeLock().unlock();
    }

    public UserInfo getUser(String userID) {
        lock.readLock().lock();
        try {
            return users.get(userID);
        }
        finally {
            lock.readLock().unlock();
        }
    }


    public void await() throws InterruptedException {
        try {
            lock.writeLock().lock();
            cond.await();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void signallAll() {
        try {
            lock.writeLock().lock();
            cond.signalAll();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
}
