import java.io.*;
import java.net.*;
import java.util.*;

public class ClienteTCP {
    public static void main(String[] args) {
        final String HOST = "localhost";
        final int PUERTO = 12345;

        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingrese el nombre de la lista: ");
            String nombreLista = scanner.nextLine();

            System.out.print("Ingrese los números de la lista separados por espacios: ");
            String numerosStr = scanner.nextLine();

            List<Integer> numeros = new ArrayList<>();
            String[] numerosArray = numerosStr.split("\\s+");
            for (String numStr : numerosArray) {
                try {
                    int num = Integer.parseInt(numStr);
                    numeros.add(num);
                } catch (NumberFormatException e) {
                    System.out.println("Error: \"" + numStr + "\" no es un número válido. Se omitirá.");
                }
            }

            Llista listaEnviar = new Llista(nombreLista, numeros);

            salida.writeObject(listaEnviar);
            System.out.println("Lista enviada al servidor: " + listaEnviar.getNom() + ", " + listaEnviar.getNumberList());

            Llista listaModificada = (Llista) entrada.readObject();
            System.out.println("Lista recibida del servidor: " + listaModificada.getNom() + ", " + listaModificada.getNumberList());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
