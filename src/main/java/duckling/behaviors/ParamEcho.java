package duckling.behaviors;

import duckling.Server;
import duckling.requests.Request;
import duckling.responses.Response;

public class ParamEcho implements Behavior {

    @Override
    public Response apply(Request request) {
        Response response = Response.wrap(request);
        StringBuilder builder = new StringBuilder();

        request
            .getParams()
            .forEach(
                (key, value) ->
                    builder
                        .append(key)
                        .append(" = ")
                        .append(value)
                        .append(Server.CRLF)
            );

        return response.withBody(builder.toString());
    }
}
