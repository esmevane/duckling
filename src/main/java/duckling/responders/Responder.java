package duckling.responders;

import duckling.Configuration;
import duckling.requests.Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public abstract class Responder {
    protected Request request;
    protected Configuration config;
    protected ArrayList<String> allowedMethods = new ArrayList<>();

    {
        this.allowedMethods.add("GET");
        this.allowedMethods.add("HEAD");
        this.allowedMethods.add("OPTIONS");
    }

    public Responder(Request request) {
        this(request, new Configuration());
    }

    public Responder(Request request, Configuration config) {
        this.config = config;
        this.request = request;
    }

    abstract public boolean matches();

    abstract public ArrayList<String> headers() throws IOException;

    abstract public InputStream body() throws IOException;

    abstract public boolean isAllowed();

}
