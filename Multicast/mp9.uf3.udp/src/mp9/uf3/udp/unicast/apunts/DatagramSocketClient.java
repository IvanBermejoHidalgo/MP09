package mp9.uf3.udp.unicast.apunts;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

/**
 * Created by jordi
 * Exemple Client UDP extret dels apunts del IOC i ampliat
 * El server és DatagramSocketServer
 *
 * Aquest client reb del server el mateix que se li envia
 * Si s'envia adeu s'acaba la connexió
 */

public class DatagramSocketClient {
    InetAddress serverIP;
    int serverPort;
    DatagramSocket socket;
    Scanner sc;
    String nom;

    public DatagramSocketClient() {
        sc = new Scanner(System.in);
    }

    public void init(String host, int port) throws SocketException, UnknownHostException {
        serverIP = InetAddress.getByName(host);
        serverPort = port;
        socket = new DatagramSocket();
    }

    public void runClient() throws IOException {
        byte [] receivedData = new byte[1024];
        byte [] sendingData;

        sendingData = getFirstRequest();
        while (mustContinue(sendingData)) {
            DatagramPacket packet = new DatagramPacket(sendingData,sendingData.length,serverIP,serverPort);
            socket.send(packet);
            packet = new DatagramPacket(receivedData,1024);
            socket.receive(packet);
            sendingData = getDataToRequest(packet.getData(), packet.getLength());
        }

    }

    //Resta de conversa que se li envia al server
    private byte[] getDataToRequest(byte[] data, int length) {
        String rebut = new String(data,0, length);
        //Imprimeix el nom del client + el que es rep del server i demana més dades
        System.out.print(nom+"("+rebut+")"+"> ");
        String msg = sc.nextLine();
        return msg.getBytes();
    }

    //primer missatge que se li envia al server
    private byte[] getFirstRequest() {
        System.out.println("Entra el teu nom: ");
        nom = sc.nextLine();
        return nom.getBytes();
    }

    //Si se li diu adeu al server, el client es desconnecta
    public boolean mustContinue(byte [] data) {
        String msg = new String(data).toLowerCase();
        return !msg.equals("adeu");
    }

    public static void main(String[] args) {
        DatagramSocketClient client = new DatagramSocketClient();
        try {
            client.init("localhost",5555);
            client.runClient();
        } catch (IOException e) {
            e.getStackTrace();
        }

    }

}
