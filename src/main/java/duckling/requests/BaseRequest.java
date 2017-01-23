package duckling.requests;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaseRequest {
    public static final String METHOD = "Method";
    public static final String PATH = "Path";
    public static final String PROTOCOL = "Protocol";
    public static final String QUERY = "Query";
    public static final String EMPTY_PATH = "";
    public static final String OPTIONS = "OPTIONS";
    public static final String HEAD = "HEAD";
    public static final String GET = "GET";

    public static final int METHOD_INDEX = 0;
    public static final int PATH_INDEX = 1;
    public static final int PROTOCOL_INDEX = 2;
    private static final String DEFAULT_PROTOCOL = "HTTP/1.0";

    private Hashtable<String,String> contents = new Hashtable<>();
    private String rawRequest = "";

    public BaseRequest() {
        this("");
    }

    public BaseRequest(String baseRequest) {
        this.rawRequest = baseRequest;

        List<String> tokens = Arrays.asList(baseRequest.split(" "));

        Function<String,String> parsePath = (path) -> {
            try {
                return path.split("\\?")[0];
            } catch (ArrayIndexOutOfBoundsException exception) {
                return "";
            }
        };

        Function<String,String> parseQuery = (path) -> {
            try {
                return path.split("\\?")[1];
            } catch (ArrayIndexOutOfBoundsException exception) {
                return "";
            }
        };

        try {
            contents.put(METHOD, tokens.get(METHOD_INDEX));
            contents.put(PATH, parsePath.apply(tokens.get(PATH_INDEX)));
            contents.put(PROTOCOL, tokens.get(PROTOCOL_INDEX));
            contents.put(QUERY, parseQuery.apply(tokens.get(PATH_INDEX)));
        } catch (ArrayIndexOutOfBoundsException exception) {
        }
    }

    public BaseRequest set(String baseRequest) {
        return new BaseRequest(baseRequest);
    }

    public String getMethod() {
        return this.contents.getOrDefault(METHOD, GET);
    }

    public String getPath() {
        return this.contents.getOrDefault(PATH, EMPTY_PATH);
    }

    public String getQuery() {
        return this.contents.getOrDefault(QUERY, EMPTY_PATH);
    }


    private String getProtocol() {
        return this.contents.getOrDefault(PROTOCOL, DEFAULT_PROTOCOL);
    }

    public boolean isEmpty() {
        return this.rawRequest.isEmpty();
    }

    public boolean isOptions() {
        return getMethod().equals(OPTIONS);
    }

    public boolean isHead() {
        return getMethod().equals(HEAD);
    }

    @Override
    public int hashCode() {
        return this.rawRequest.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof BaseRequest) {
            BaseRequest other = (BaseRequest) object;

            return this.rawRequest.equals(other.rawRequest);
        }

        return false;
    }

    @Override
    public String toString() {
        return Stream.of(getMethod(), getPath(), getProtocol()).
            collect(Collectors.joining(" "));
    }

}
