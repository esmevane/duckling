package duckling;

import static org.junit.Assert.assertEquals;

import duckling.support.MockServerSocket;
import duckling.support.MockThreadPool;
import org.junit.Before;
import org.junit.Test;

public class ServerTest {
    private int port = 5123;
    private String root = "./";
    private Server server;
    private MockServerSocket connection;
    private MockThreadPool pool;

    @Before
    public void setup() throws Exception {
        connection = new MockServerSocket();
        pool = new MockThreadPool();
        server = new Server(port, root, connection, pool);
    }

    @Test
    public void definePort() throws Exception {
        assertEquals(port, server.port);
    }

    @Test
    public void defineRoot() throws Exception {
        assertEquals(root, server.root);
    }

    @Test
    public void beforeListenBindsConnection() throws Exception {
        server.onBegin();
        assertEquals(true, connection.isBound());
    }

    @Test
    public void onShutdownClosesConnection() throws Exception {
        server.onBegin();
        server.onShutdown();
        assertEquals(true, connection.isClosed());
    }

    @Test
    public void onShutdownClosesPool() throws Exception {
        server.onShutdown();
        assertEquals(true, server.isPoolClosed());
    }

    @Test
    public void onSocketRequestLaunchesThread() throws Exception {
        server.onRequest();
        assertEquals(1, pool.getThreadCount());
    }

}