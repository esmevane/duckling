package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;

import java.util.ArrayList;

public class RestrictToMethods implements Behavior {
    ArrayList<String> allowedMethods;

    public RestrictToMethods(ArrayList<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    @Override
    public Response apply(Request request) {
        Response newResponse = Response.wrap(request);

        return allowedMethods.contains(request.getMethod()) ?
            newResponse :
            newResponse.respondWith(ResponseCodes.METHOD_NOT_ALLOWED).contentType("null");
    }
}
