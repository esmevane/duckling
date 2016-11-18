package duckling;

import duckling.requests.RequestHandler;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    public static final String CRLF = "\r\n";

    int port;
    String root;

    private volatile boolean shuttingDown = false;
    private InetSocketAddress address;
    private ServerSocket connection;
    private ExecutorService pool;
    private Logger logger;

    public Server(int port, String root) throws IOException {
        this(
                port,
                root,
                new ServerSocket(),
                Executors.newCachedThreadPool(),
                new Logger()
        );
    }

    public Server(
            int port,
            String root,
            ServerSocket connection,
            ExecutorService pool,
            Logger logger
    ) throws IOException {
        this.port = port;
        this.root = root;

        this.address = new InetSocketAddress(this.port);
        this.connection = connection;
        this.pool = pool;
        this.logger = logger;
    }

    public boolean isPoolClosed() {
        return this.pool.isShutdown();
    }

    public void onBegin() throws IOException {
        this.connection.bind(this.address);
    }

    public void onRequest() throws IOException {
        RequestHandler handler = new RequestHandler(this.connection.accept(), root);
        this.pool.execute(handler);
    }

    public void onShutdown() {
        try {
            this.shuttingDown = true;
            this.pool.shutdown();
            this.connection.close();
            this.logger.info(CRLF + "Shutting down.");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void listen() throws IOException {
        onBegin();

        Runnable shutdown = this::onShutdown;
        Runtime.getRuntime().addShutdownHook(new Thread(shutdown));

        while (notShuttingDown()) onRequest();
    }

    private boolean notShuttingDown() {
        return !this.shuttingDown;
    }
}
