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
    public boolean matches() {
        return true;
    }

    @Override
    public ArrayList<String> headers() throws IOException {
        return new ResponseHeaders().notFound().withContentType("text/html").toList();
    }

    @Override
    public InputStream body() throws IOException {
        return new ByteArrayInputStream(rawResponse().getBytes());
    }

    private String rawResponse() {
        return "<html><head><title>Not found</title></head>" +
            "<body>404 not found</body></head>" +
            Server.CRLF;
    }

}
