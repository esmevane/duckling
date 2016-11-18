package duckling.responders;

import duckling.requests.Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public abstract class Responder {
    protected Request request;

    public Responder(Request request) {
        this.request = request;
    }

    abstract public boolean matches();

    abstract public ArrayList<String> headers() throws IOException;
    abstract public InputStream body() throws IOException;

}
