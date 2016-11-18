package duckling;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;

import duckling.support.SpyLogger;
import duckling.support.SpyServerSocket;
import duckling.support.SpyThreadPool;
import org.junit.Before;
import org.junit.Test;

public class ServerTest {
    private int port = 5123;
    private String root = "./";
    private Server server;
    private SpyServerSocket connection;
    private SpyThreadPool pool;
    private SpyLogger logger;

    @Before
    public void setup() throws Exception {
        connection = new SpyServerSocket();
        pool = new SpyThreadPool();
        logger = new SpyLogger();
        server = new Server(port, root, connection, pool, logger);
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
    public void onShutdownLogsOutput() throws Exception {
        server.onShutdown();
        assertThat(logger.messages, hasItem(Server.CRLF + "Shutting down."));
    }

    @Test
    public void onSocketRequestLaunchesThread() throws Exception {
        server.onRequest();
        assertEquals(1, pool.getThreadCount());
    }

}