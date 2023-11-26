import java.util.LinkedList;

public class EjercicioBarbero {
    public static class Barbero extends Thread{
        Barberia barberia;
        public Barbero(Barberia barberia){
            this.barberia = barberia;
        }
        @Override
        public void run(){
            for(;;) {
                barberia.cortar();
            }
        }
    }


    public static class Cliente extends Thread{
        Barberia barberia;
        public Cliente(String name, Barberia barberia){
            super(name);
            this.barberia = barberia;
        }
        @Override
        public void run(){
            barberia.EntrarBarberia(this);
        }
    }

    public static class Barberia {
        int sillasespera;
        final LinkedList<Cliente> clientes;
        public Barberia() {
            sillasespera = 4;
            clientes = new LinkedList<>();
        }
        public void cortar(){
            Cliente cliente;
            synchronized (clientes){
                while(clientes.size() == 0){
                    System.out.println("Barber dormido.");
                    System.out.println("Hay " + clientes.size() + " clientes en las sillas de espera.");
                    try{
                        clientes.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                System.out.println("El cliente se sienta en la silla del barbero.");
                cliente = clientes.poll();
            }
            if (cliente == null) {
                throw new IllegalStateException("La variable cliente es nula");
            }
            System.out.println("Barbero ocupado con cliente " + cliente.getName() + ".");
            try {
                Thread.sleep((long) (Math.random()*8000)+6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("El barbero ha acabado con el cliente " + cliente.getName() + ".");
        }
        public void EntrarBarberia(Cliente cliente){
            System.out.println("Entra el cliente " + cliente.getName() + ".");
            synchronized (clientes){
                if(clientes.size() == sillasespera) {
                    System.out.println("No hay sillas de espera suficientes.");
                    System.out.println("El cliente" + cliente.getName()+ " se va de la barberia.");
                    return;
                }
                clientes.offer(cliente);
                System.out.println("El cliente se sienta en la silla de espera.");
                System.out.println("Hay " + clientes.size() + " clientes en las sillas de espera.");
                if(clientes.size() == 1) {
                    clientes.notify();
                }
            }
        }
    }

    public static void main(String[] args) {
        Barberia barberia = new Barberia();
        Barbero barbero = new Barbero(barberia);
        int contadorclientes = 1;
        barbero.start();
        Cliente cliente;
        for(int i = 0; i < 10; i++){
            try {
                Thread.sleep((long) (Math.random()*3000)+2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            cliente = new Cliente(String.valueOf(contadorclientes),barberia);
            cliente.start();
            contadorclientes++;
        }
    }
}