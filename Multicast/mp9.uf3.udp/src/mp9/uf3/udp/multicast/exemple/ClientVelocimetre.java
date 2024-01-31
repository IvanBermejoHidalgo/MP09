package mp9.uf3.udp.multicast.exemple;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class ClientVelocimetre {
/* Client afegit al grup multicast SrvVelocitats.java que representa un velocímetre */

	private boolean continueRunning = true;
    private MulticastSocket socket;
    private InetAddress multicastIP;
    private int port;
    NetworkInterface netIf;
    InetSocketAddress group;
    private Map<String, Integer> contadorPalabras = new HashMap<>();
    private String[] palabras;


    public ClientVelocimetre(int portValue, String strIp) throws IOException {
		multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        socket = new MulticastSocket(port);
        //netIf = NetworkInterface.getByName("enp1s0");
        netIf = socket.getNetworkInterface();
        group = new InetSocketAddress(strIp,portValue);
	}

        public void runClient() throws IOException {
            DatagramPacket packet;
            byte[] receivedData = new byte[1024];

            socket.joinGroup(group, netIf);
            System.out.printf("Connectat a %s:%d%n", group.getAddress(), group.getPort());

            while (true) {
                packet = new DatagramPacket(receivedData, receivedData.length);
                socket.receive(packet);
                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Paraules rebudes: " + receivedMessage);
            }
        }

        
        /*while(continueRunning){
           packet = new DatagramPacket(receivedData, 4);
           socket.setSoTimeout(5000);
           try{
                socket.receive(packet);
                continueRunning = getData(packet.getData());
            }catch(SocketTimeoutException e){
                System.out.println("S'ha perdut la connexió amb el servidor.");
                continueRunning = false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        socket.leaveGroup(group,netIf);
        socket.close();*/


	
	/*protected  boolean getData(byte[] data) {
		boolean ret=true;
		
        int v = ByteBuffer.wrap(data).getInt();
        
        //pintem velocimetre
        for(int i=0;i<v;i++) System.out.print("#");
        System.out.print("\n");

        //if (v==1) ret=false;
        
		return ret;
    }*/
	
	public static void main(String[] args) throws IOException {
		ClientVelocimetre cvel = new ClientVelocimetre(5557, "224.0.11.111");
		cvel.runClient();
		System.out.println("Parat!");

	}

}
