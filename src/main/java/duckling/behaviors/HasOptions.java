package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.CommonHeaders;
import duckling.responses.Response;

public class HasOptions implements Behavior {
    String allowedMethodsString;

    public HasOptions(String allowedMethodsString) {
        this.allowedMethodsString = allowedMethodsString;
    }

    @Override
    public Response apply(Request request) {
        Response newResponse = Response.wrap(request);

        return request.isOptions() ?
            newResponse.withHeader(CommonHeaders.ALLOW, allowedMethodsString).withoutBody() :
            newResponse.contentType("text/html");
    }
}
