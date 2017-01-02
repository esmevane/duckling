package duckling.responders;

import duckling.Configuration;
import duckling.requests.Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Responder {
    protected Request request;
    protected Configuration config;
    protected ArrayList<String> allowedMethods = new ArrayList<>(
        Arrays.asList("GET", "HEAD", "OPTIONS")
    );

    public Responder(Request request) {
        this(request, new Configuration());
    }

    public Responder(Request request, Configuration config) {
        this.config = config;
        this.request = request;
    }

    abstract public boolean matches();

    abstract public ArrayList<String> headers();

    abstract public InputStream body();

    public boolean isAllowed() {
        return this.allowedMethods.contains(this.request.getMethod());
    }

}
