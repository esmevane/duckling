package duckling.responders;

import duckling.behaviors.HasOptions;
import duckling.requests.Request;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;
import duckling.Server;

public class NotFound extends Responder {
    public NotFound(Request request) {
        super(request);

        String responseBody =
            "<html><head><title>Not found</title></head>" +
                "<body>404 not found</body></head>" +
                Server.CRLF;

        response.bind((givenRequest) ->
            Response
                .wrap(givenRequest)
                .withBody(responseBody)
                .respondWith(ResponseCodes.NOT_FOUND)
        );

        response.bind(new HasOptions(allowedMethodsString()));
    }

    @Override
    public boolean isAllowed() {
        return true;
    }

    @Override
    public boolean matches() {
        return true;
    }

}
