public class Main {
    public static void main(String[] args) {
        System.out.println(53834169 % 3);

        String[] paraules = PalabraRandom.getParaules();

        int numero = (int) (Math.random() * paraules.length);
        System.out.println(paraules[numero]);
    }
}
