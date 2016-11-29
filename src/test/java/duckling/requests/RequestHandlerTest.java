package duckling.requests;

import duckling.Configuration;
import duckling.Logger;
import duckling.support.SpyLogger;
import duckling.support.SpySocket;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class RequestHandlerTest {
    private SpySocket client;
    private String root;
    private Configuration config;
    private RequestHandler handler;

    @Before
    public void setup() throws Exception {
        client = new SpySocket();
        root = "/path/to/root";
        config = new Configuration(5151, root);
        handler = new RequestHandler(client, config);
    }

    @Test
    public void buildRequestUsesGivenRoot() throws Exception {
        assertThat(handler.prepareRequest().getRoot(), is(root));
    }

    @Test
    public void buildRequestStreamUsesClientInput() throws Exception {
        handler.buildRequestStream();
        assertThat(client.wasInputStreamCalled(), is(true));
    }

    @Test
    public void runOutputsLogDetail() throws Exception {
        SpyLogger spyLogger = new SpyLogger();
        String message = "GET / HTTP/1.1";

        RequestHandler handler = new RequestHandler(client, config) {
            @Override
            public Logger getLogger() {
                return spyLogger;
            }

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

        assertThat(spyLogger.messages, hasItem(message));
    }

    @Test
    public void runClosesClient() throws Exception {
        // TODO: null characters printed during test run
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

        System.out.println("" + request.getPath());
        RequestHandler handler = new RequestHandler(client, config) {
            @Override
            public Request prepareRequest() {
                return request;
            }

            @Override
            public Logger getLogger() {
                return new SpyLogger();
            }

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
}
