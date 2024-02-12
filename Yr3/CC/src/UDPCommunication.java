import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
* Class that handles multi-threaded udp communication between client and server
* @author Ana Rita,Duarte Parente
* @since 24/12/2022
* Last change: 26/12/2022
*/
public class UDPCommunication{

    public UDPCommunication() throws IOException{

        DatagramSocket serverSocket = new DatagramSocket(DataHandler.getInstance().getPort()); 

        while(true){
            byte[] buffer = new byte[256];
        
            //prepares the packet 
            DatagramPacket serverDatagram = new DatagramPacket(buffer, buffer.length); // is now ready to receive data
            serverSocket.receive(serverDatagram); 
            TimeStamp queryReceived = new TimeStamp();
            InetAddress clientAddress = serverDatagram.getAddress();
            int port = serverDatagram.getPort();

            DNSQuery dnsquery = new DNSQuery(serverDatagram, queryReceived, clientAddress.getHostAddress(),port);
            new Thread(dnsquery).start();
            
        }
        //serverSocket.close();

    }
            
    
}
