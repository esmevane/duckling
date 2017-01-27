package duckling.responders;

import duckling.requests.Request;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Responder {
    protected Request request;
    protected ArrayList<String> allowedMethods = new ArrayList<>(
        Arrays.asList("GET", "HEAD", "OPTIONS")
    );

    public Responder(Request request) {
        this.request = request;
    }

    abstract public InputStream body();

    abstract public ArrayList<String> headers();

    public boolean isAllowed() {
        return this.allowedMethods.contains(this.request.getMethod());
    }

    abstract public boolean matches();

}
