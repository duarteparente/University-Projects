import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class UserInfo {

    private String username;
    private String password;
    private boolean notifications;
    private String location;
    private int points;
    private TaggedConnection c;  /* null se não tiver login efetuado */
    private ReentrantLock lock = new ReentrantLock();


    public UserInfo(String username,String password) {
        this.username = username;
        this.password = password;
        this.notifications = false;
        this.points = 0;
        this.location = Util.generateRandomPos(20); // a posição é random, tentativa de simular gps tracking
        this.c = null;
    }


    public int getPoints(){
        lock.lock();
        try {
            return this.points;
        }
        finally {
            lock.unlock();
        }
    }

    public void setPoints(int p){
        lock.lock();
        this.points += p;
        lock.unlock();
    }


    public String getUsername() {
        lock.lock();
        try {
            return this.username;
        }
        finally {
            lock.unlock();
        }
    }


    public String getPassword() {
        lock.lock();
        try {
            return this.password;
        }
        finally {
            lock.unlock();
        }
    }


    public boolean checkPassword(String pw) {
        lock.lock();
        try {
            return pw.equals(this.password);
        }
        finally {
            lock.unlock();
        }
    }

    public void setConnection(TaggedConnection con) {
        lock.lock();
        try {
            this.c = con;
        }
        finally {
            lock.unlock();
        }
    }

    public TaggedConnection getConnection() {
        lock.lock();
        try {
            return this.c;
        }
        finally {
            lock.unlock();
        }
    }

    public boolean notificationsON(){
        lock.lock();
        try {
            return this.notifications;
        }
        finally {
            lock.unlock();
        }
    }

    public void setNotifications(Boolean b) {
        lock.lock();
        try {
            this.notifications = b;
        }
        finally {
            lock.unlock();
        }
    }

    public String getLocation(){
        lock.lock();
        try {
            return this.location;
        }
        finally {
            lock.unlock();
        }
    }

    public void setLocation(String pos){
        lock.lock();
        try {
            this.location = pos;
        }
        finally {
            lock.unlock();
        }
    }


    public void lockUser() {
        lock.lock();
    }


    public void unlockUser() {
        lock.unlock();
    }


    @Override
    public int hashCode() {
        lock.lock();
        try {
            return Objects.hash(username, password);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        lock.lock();
        try {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserInfo user = (UserInfo) o;
            return this.username.equals(user.getUsername()) && this.password.equals(user.getPassword());
        }
        finally {
            lock.unlock();
        }
    }


    
}
