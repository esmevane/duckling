package duckling.requests;

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
    private final Socket client;
    private final String root;
    private ArrayList<String> loggables = new ArrayList<>();

    public RequestHandler(Socket client, String root) {
        this.client = client;
        this.root = root;
    }

    @Override
    public void run() {
        try {
            OutputStream output = this.client.getOutputStream();
            Request request = marshalRequest();
            Responder responder = getResponder(request);

            new HeadersWriter(responder, output).write();
            new BodyWriter(responder, output).write();

            Logger logger = getLogger();
            for (String loggable : loggables) {
                logger.info(loggable);
            }

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
        return new Request(this.root);
    }

    public RequestStream buildRequestStream() throws IOException {
        return new RequestStream(client.getInputStream());
    }

    public Responder getResponder(Request request) throws IOException {
        Responders responders = new Responders(request);

        return responders.getResponder();
    }

    public Logger getLogger() {
        return new Logger();
    }

}
