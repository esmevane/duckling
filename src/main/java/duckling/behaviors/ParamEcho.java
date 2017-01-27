package duckling.behaviors;

import duckling.Server;
import duckling.requests.Request;

public class ParamEcho implements Behavior {

    @Override
    public String apply(Request request) {
        StringBuilder builder = new StringBuilder();

        request
            .getParams()
            .forEach((key, value) -> builder.append(key + " = " + value + Server.CRLF));

        return builder.toString();
    }
}
