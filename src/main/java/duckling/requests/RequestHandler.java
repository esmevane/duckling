package duckling.requests;

import duckling.Configuration;
import duckling.writers.BodyWriter;
import duckling.writers.HeadersWriter;
import duckling.Logger;
import duckling.responders.Responder;
import duckling.responders.Responders;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class RequestHandler implements Runnable {
    private final Configuration config;
    private final Socket client;
    private final Logger logger;
    private ArrayList<String> loggables = new ArrayList<>();

    public RequestHandler(Socket client, Configuration config) {
        this(client, config, new Logger());
    }

    public RequestHandler(Socket client, Configuration config, Logger logger) {
        this.client = client;
        this.config = config;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            handleRequest();
            this.client.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    private void handleRequest() throws IOException {
        OutputStream output = this.client.getOutputStream();
        RequestStream requestStream = buildRequestStream();
        Request request = prepareRequest();

        Consumer<String> recordLine = line -> {
            request.add(line);
            loggables.add(line);
        };

        requestStream.toList().forEach(recordLine);

        Responder responder = getResponder(request);
        HeadersWriter headers = new HeadersWriter(responder, output);
        BodyWriter body = new BodyWriter(responder, output);

        loggables.add("");
        loggables.add("Responding with " + responder.getClass().toString());
        loggables.add("");
        loggables.forEach(logger::info);

        headers.write();
        if (!request.getMethod().equals("HEAD")) body.write();
    }

    public Request prepareRequest() {
        return new Request(this.config.root);
    }

    public RequestStream buildRequestStream() throws IOException {
        return new RequestStream(this.client.getInputStream());
    }

    public Responder getResponder(Request request) throws IOException {
        Responders responders = new Responders(request, this.config);

        return responders.getResponder();
    }

}
