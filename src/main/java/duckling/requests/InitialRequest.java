package duckling.requests;

public class InitialRequest {
    private String method, path, protocol;

    public InitialRequest(String initialRequest) {
        String[] tokens = initialRequest.split(" ");

        try {
            method = tokens[0];
            path = tokens[1];
            protocol = tokens[2];
        } catch (ArrayIndexOutOfBoundsException exception) {
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public boolean equals(InitialRequest other) {
        return method.equals(other.method)
                && path.equals(other.path)
                && protocol.equals(other.protocol);
    }
}
