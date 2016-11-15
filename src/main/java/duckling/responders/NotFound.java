package duckling.responders;

import duckling.requests.Request;
import duckling.ResponseHeaders;
import duckling.Server;

import java.io.OutputStream;
import java.io.IOException;

public class NotFound extends Responder {
    public NotFound(Request request, OutputStream outputStream) {
        super(request, outputStream);
    }

    @Override
    public boolean matches() {
        return true;
    }

    @Override
    public void respond() throws IOException {
        ResponseHeaders responseHeaders = new ResponseHeaders().
                notFound().
                withContentType("text/html");

        for (String line: responseHeaders.toArray()) {
            this.outputStream.write(line.getBytes());
        }

        this.outputStream.write(rawResponse().getBytes());
    }

    private String rawResponse() {
       return "<html><head><title>Not found</title></head>" +
           "<body>404 not found</body></head>" +
           Server.CLRF;
    }

}
