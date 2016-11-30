package duckling.requests;

import duckling.Server;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Request {
    protected boolean acceptingBody = false;
    protected Hashtable<String, String> request = new Hashtable<>();
    protected Hashtable<String, String> headers = new Hashtable<>();
    protected ArrayList<String> body = new ArrayList<>();
    private String root;

    public Request(String root) {
        this.root = root;
    }

    public Request() {
        this(".");
    }

    public void add(String line) {
        if (line.length() == 0) setAcceptingBody();
        if (this.request.isEmpty() && !acceptingBody) setRequestPairs(line);

        if (acceptingBody && line.length() != 0) {
            body.add(line);
        } else {
            setHeaderPair(line);
        }
    }

    public int hashCode() {
        return request.hashCode() + body.hashCode() + headers.hashCode();
    }

    public boolean equals(Request other) {
        return request.equals(other.request)
            && body.equals(other.body)
            && headers.equals(other.headers);
    }

    public String fullFilePath() {
        return root + getPath();
    }

    public String getBody() {
        return body.stream().collect(Collectors.joining(Server.CRLF));
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public File getFile() {
        return new File(root + getPath());
    }

    public String getMethod() {
        String method = request.get("Method");

        return method == null ? "GET" : method;
    }

    public String getPath() {
        String path = request.get("Path");

        return path == null ? "" : path;
    }

    public String getProtocol() {
        String protocol = request.get("Protocol");

        return protocol == null ? "HTTP/1.0" : protocol;
    }

    public String getRoot() {
        return root;
    }

    public String initialRequestString() {
        Stream<String> lines = Stream.of(getMethod(), getPath(), getProtocol());

        return lines.collect(Collectors.joining(" "));
    }

    public boolean isAcceptingBody() {
        return acceptingBody;
    }

    public void setAcceptingBody() {
        this.acceptingBody = true;
    }

    private void setHeaderPair(String line) {
        String[] headerPair = line.split(": ");

        try {
            headers.put(headerPair[0], headerPair[1]);
        } catch (ArrayIndexOutOfBoundsException exception) {
        }
    }

    private void setRequestPairs(String initialRequest) {
        String[] tokens = initialRequest.split(" ");

        try {
            request.put("Method", tokens[0]);
            request.put("Path", tokens[1]);
            request.put("Protocol", tokens[2]);
        } catch (ArrayIndexOutOfBoundsException exception) {
        }
    }

    public boolean isOptions() {
        return getMethod().equals("OPTIONS");
    }
}
