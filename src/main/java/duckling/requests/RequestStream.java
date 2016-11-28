package duckling.requests;

import duckling.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class RequestStream {
    private final BufferedReader buffer;
    private String rawRequest;

    public RequestStream(InputStream inputStream) throws IOException {
        this.buffer = new BufferedReader(
            new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        );
    }

    public ArrayList<String> toList() throws IOException {
        ArrayList<String> list = new ArrayList<>();

        if (rawRequest == null) marshalRawRequest();
        Collections.addAll(list, rawRequest.split(Server.CRLF));

        return list;
    }

    private void marshalRawRequest() throws IOException {
        StringBuilder builder = new StringBuilder();

        while (buffer.ready() || builder.length() == 0) {
            builder.append((char) buffer.read());
        }

        rawRequest = builder.toString();
    }

}
