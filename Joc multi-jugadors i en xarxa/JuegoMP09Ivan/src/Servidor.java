import java.io.IOException;
import java.net.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private DatagramSocket socket;
    private int port;
    private GuessHandler guessHandler;
    private String randomWord;

    public Servidor(int port) {
        try {
            socket = new DatagramSocket(port);
            System.out.printf("Servidor abierto en el puerto %d%n", port);
            this.port = port;
            this.randomWord = getRandomWord(5);
            guessHandler = new GuessHandler(randomWord);
            System.out.println("Palabra generada: " + randomWord);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void runServer() {
        try {
            while (true) {
                byte[] receivingData = new byte[1024];
                DatagramPacket packet = new DatagramPacket(receivingData, receivingData.length);
                socket.receive(packet);
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                String respuesta = procesarRespuesta(receivedMessage);

                DatagramPacket sendPacket = new DatagramPacket(respuesta.getBytes(), respuesta.getBytes().length, clientAddress, clientPort);
                socket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private String procesarRespuesta(String guess) {
        String respuesta;
        if (guess.equalsIgnoreCase("Adivinado")) {
            respuesta = "Correcto, has acertado la palabra. El servidor ha pensado en otra palabra";
            randomWord = getRandomWord(5);
            guessHandler = new GuessHandler(randomWord);
        } else {
            respuesta = guessHandler.handleGuess(guess);
            if (guessHandler.isWordGuessed()) {
                respuesta += "\nHas adivinado la palabra. Enviando señal de cierre al servidor.";
                respuesta = "Adivinado";
            }
        }
        return respuesta;
    }

    private String getRandomWord(int longitud) {
        PalabraRandom palabraRandom = new PalabraRandom();
        String palabra;
        do {
            palabra = palabraRandom.pensa();
        } while (palabra.length() != longitud);
        return palabra;
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor(5566);
        servidor.runServer();
        System.out.println("Fin del Servidor");
    }
}

class GuessHandler {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLACK = "\u001B[30m";
    private String chosenWord;
    private boolean[] wordGuessed;

    public GuessHandler(String chosenWord) {
        this.chosenWord = chosenWord;
        this.wordGuessed = new boolean[chosenWord.length()];
    }

    public String handleGuess(String guess) {
        boolean guessIsCorrect = false;

        for (int i = 0; i < chosenWord.length(); i++) {
            if (guess.length() == 1 && guess.charAt(0) == chosenWord.charAt(i)) {
                wordGuessed[i] = true;
                guessIsCorrect = true;
            } else if (guess.equals(chosenWord)) {
                wordGuessed[i] = true;
                guessIsCorrect = true;
            }
        }

        String wordGuessedSoFar = checkWord(chosenWord, guess);

        String serverResponse;

        if (guessIsCorrect) {
            serverResponse = "Correcto! la palabra era:  " + wordGuessedSoFar;
        } else {
            serverResponse = "Incorrecto. " + wordGuessedSoFar + ":  Vuelve a intentarlo ";
        }
        if (guess.length() != 5) {
            serverResponse = ANSI_RED + "Solo se aceptan 5 caracteres" + ANSI_RESET;
        }
        return serverResponse;
    }

    public String checkWord(String secretWord, String guess) {
        String correctLetters = "";
        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == guess.charAt(i)) {
                correctLetters += ANSI_BLACK + ANSI_GREEN + Character.toUpperCase(guess.charAt(i)) + ANSI_RESET;
            } else if (secretWord.contains(String.valueOf(guess.charAt(i)))) {
                correctLetters += ANSI_YELLOW + Character.toLowerCase(guess.charAt(i)) + ANSI_RESET;
            } else {
                correctLetters += "-";
            }
        }
        return correctLetters;
    }

    public boolean isWordGuessed() {
        for (boolean b : wordGuessed) {
            if (!b) {
                return false;
            }
        }
        return true;
    }
}
