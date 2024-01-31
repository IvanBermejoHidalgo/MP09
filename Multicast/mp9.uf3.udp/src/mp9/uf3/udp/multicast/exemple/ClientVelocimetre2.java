package mp9.uf3.udp.multicast.exemple;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ClientVelocimetre2 {
	/*Client que s'afegeix al Multicast SrvVelocitats.java que treu mitjanaes de velocitat */
	
	//private boolean continueRunning = true;
	private MulticastSocket socket;
    private InetAddress multicastIP;
    private int port;
    //private List<Integer> velocitats;
    //private Integer sumVel;
    NetworkInterface netIf;
    InetSocketAddress group;

	public ClientVelocimetre2(int portValue, String strIp) throws IOException {
		multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        socket = new MulticastSocket(port);
        //sumVel = 0;
        //velocitats = new ArrayList();
        netIf = socket.getNetworkInterface();
        group = new InetSocketAddress(strIp,portValue);
	}

	public void runClient() throws IOException{
        DatagramPacket packet;
        byte [] receivedData = new byte[1024];
        
        socket.joinGroup(group,netIf);
        System.out.printf("Connectat a %s:%d%n",group.getAddress(),group.getPort());
        
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
        }

        socket.leaveGroup(group,netIf);
        socket.close();*/
        while (true) {
            packet = new DatagramPacket(receivedData, receivedData.length);
            socket.receive(packet);
            String receivedMessage = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Palabras recibidas: " + receivedMessage);
        }
    }
	
	/*protected  boolean getData(byte[] data) {
		boolean ret=true;
		
        int v = ByteBuffer.wrap(data).getInt();
        velocitats.add(v);
        //int ti = (int) System.currentTimeMillis();
        
        if(velocitats.size() % 5 == 0) {
        	for (Integer mark : velocitats) {
                sumVel += mark;
            }
        //	int te = (int) System.currentTimeMillis();
        	System.out.println("Velocitat mitjana: " + sumVel.doubleValue() / velocitats.size());
        	sumVel = 0;
        	velocitats.clear();
        }
        
        //if (v==0) ret=false;
                   
		return ret;
    }*/
	
	public static void main(String[] args) throws IOException {
		ClientVelocimetre2 cvel = new ClientVelocimetre2(5557, "224.0.11.111");
		cvel.runClient();
		System.out.println("Parat!");

	}

}
