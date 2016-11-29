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
            loggables.forEach(logger::info);

            this.client.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    private void handleRequest() throws IOException {
        OutputStream output = this.client.getOutputStream();
        Request request = marshalRequest();
        Responder responder = getResponder(request);

        new HeadersWriter(responder, output).write();
        new BodyWriter(responder, output).write();
    }

    private Request marshalRequest() throws IOException {
        RequestStream requestStream = buildRequestStream();
        Request request = prepareRequest();

        requestStream.toList().forEach(requestLine -> {
            request.add(requestLine);
            loggables.add(requestLine);
        });

        loggables.add("");

        return request;
    }

    public Request prepareRequest() {
        return new Request(this.config.root);
    }

    public RequestStream buildRequestStream() throws IOException {
        return new RequestStream(client.getInputStream());
    }

    public Responder getResponder(Request request) throws IOException {
        Responders responders = new Responders(request, config);

        return responders.getResponder();
    }

}
