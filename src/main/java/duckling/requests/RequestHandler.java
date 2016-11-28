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
    private ArrayList<String> loggables = new ArrayList<>();

    public RequestHandler(Socket client, Configuration config) {
        this.client = client;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            OutputStream output = this.client.getOutputStream();
            Request request = marshalRequest();
            Responder responder = getResponder(request);
            Logger logger = getLogger();

            new HeadersWriter(responder, output).write();
            new BodyWriter(responder, output).write();

            loggables.forEach(logger::info);

            this.client.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

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

    public Logger getLogger() {
        return new Logger();
    }

}
