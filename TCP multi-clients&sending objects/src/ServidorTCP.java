import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorTCP {
    public static void main(String[] args) {
        final int PUERTO = 12345;

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado. Esperando conexiones...");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado desde " + cliente.getInetAddress().getHostAddress());

                new Thread(new ManejadorCliente(cliente)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ManejadorCliente implements Runnable {
        private Socket cliente;

        public ManejadorCliente(Socket cliente) {
            this.cliente = cliente;
        }

        @Override
        public void run() {
            try (ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
                 ObjectOutputStream salida = new ObjectOutputStream(cliente.getOutputStream())) {

                Llista lista = (Llista) entrada.readObject();
                System.out.println("Lista recibida del cliente: " + lista.getNom() + ", " + lista.getNumberList());

                List<Integer> listaOrdenada = new ArrayList<>(new TreeSet<>(lista.getNumberList()));

                Llista listaModificada = new Llista(lista.getNom(), listaOrdenada);
                salida.writeObject(listaModificada);
                System.out.println("Lista modificada enviada al cliente: " + listaModificada.getNom() + ", " + listaModificada.getNumberList());

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    cliente.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
