import static org.junit.Assert.assertEquals;
//
//import org.junit.Before;
import org.junit.Test;

import duckling.Server;

public class ServerTest {
    @Test
    public void setPort() throws Exception {
        int port = 5000;
        Server server = new Server(port);

        assertEquals(port, server.port);
    }
}