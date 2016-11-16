package duckling;

import duckling.requests.Request;
import duckling.requests.RequestBuilder;
import duckling.responders.Responders;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    public static final String CRLF = "\r\n";

    public int port;
    public String root;

    private boolean shuttingDown = false;
    private InetSocketAddress address;
    private ServerSocket connection;
    private ExecutorService pool;

    public Server(int port, String root) throws IOException {
        this(
            port,
            root,
            new ServerSocket(),
            Executors.newCachedThreadPool()
        );
    }

    public Server(
        int port,
        String root,
        ServerSocket connection,
        ExecutorService pool
    ) throws IOException {
        this.port = port;
        this.root = root;

        this.address = new InetSocketAddress(this.port);
        this.connection = connection;
        this.pool = pool;
    }

    public boolean isPoolClosed() {
        return this.pool.isShutdown();
    }

    public void onBegin() throws IOException {
        this.connection.bind(this.address);
    }

    public void onRequest() throws IOException {
        Socket client = this.connection.accept();
        Runnable handler = () -> {
            try {
                RequestBuilder builder = new RequestBuilder(
                    this.root,
                    client.getInputStream()
                );
                Request request = builder.build();
                Responders responders = new Responders(
                    request,
                    client.getOutputStream()
                );

                builder.printRawRequest();
                responders.getResponder().respond();

                client.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        };

        this.pool.execute(handler);
    }

    public void onShutdown() {
        try {
            this.pool.shutdown();
            this.connection.close();
            this.shuttingDown = true;

            System.out.println(CRLF + "Shutting down.");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void listen() throws IOException {
        onBegin();

        Runnable shutdown = this::onShutdown;
        Runtime.getRuntime().addShutdownHook(new Thread(shutdown));

        while(notShuttingDown()) onRequest();
    }

    private boolean notShuttingDown() { return !this.shuttingDown; }
}
