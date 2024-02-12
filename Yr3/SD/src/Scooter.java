import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Scooter {

    private Boolean taken;
    private String reservation_code;
    private long time;
    private ReentrantLock lock;
    private Condition cond;


    public Scooter(){
        this.taken = false;
        this.reservation_code = null;
        lock = new ReentrantLock();
        cond = lock.newCondition();
    }

    public String reservation(String username) {
        lock.lock();
        try{
            this.taken = true;
            String tool = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
            StringBuilder sb = new StringBuilder();
            Random r = ThreadLocalRandom.current();
            for (int i = 0; i < 5; i++) {
                int pos = r.nextInt(tool.length());
                char c = tool.charAt(pos);
                sb.append(c);
            }
            this.reservation_code = sb.toString();
            this.time = System.currentTimeMillis();
            return this.reservation_code;
        }
        finally{
            lock.unlock();
        }
    }

    public long parking(String code, long time){
        lock.lock();
        try{
            this.taken = false;
            this.reservation_code = null;
            return time - this.time;
        }
        finally{
            lock.unlock();
        }
    }


    public boolean isTaken(){
        lock.lock();
        try{
            return this.taken;
        }
        finally{
            lock.unlock();
        }
    }

    public String getReservationCode(){
        lock.lock();
        try{
            return this.reservation_code;
        }
        finally{
            lock.unlock();
        }
    }

    public long getTime(){
        lock.lock();
        try{
            return this.time;
        }
        finally{
            lock.unlock();
        }
    }


    public void await() throws InterruptedException {
        lock.lock();
        cond.await();
        lock.unlock();
    }

    public void signallAll() {
        lock.lock();
        cond.signalAll();
        lock.unlock();
    }
    
}
