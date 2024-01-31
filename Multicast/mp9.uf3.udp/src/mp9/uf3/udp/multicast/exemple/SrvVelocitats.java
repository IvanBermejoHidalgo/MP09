package mp9.uf3.udp.multicast.exemple;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SrvVelocitats {
/* Servidor Multicast que ens comunica la velocitat que porta d'un objecte */
	
	MulticastSocket socket;
	InetAddress multicastIP;
	int port;
	boolean continueRunning = true;
	private Random random = new Random();
	private String[] paraules = {"ivan", "bermejo", "hidalgo", "dam", "elpuig"};
	Velocitat simulator;
	private Map<String, Integer> contadorPalabras = new HashMap<>();


	public SrvVelocitats(int portValue, String strIp) throws IOException {
		socket = new MulticastSocket(portValue);
		multicastIP = InetAddress.getByName(strIp);
		port = portValue;
	}

	public void contarPalabra(String palabra) {
		contadorPalabras.put(palabra, contadorPalabras.getOrDefault(palabra, 0) + 1);
	}

	public void enviarPalabra(String palabra) throws IOException {
		byte[] sendingData = palabra.getBytes();
		DatagramPacket packet = new DatagramPacket(sendingData, sendingData.length, multicastIP, port);
		socket.send(packet);
	}
	
	public void runServer() throws IOException{
		//DatagramPacket packet;
		//byte [] sendingData;

		while(continueRunning){
			String paraula = paraules[random.nextInt(paraules.length)];
			//byte[] sendingData = paraula.getBytes();
			//sendingData = ByteBuffer.allocate(4).putInt(simulator.agafaVelocitat()).array();
			//packet = new DatagramPacket(sendingData, sendingData.length,multicastIP, port);
			contarPalabra(paraula);
			enviarPalabra(paraula);
			System.out.println("Contador de palabras: " + contadorPalabras);
			byte[] sendingData = paraula.getBytes();
			DatagramPacket packet = new DatagramPacket(sendingData, sendingData.length, multicastIP, port);

			socket.send(packet);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException ex) {
				ex.getMessage();
			}


		}
		socket.close();
	}

	public static void main(String[] args) throws IOException {
		//Canvieu la X.X per un número per formar un IP.
		//Que no sigui la mateixa que la d'un altre company
		SrvVelocitats srvVel = new SrvVelocitats(5557, "224.0.11.111");
		srvVel.runServer();
		System.out.println("Parat!");

	}

}
