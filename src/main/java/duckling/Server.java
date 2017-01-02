package duckling;

import duckling.requests.RequestHandler;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    public static final String CRLF = "\r\n";

    private Configuration config;
    private volatile boolean shuttingDown = false;
    private InetSocketAddress address;
    private ServerSocket connection;
    private ExecutorService pool;
    private Logger logger;

    public Server(Configuration config) throws IOException {
        this(
            config,
            new ServerSocket(),
            Executors.newCachedThreadPool(),
            new Logger()
        );
    }

    public Server(
        Configuration config,
        ServerSocket connection,
        ExecutorService pool,
        Logger logger
    ) throws IOException {
        this.config = config;

        this.address = new InetSocketAddress(this.config.port);
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
        RequestHandler handler = new RequestHandler(
            this.connection.accept(),
            this.config
        );

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

        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));

        while (!this.shuttingDown) onRequest();
    }

}
