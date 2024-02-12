import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException{

        ServerSocket server = new ServerSocket(1234);
        DataHandler data = DataHandler.getInstance();
        Rewards rewards = new Rewards();
        new Thread(rewards).start();
        

        while(true){

            Socket client = server.accept();
            TaggedConnection c = new TaggedConnection(client);
            Worker clientHandler = new Worker(c, data);
            new Thread(clientHandler).start();

        }
    }
    
}
