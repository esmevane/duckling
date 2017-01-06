package duckling.responders;

import duckling.requests.Request;
import duckling.responses.ResponseHeaders;
import duckling.Server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class NotFound extends Responder {
    public NotFound(Request request) {
        super(request);
    }

    @Override
    public InputStream body() {
        return new ByteArrayInputStream(rawResponse().getBytes());
    }

    @Override
    public ArrayList<String> headers() {
        if (request.isOptions()) {
            return new ResponseHeaders().allowedMethods(this.allowedMethods).toList();
        }

        return new ResponseHeaders().notFound().withContentType("text/html").toList();
    }

    @Override
    public boolean isAllowed() {
        return true;
    }

    @Override
    public boolean matches() {
        return true;
    }

    private String rawResponse() {
        return "<html><head><title>Not found</title></head>" +
            "<body>404 not found</body></head>" +
            Server.CRLF;
    }

}
