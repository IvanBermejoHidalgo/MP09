import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    InetAddress serverIP;
    int serverPort;
    DatagramSocket socket;
    Scanner sc;
    String nombre;
    int intentos = 5;

    public Cliente() {
        sc = new Scanner(System.in);
    }

    public void iniciar(String host, int port) throws SocketException, UnknownHostException {
        serverIP = InetAddress.getByName(host);
        serverPort = port;
        socket = new DatagramSocket();
    }

    public void ejecutarCliente() throws IOException {
        byte[] receivedData = new byte[1024];
        byte[] sendingData;

        sendingData = getFirstRequest();

        while (mustContinue(sendingData) && intentos >= 0) {
            DatagramPacket packet = new DatagramPacket(sendingData, sendingData.length, serverIP, serverPort);
            socket.send(packet);
            packet = new DatagramPacket(receivedData, 1024);
            socket.receive(packet);
            String receivedMessage = new String(packet.getData(), 0, packet.getLength());

            if (receivedMessage.equals("Correcto, has acertado la palabra. El servidor ha pensado en otra palabra")) {
                System.out.println("¡Has adivinado la palabra! Enviando señal de cierre al servidor.");
                sendingData = "Adivinado".getBytes();
                packet = new DatagramPacket(sendingData, sendingData.length, serverIP, serverPort);
                socket.send(packet);
                break;
            }

            sendingData = getDataToRequest(packet.getData(), packet.getLength());
        }
    }

    private byte[] getDataToRequest(byte[] data, int length) {
        String recibido = new String(data, 0, length);
        intentos--;

        String mensaje = "";
        if (intentos > 0) {
            System.out.print(nombre + "(" + recibido + ") Te quedan " + intentos + " intentos!" + "\n");
            mensaje = sc.nextLine();
        }
        return mensaje.getBytes();
    }

    private byte[] getFirstRequest() {
        System.out.println("Escribe tu nombre:");
        nombre = sc.nextLine();
        System.out.println("Digita una palabra de 5 caracteres:");
        String palabra = sc.nextLine();
        return palabra.getBytes();
    }

    private boolean mustContinue(byte[] data) {
        String mensaje = new String(data).toLowerCase();
        return !mensaje.equals("adios");
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        try {
            cliente.iniciar("localhost", 5566);
            cliente.ejecutarCliente();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

