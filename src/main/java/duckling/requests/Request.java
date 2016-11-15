package duckling.requests;

import duckling.Server;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Request {
    protected boolean acceptingBody = false;
    protected InitialRequest initialRequest;
    protected Hashtable<String, String> headers = new Hashtable<>();
    protected ArrayList<String> body = new ArrayList<>();
    private String root;

    public Request(String root) {
        this.root = root;
    }

    public Request() {
        this(".");
    }

    public String getBody() {
        return body.stream().collect(Collectors.joining(Server.CLRF));
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public boolean isAcceptingBody() {
        return acceptingBody;
    }

    public void setAcceptingBody() {
        this.acceptingBody = true;
    }

    public void add(String line) {
        if (line.length() == 0) setAcceptingBody();
        if (this.initialRequest == null && !acceptingBody) setInitialRequest(line);

        if (acceptingBody && line.length() != 0) {
            body.add(line);
        } else {
            setHeaderPair(line);
        }
    }

    private void setHeaderPair(String line) {
        String[] headerPair = line.split(": ");

        try {
            headers.put(headerPair[0], headerPair[1]);
        } catch (ArrayIndexOutOfBoundsException exception) {
        }
    }

    private void setInitialRequest(String initialRequest) {
        this.initialRequest = new InitialRequest(initialRequest);
    }

    public String initialRequestString() {
        return Stream.of(
            initialRequest.getMethod(),
            initialRequest.getPath(),
            initialRequest.getProtocol()
        ).collect(Collectors.joining(" "));
    }

    public String getMethod() {
        return initialRequest.getMethod();
    }

    public String getPath() {
        return initialRequest.getPath();
    }

    public String getProtocol() {
        return initialRequest.getProtocol();
    }

    public File getFile() {
        return new File(fullFilePath());
    }

    public String fullFilePath() {
        return root + getPath();
    }

    public boolean equals(Request other) {
        return initialRequest.equals(other.initialRequest)
                && body.equals(other.body)
                && headers.equals(other.headers);
    }

    public String getRoot() {
        return root;
    }

}
