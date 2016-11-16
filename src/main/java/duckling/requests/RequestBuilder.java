package duckling.requests;

import duckling.Server;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

public class RequestBuilder {
    private String root;
    private String[] rawRequest;

    public RequestBuilder(InputStream inputStream) throws IOException {
        this(".", inputStream);
    }

    public RequestBuilder(String root, InputStream inputStream) throws IOException {
        int input;
        this.root = root;
        StringBuilder requestBody = new StringBuilder();

        while ((input = inputStream.read()) != -1 && inputStream.available() > 0) {
            requestBody.append((char) input);
        }

        rawRequest = requestBody.toString().split(Server.CRLF);
    }

    public void printRawRequest() {
        for (String line: rawRequest) System.out.println(line);
    }

    public Request build() {
        Request request = new Request(root);

        Stream.of(rawRequest).forEach(request::add);

        return request;
    }
}
