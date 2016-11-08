import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import duckling.Server;

public class ServerTest {
    private int port = 5123, timeout = 100000;
    private Server server;

    @Before
    public void setup() throws Exception {
        server = new Server(port);
    }

    @Test
    public void definePort() throws Exception {
        assertEquals(port, server.port);
    }
}