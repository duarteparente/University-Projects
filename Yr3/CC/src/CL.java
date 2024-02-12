import java.io.IOException;
import java.net.*;

/**
* Class that represents the DNS client application.
* @author Duarte Parente, Ana Rita
* @since 07/11/2022
* Last change: 23/11/2022
*/
public class CL {

    
    /** 
     * Method that represents the implementation of a DNS query.
     *      { arg1 = ip[:porta] arg2 = QUERY-INFO.NAME arg3 = QUERY-INFO.TYPE }
     */
    public static void main(String[] args) throws IOException{
        if (Validation.checkClientArguments(args)){
            Character flag = 'Q';
            String serverIP = args[0];
            Integer port = 5353;
            if (args[0].contains(":")){
                String[] parts = args[0].split(":");
                port = Integer.parseInt(parts[1]);
                serverIP = parts[0];
            }


            QueryHandler pduSent = new QueryHandler(serverIP,args[1],args[2],flag);
            byte[] senderBuffer = pduSent.queryToByteArray();
            byte[] receiverBuffer = new byte[512];

            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(serverIP);
            DatagramPacket sentDatagram = new DatagramPacket(senderBuffer, senderBuffer.length, address, port);
            clientSocket.send(sentDatagram);

            DatagramPacket receivedDatagram = new DatagramPacket(receiverBuffer, receiverBuffer.length);
            clientSocket.receive(receivedDatagram);
            QueryHandler pduReceived = new QueryHandler(receivedDatagram.getData());
            pduReceived.showOutput();

            clientSocket.close();
        } 
        else System.out.println("\n » Could not execute - Wrong arguments\n");
    }
}
