package duckling.requests;

import duckling.Logger;
import duckling.support.SpyLogger;
import duckling.support.SpySocket;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class RequestHandlerTest {
    private SpySocket client = new SpySocket();
    private String root = "/path/to/root";
    private RequestHandler handler = new RequestHandler(client, root);

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

        RequestHandler handler = new RequestHandler(client, root) {
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

        RequestHandler handler = new RequestHandler(client, root) {
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