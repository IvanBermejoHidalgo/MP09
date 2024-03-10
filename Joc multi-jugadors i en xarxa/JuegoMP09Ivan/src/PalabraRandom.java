import java.util.Random;

public class PalabraRandom {

    private static final String[] paraules = {
            "Manta", "Pezon", "Lugar", "Motel","Ruido", "Rapido", "Banco", "Pezon", "Rocio",
            "Viejo", "Korea", "Salsa", "Cocer","Salsa", "Rapido", "Danza", "Lista", "Cuter",
            "Elige", "Ergon", "Hacha", "Redes", "Dardo", "Golpe", "Enloa", "Mundo", "Manco", "Suter",
            "Yerno", "Suelto", "Fuego", "Rieso", "Falso", "Grado", "Akoza", "Cotea",
            "Hozar", "Jinio", "Yerno", "Hecho", "Ruina", "Relay", "Junia"
    };

    private String paraula;

    public PalabraRandom() {
        paraula = "";
        pensa();
    }

    public String pensa() {
        int numero = new Random().nextInt(paraules.length);
        paraula = paraules[numero];
        return paraula;
    }

    public String comprova(String n) {
        return paraula.equals(n) ? "0" : "1";
    }

    public String getParaula() {
        return paraula;
    }

    public void setParaula(String paraula) {
        this.paraula = paraula;
    }

    @Override
    public String toString() {
        return "ParaulaAleatoria{" +
                "paraula='" + paraula + '\'' +
                '}';
    }

    public static String[] getParaules() {
        return paraules;
    }
}