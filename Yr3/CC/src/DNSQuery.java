import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
* Class that helps handling multi-threaded udp communication between client and server
* @author Duarte Parente
* @since 24/12/2022
* Last change: 26/12/2022
*/
public class DNSQuery implements Runnable {

   private final DatagramPacket packet;
   private final TimeStamp queryReceived;
   private QueryHandler pduReceived;
   private final String clientAdress;
   private final int clientPort;

    public DNSQuery(DatagramPacket packet, TimeStamp queryReceived, String address, int port){
        this.packet = packet;
        this.queryReceived = queryReceived;
        this.clientAdress = address;
        this.clientPort = port;
    }

   
    public void run() {

        try {
            //receives the packet
    
            QueryHandler pduReceived = new QueryHandler(this.packet.getData());
            this.pduReceived = pduReceived;
            LogHandler.getInstance().queryActivity("QR", this.queryReceived, this.clientAdress, pduReceived, DataHandler.getInstance().getMode());

            DatagramSocket QuerySocket = new DatagramSocket();

            if (ConfigHandler.getInstance().checkServerType() != 2){
                DataHandler.getInstance().prepare_answer(pduReceived);
                DatagramPacket sendResponse = new DatagramPacket (pduReceived.queryToByteArray(), pduReceived.queryToByteArray().length, InetAddress.getByName(this.clientAdress), this.clientPort);
                QuerySocket.send(sendResponse);
                LogHandler.getInstance().queryActivity("RP", new TimeStamp(),this.clientAdress, pduReceived, DataHandler.getInstance().getMode());
            }
            else{

                List<String> ips = DataHandler.getInstance().getSTIPs();
                List<String> visited = new ArrayList<>(); /* Contacted servers */

                /* Verificar se resposta está em cache */
                

                /* Mandar para entradas DD (se tiver) ou ST */
                if(ConfigHandler.getInstance().getDomain().equals(this.pduReceived.getQueryInfoName()) && ConfigHandler.getInstance().hasDD()){
                    ips = ConfigHandler.getInstance().getDDIPs();
                }
                boolean found = false;
                while(!found && !ips.isEmpty()){
                    int port = 5353;
                    String sendIP = ips.remove(0);
                    if (visited.contains(sendIP)) continue;
                    visited.add(sendIP);
                    if (sendIP.contains(":")){
                        String[] parts = sendIP.split(":");
                        port = Integer.parseInt(parts[1]);
                        sendIP = parts[0];
                    }
                    DatagramPacket pdu = new DatagramPacket(pduReceived.queryToByteArray(), pduReceived.queryToByteArray().length, InetAddress.getByName(sendIP), port);
                    QuerySocket.send(pdu);
                    LogHandler.getInstance().queryActivity("QE", new TimeStamp(),sendIP, pduReceived, DataHandler.getInstance().getMode());

                    byte[] buffer = new byte[512];
                    DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                    QuerySocket.receive(response);
                    TimeStamp r = new TimeStamp();
                    QueryHandler received = new QueryHandler(response.getData());
                    LogHandler.getInstance().queryActivity("RR", r,response.getAddress().getHostAddress(), received, DataHandler.getInstance().getMode());

                    /* Resposta encontrada */
                    if (received.getResponseCode() == 0){
                        DatagramPacket ansFound = new DatagramPacket(received.queryToByteArray(), received.queryToByteArray().length, InetAddress.getByName(this.clientAdress), this.clientPort); 
                        QuerySocket.send(ansFound);
                        LogHandler.getInstance().queryActivity("RP", new TimeStamp(),this.clientAdress, received, DataHandler.getInstance().getMode());
                        found = true;
                    }

                    else if (received.getResponseCode() == 1){
                        ips = received.getExtraIPs();
                    }
                }

                if (!found){
                    this.pduReceived.setResponseCode(2);
                    DatagramPacket sendResponse = new DatagramPacket (pduReceived.queryToByteArray(), pduReceived.queryToByteArray().length, InetAddress.getByName(this.clientAdress), this.clientPort);
                    QuerySocket.send(sendResponse);
                    LogHandler.getInstance().queryActivity("RP", new TimeStamp(),this.clientAdress, pduReceived, DataHandler.getInstance().getMode());
                }
            }
                
            QuerySocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
}
 