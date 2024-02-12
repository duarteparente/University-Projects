import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SPZoneTransfer implements Runnable{

    private Socket socket;

    public SPZoneTransfer(Socket socket){
        this.socket = socket;
    }

    public void run(){
        
        try {
            boolean zt = false;
            boolean authorized = false;
            DataInputStream dis = null;
            DataOutputStream dos = null;
            String address = (((InetSocketAddress) this.socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
            Integer byteCounter = 0;
            long start = 0;

            dis = new DataInputStream(this.socket.getInputStream());
            dos = new DataOutputStream(this.socket.getOutputStream());

            while(!zt){
            
                DataHandler data = DataHandler.getInstance();

                String message = (String) dis.readUTF();
                TimeStamp received = new TimeStamp();

                if (!authorized){
                    start = System.currentTimeMillis();

                    /* Receive SOASERIAL Query */
                    QueryHandler query = new QueryHandler(message);
                    LogHandler.getInstance().queryActivity("QR",received, address, query, data.getMode());
                    data.prepare_answer(query);
                    
                    dos.writeUTF(query.toString());
                    LogHandler.getInstance().queryActivity("RP",new TimeStamp(), address, query, data.getMode());
                    authorized = true;
                }
                else if (message.contains("abort")){
                    zt = true;
                }
                else if (message.contains("domain: ")){
                    String parts[] = message.split(" ");
                    // sp.validateSS((((InetSocketAddress) s.getRemoteSocketAddress()).getAddress()).toString().replace("/",""));
                    if (ConfigHandler.getInstance().getDomain().equals(parts[1])){
                        String ans = "entries: " + data.getNumberOfDBEntries();
                        dos.writeUTF(ans);
                    }
                }
                else if (message.contains("ok: ")){
                    String parts[] = message.split(" ");
                    if (Integer.parseInt(parts[1]) == data.getNumberOfDBEntries()){
                        String ans = ParserSPDatabase.getDBFileZT();
                        dos.writeUTF(ans);
                        long end = System.currentTimeMillis()-start;
                        byteCounter += ans.getBytes().length;
                        LogHandler.getInstance().zoneTransfer(new TimeStamp(), address, end, byteCounter, ConfigHandler.getInstance().checkServerType(), data.getMode());
                        zt = true;
                    }
                }
        
            }
            dis.close();
            dos.close();
            this.socket.close();
            
            
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}
