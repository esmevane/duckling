package duckling.requests;

import duckling.Server;

import java.util.HashMap;
import java.util.function.Function;

public class ParamConverter implements Function<Request, String> {

    @Override
    public String apply(Request request) {
        StringBuilder builder = new StringBuilder();
        ParamExtractor extractor = new ParamExtractor();
        HashMap<String, String> params = extractor.apply(request);

        params.forEach((key, value) -> builder.append(key + " = " + value + Server.CRLF));

        return builder.toString();
    }
}
