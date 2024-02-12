import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void shutdown(Socket socket) throws IOException{
        socket.shutdownInput();
        socket.shutdownOutput();
        socket.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException{

        Socket socket = new Socket("localhost", 1234);
        UI userUI = new UI();  
        TaggedConnection c = new TaggedConnection(socket);


        ClientInputHandler clih = new ClientInputHandler(c, userUI);
        Thread cin = new Thread(clih);
        cin.start();

        ServerInputHandler sih = new ServerInputHandler(c, clih, userUI);
        Thread sin = new Thread(sih);
        sin.start();

        cin.join();
        sin.join();

        shutdown(socket);

    }
    
}
