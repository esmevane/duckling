package duckling.requests;

import duckling.Configuration;
import duckling.Server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static sun.security.pkcs11.wrapper.Functions.toHexString;

public class Request {
    public Headers headers = new Headers();

    private boolean acceptingBody = false;
    public BaseRequest baseRequest = new BaseRequest();
    private ArrayList<String> body = new ArrayList<>();

    private Configuration config;

    public Request() {
        this(new Configuration());
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
        return new File(getFilePath());
    }

    public boolean fileExists() {
        return Files.exists(Paths.get(getFilePath()));
    }

    public String getFileHexString() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(readFile());
        return toHexString(digest.digest());
    }

    public void writeFile(byte[] content) throws IOException {
        Path path = Paths.get(getFilePath());
        Files.write(path, content);
    }

    public byte[] readFile() {
        Path path = Paths.get(getFilePath());
        try {
            return Files.readAllBytes(path);
        } catch (IOException exception) {
            return "".getBytes();
        }
    }

    public String getFilePath() {
        return this.config.root + this.baseRequest.getPath();
    }

    public String getMethod() {
        return this.baseRequest.getMethod();
    }

    public String getPath() {
        return this.baseRequest.getPath();
    }

    public String getQuery() {
        return this.baseRequest.getQuery();
    }

    public HashMap<String, String> getParams() {
        return new QueryParams().apply(this);
    }

    public ArrayList<String> getBody() {
        return this.body;
    }

    public boolean isOptions() {
        return this.baseRequest.isOptions();
    }

    public boolean isHead() {
        return this.baseRequest.isHead();
    }

    @Override
    public int hashCode() {
        return this.baseRequest.hashCode() +
            this.body.hashCode() +
            this.headers.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Request) {
            Request other = (Request) object;

            return this.baseRequest.equals(other.baseRequest)
                && this.body.equals(other.body)
                && this.headers.equals(other.headers);
        }

        return false;
    }

    @Override
    public String toString() {
        return baseRequest.toString() + Server.CRLF +
            headers.toString() + Server.CRLF +
            body.toString();
    }

    public boolean maybeWrite(String ifMatch, String update) {
        try {
            if (ifMatch.equals(getFileHexString())) {
                writeFile(update.getBytes());
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            return false;
        }
    }
}
