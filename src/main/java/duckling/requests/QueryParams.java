package duckling.requests;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Stream;

public class QueryParams implements Function<Request, HashMap<String, String>> {
    @Override
    public HashMap<String, String> apply(Request request) {
        String query = request.getQuery();
        String[] rawParams = query.split("&");
        Stream<String[]> parsedParams = Arrays.asList(rawParams).stream().map((String s) -> s.split("=", 2));

        HashMap<String, String> params = new HashMap<>();

        parsedParams.forEach((String[] paramPair) -> {
            try {
                params.put(paramPair[0], URLDecoder.decode(paramPair[1], "utf-8"));
            } catch (UnsupportedEncodingException exception) {
                exception.printStackTrace();
            }
        });

        return params;
    }
}