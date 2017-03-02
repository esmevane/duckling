package duckling.requests;

import duckling.Configuration;
import duckling.MemoryCache;
import duckling.Server;
import duckling.responders.Responders;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class RequestHandler implements Runnable {
    private final Configuration config;
    private final Socket client;

    private Request request;

    public RequestHandler(Socket client, Configuration config) {
        this(client, config, new Request(config));
    }

    public RequestHandler(Socket client, Configuration config, Request request) {
        this.client = client;
        this.config = config;
        this.request = request;
    }

    @Override
    public void run() {
        try {
            RequestStream requestStream = buildRequestStream();
            List<String> requestLines = requestStream.toList();

            request.add(requestLines);

            MemoryCache.shortList("/logs", request.baseRequest.toString() + Server.CRLF);
            Responders.respondTo(request, this.client.getOutputStream(), this.config);

            this.client.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public RequestStream buildRequestStream() throws IOException {
        return new RequestStream(this.client.getInputStream());
    }

}
