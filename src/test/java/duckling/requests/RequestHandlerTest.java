package duckling.requests;

import duckling.Configuration;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;
import duckling.support.SpyOutputStream;
import duckling.support.SpySocket;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class RequestHandlerTest {
    private SpySocket client;
    private Configuration config;
    private RequestHandler handler;

    @Before
    public void setup() throws Exception {
        client = new SpySocket();
        String root = "/path/to/root";
        config = new Configuration(5151, root);
        handler = new RequestHandler(client, config);
    }

    @Test
    public void buildRequestStreamUsesClientInput() throws Exception {
        handler.buildRequestStream();
        assertThat(client.wasInputStreamCalled(), is(true));
    }

    @Test
    public void runClosesClient() throws Exception {
        handler.run();
        assertThat(client.wasClosed(), is(true));
    }

    @Test
    public void runAddsRequestDetail() throws Exception {
        String message = "GET / HTTP/1.1";
        ArrayList<String> details = new ArrayList<>();
        Request request = new Request() {
            @Override
            public void add(String detail) {
                details.add(detail);
            }
        };

        RequestHandler handler = new RequestHandler(client, config, request) {
            @Override
            public RequestStream buildRequestStream() throws IOException {
                return new RequestStream(client.getInputStream()) {
                    @Override
                    public ArrayList<String> toList() {
                        return new ArrayList<>(Collections.singletonList(message));
                    }
                };
            }
        };

        handler.run();

        assertThat(details, hasItem(message));
    }

    @Test
    public void respondsToHeadersRequestsWithEmptyBody() throws Exception {
        ArrayList<String> list = new ArrayList<>();
        SpyOutputStream output = new SpyOutputStream();

        list.add("HEAD / HTTP/1.1");

        client = new SpySocket() {
            @Override
            public OutputStream getOutputStream() throws IOException {
                super.getOutputStream();
                return output;
            }
        };

        RequestHandler handler = new RequestHandler(client, config) {
            @Override
            public RequestStream buildRequestStream() throws IOException {
                return new RequestStream(client.getInputStream()) {
                    @Override
                    public ArrayList<String> toList() {
                        return list;
                    }
                };
            }
        };

        String response =
            Response
                .wrap(new Request())
                .withResponseCode(ResponseCodes.NOT_FOUND)
                .withContentType("text/html")
                .getResponseHeaders()
                .stream()
                .collect(Collectors.joining());

        handler.run();

        assertThat(
            output.getWrittenOutput(),
            is(response)
        );
    }
}
