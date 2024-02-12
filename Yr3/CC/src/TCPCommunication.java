import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPCommunication implements Runnable{

    public void run(){

        try {

            if (ConfigHandler.getInstance().checkServerType() == 0){ /* SP */

                while (true){

                    ServerSocket clientSocket = new ServerSocket(5959);
                    Socket s = clientSocket.accept();
                    SPZoneTransfer serverZT = new SPZoneTransfer(s);
                    new Thread(serverZT).start();

                    clientSocket.close();
                }
                

            }
            else{ /* SS */
                boolean zt = false;
                boolean authorized = false;
                boolean asking = false;
                boolean receiving = false;
                Integer nrEntries = 0;
                Integer dbVersion = 0;
                Integer byteCounter = 0;
                long start = 0;

                DataInputStream dis = null;
                DataOutputStream dos = null;
                
                String spIP = ConfigHandler.getInstance().getPrimaryServerIP();
                Socket sendersocket = new Socket(spIP,5959);

                DataHandler data = DataHandler.getInstance();
                ConfigHandler config = ConfigHandler.getInstance();

                dis = new DataInputStream(sendersocket.getInputStream());
                dos = new DataOutputStream(sendersocket.getOutputStream());


                while(!zt){

                    if (!authorized && !asking && !receiving){
                        start = System.currentTimeMillis();
                        QueryHandler query = new QueryHandler(spIP, config.getDomain(), "SOASERIAL", 'Q');
                        dos.writeUTF(query.toString());
                        LogHandler.getInstance().queryActivity("QE", new TimeStamp(), spIP, query, data.getMode());

                        String message = (String) dis.readUTF();
                        query = new QueryHandler(message);
                        LogHandler.getInstance().queryActivity("RR", new TimeStamp(), spIP, query, data.getMode());

                        /* Check DB version */
                        dbVersion = query.getdbVersion();
                        if (data.getDBVersion() < dbVersion){
                            asking = true;
                        }
                        else {
                            dos.writeUTF("abort");
                            zt = true;
                        }
                    }
                    else if (!authorized && asking){
                        dos.writeUTF("domain: " + config.getDomain());
                        String received = (String) dis.readUTF();
                        if(received.contains("entries:")){
                            asking = false;
                            authorized = true;
                            String parts[] = received.split(" ");
                            nrEntries = Integer.parseInt(parts[1]);
                            if (nrEntries > 65535){
                                LogHandler.getInstance().errorZoneTransfer(new TimeStamp(), spIP, config.checkServerType(), data.getMode());
                                zt = true;
                                dos.writeUTF("abort");
                            }

                        }
                        else{
                            LogHandler.getInstance().errorZoneTransfer(new TimeStamp(), spIP, config.checkServerType(), data.getMode());
                            zt = true;
                            dos.writeUTF("abort");
                        }
                    }
                    else if (authorized){
                        dos.writeUTF("ok: " + nrEntries);
                        String received = (String) dis.readUTF();
                        long end = System.currentTimeMillis()-start;
                        TimeStamp treceived = new TimeStamp();
                        byteCounter += received.getBytes().length;
                        zt = true;
                        LogHandler.getInstance().zoneTransfer(treceived, spIP, end, byteCounter, config.checkServerType(), data.getMode());
                        data.setDBVersion(dbVersion);
                        data.storeDB(received);
                    }
                   
                }

                dis.close();
                dos.close();
                sendersocket.close();
                
                
                

            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    
}
