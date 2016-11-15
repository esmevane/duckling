package duckling.responders;

import duckling.requests.Request;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Responder {
    protected Request request;
    protected OutputStream outputStream;

    public Responder(Request request, OutputStream outputStream) {
        this.request = request;
        this.outputStream = outputStream;
    }

    abstract public boolean matches();
    abstract public void respond() throws IOException;
}
