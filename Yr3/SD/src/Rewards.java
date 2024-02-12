import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Rewards implements Runnable{

    private ReentrantLock lock;
    private int D;

    public Rewards(){
        this.lock =  new ReentrantLock();
        this.D = 2;
    }

    public void setD(int D){
        lock.lock();
        this.D = D;
        lock.unlock();
    }


    public void run() {
        Map<String, List<String>> movements =  new HashMap<>();
        DataHandler data = DataHandler.getInstance();
        List<String> multiple = data.getPositionsMultipleScooters();
        List<String> noFree = data.noFreeScootersNear(D);
        for (String i : multiple){
            movements.put(i,noFree);
        }
        data.setRewards(movements);
        data.signallAll();
    }


}
