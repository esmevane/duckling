package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.CommonHeaders;
import duckling.responses.Response;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class HasOptions implements Behavior {
    private String allowedMethodsString;

    public HasOptions(String allowedMethodsString) {
        this.allowedMethodsString = allowedMethodsString;
    }

    public HasOptions(ArrayList<String> allowedMethods) {
        this(allowedMethods.stream().collect(Collectors.joining(",")));
    }

    @Override
    public Response apply(Request request) {
        Response newResponse = Response.wrap(request);

        return request.isOptions() ?
            newResponse.withHeader(CommonHeaders.ALLOW, allowedMethodsString) :
            newResponse.withContentType("text/html");
    }
}
