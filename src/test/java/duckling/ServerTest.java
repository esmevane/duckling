package duckling;

import static org.junit.Assert.assertEquals;

import duckling.support.SpyServerSocket;
import duckling.support.SpyThreadPool;
import org.junit.Before;
import org.junit.Test;

public class ServerTest {

    private Server server;
    private SpyServerSocket connection;
    private SpyThreadPool pool;

    @Before
    public void setup() throws Exception {
        String root = "./";
        int port = 5123;
        Configuration config = new Configuration(port, root);
        connection = new SpyServerSocket();
        pool = new SpyThreadPool();
        server = new Server(config, connection, pool);
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