package mp9.uf3.udp.unicast.apunts;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatagramSocketClientTest {

    @Test
    public void testMustContinue() { //comprovem si el client ha de continuar
        DatagramSocketClient client = new DatagramSocketClient();
        byte[] data = "adeu".getBytes();
        assertFalse(client.mustContinue(data));

        data = "hola".getBytes();
        assertTrue(client.mustContinue(data));
    }
}
