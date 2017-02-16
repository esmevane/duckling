package duckling.responders;

import duckling.requests.Request;
import duckling.responses.Response;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class Responder {
    Response response;
    Request request;
    ArrayList<String> allowedMethods = new ArrayList<>(
        Arrays.asList("GET", "HEAD", "OPTIONS")
    );

    public Responder(Request request) {
        this.response = Response.wrap(request);
        this.request = request;
    }

    public InputStream body() {
        return response.compose().getBody();
    }

    public ArrayList<String> headers() {
        return response.compose().getResponseHeaders();
    }

    public String allowedMethodsString() {
        return allowedMethods.stream().collect(Collectors.joining(","));
    }

    public boolean isAllowed() {
        return allowedMethods.contains(request.getMethod());
    }

    abstract public boolean matches();

}
