package duckling.requests;

import duckling.Configuration;
import duckling.Server;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Request {
    protected boolean acceptingBody = false;

    protected Headers headers = new Headers();
    protected BaseRequest baseRequest = new BaseRequest();
    protected ArrayList<String> body = new ArrayList<>();

    private Configuration config;

    public Request() {
        this(new Configuration());
    }

    public Request(String root) {
        this(new Configuration(root));
    }

    public Request(Configuration config) {
        this.config = config;
    }

    public void add(String... lines) {
        add(Arrays.asList(lines));
    }

    public void add(List<String> lines) {
        lines.forEach(this::add);
    }

    public void add(String line) {
        if (line.length() == 0) this.acceptingBody = true;
        if (this.baseRequest.isEmpty() && !this.acceptingBody) {
            this.baseRequest = baseRequest.set(line);
        } else if (acceptingBody && line.length() != 0) {
            body.add(line);
        } else {
            this.headers.add(line);
        }
    }

    public File getFile() {
        return new File(this.config.root + this.baseRequest.getPath());
    }

    public String getMethod() {
        return this.baseRequest.getMethod();
    }

    public String getPath() {
        return this.baseRequest.getPath();
    }

    public boolean isOptions() {
        return this.baseRequest.isOptions();
    }

    public boolean isHead() {
        return this.baseRequest.isHead();
    }

    public int hashCode() {
        return this.baseRequest.hashCode() +
            this.body.hashCode() +
            this.headers.hashCode();
    }

    public boolean equals(Object other) {
        return equals((Request) other);
    }

    public boolean equals(Request other) {
        return this.baseRequest.equals(other.baseRequest)
            && this.body.equals(other.body)
            && this.headers.equals(other.headers);
    }

    public String toString() {
        return baseRequest.toString() + Server.CRLF +
            headers.toString() + Server.CRLF +
            body.toString();
    }

}
