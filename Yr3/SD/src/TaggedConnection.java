import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class TaggedConnection implements AutoCloseable{

    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    ReentrantLock read = new ReentrantLock();
    ReentrantLock write = new ReentrantLock();

    /* A mensagem passada no socket tem o formato "tag(int) + mensagem a passar(bytes)" */

    public TaggedConnection(Socket socket) throws IOException {
        this.socket = socket;
        dis = new DataInputStream((this.socket.getInputStream()));
        dos = new DataOutputStream(this.socket.getOutputStream());
    }

    public void send(Frame frame) throws IOException {
        this.send(frame.tag, frame.data);
    }

    public void send(int tag, List<byte[]> data) throws IOException {
        write.lock();
        dos.writeInt(tag);
        int size = 0;
        if (data != null) size = data.size();
        dos.writeInt(size);
        for(int i=0; i<size; i++){
            dos.writeInt(data.get(i).length);
            dos.write(data.get(i));
            dos.flush();
        }
        write.unlock();
    }


    public Frame receive() throws IOException {
        read.lock();
        try {
            int tag = dis.readInt();
            int size = dis.readInt();
            List<byte[]> data = new ArrayList<>();
            for(int i=0; i<size; i++){
                int n = dis.readInt();
                byte[] d = new byte[n];
                dis.readFully(d);
                data.add(d);
            }
            return new Frame(tag,data);
        }
        finally {
            read.unlock();
        }
    }


    public void close() throws IOException {
        this.dis.close();
        this.dos.close();
    }

    
}
