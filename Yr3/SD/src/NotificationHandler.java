import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotificationHandler implements Runnable{

    private String position;
    private UserInfo user;


    public NotificationHandler(String pos, String username){
        this.position = pos;
        this.user = DataHandler.getInstance().getUser(username);
    }

    @Override
    public void run() {
        try{
            DataHandler data = DataHandler.getInstance();
            while(data.checkReward(this.position, position) != 1){
                data.await();

            }
            //this.user.lockUser();
            TaggedConnection c = user.getConnection();
            List<byte[]> result = new ArrayList<>();
            result.add("reward".getBytes());
            Frame frame = new Frame(6, result);
            c.send(frame);
        }
        catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
           // this.user.unlockUser();
        }


        
        
    }
    
}
