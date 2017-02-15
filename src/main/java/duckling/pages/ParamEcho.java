package duckling.pages;

import duckling.Server;
import duckling.requests.Request;

public class ParamEcho implements Page {

    @Override
    public String apply(Request request) {
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

        return builder.toString();
    }
}
