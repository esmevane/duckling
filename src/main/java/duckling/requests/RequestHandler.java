package duckling.requests;

import duckling.Configuration;
import duckling.Logger;
import duckling.responders.Responders;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler implements Runnable {
    private final Configuration config;
    private final Socket client;
    private final Logger logger;
    private ArrayList<String> loggables = new ArrayList<>();

    private Request request;

    public RequestHandler(Socket client, Configuration config) {
        this(client, config, new Logger());
    }

    public RequestHandler(Socket client, Configuration config, Logger logger) {
        this(client, config, logger, new Request(config));
    }

    public RequestHandler(Socket client, Configuration config, Logger logger, Request request) {
        this.client = client;
        this.config = config;
        this.logger = logger;
        this.request = request;
    }

    @Override
    public void run() {
        try {
            RequestStream requestStream = buildRequestStream();
            List<String> requestLines = requestStream.toList();

            this.loggables.addAll(requestLines);
            this.request.add(requestLines);

            Responders.respondTo(request, this.client.getOutputStream(), this.config);

            this.client.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            this.loggables.forEach(this.logger::info);
        }
    }

    public RequestStream buildRequestStream() throws IOException {
        return new RequestStream(this.client.getInputStream());
    }

}
