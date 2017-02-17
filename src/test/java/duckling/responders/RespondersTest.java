package duckling.responders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import duckling.Server;
import duckling.requests.RequestStream;
import duckling.requests.Request;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class RespondersTest {
    private Request request;

    @Before
    public void setup() throws Exception {
        String requestContent = "GET / HTTP/1.1" + Server.CRLF + Server.CRLF;
        byte[] getRequest = requestContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(getRequest);

        RequestStream requestStream = new RequestStream(inputStream);
        Request request = new Request();

        requestStream.toList().forEach(request::add);

        this.request = request;
    }

    @Test
    public void getResponderReturnsFirstMatch() throws IOException {
        Responders responders = new Responders(
            this.request,
            new NoMatchResponder(this.request),
            new MatchResponder(this.request)
        );

        assertThat(responders.getResponder(), instanceOf(MatchResponder.class));
    }

    private class MatchResponder extends Responder {
        MatchResponder(Request request) { super(request); }

        @Override
        public boolean matches() { return true; }

        @Override
        public ArrayList<String> headers() { return null; }

        @Override
        public InputStream body() { return null; }

        @Override
        public boolean isAllowed() { return true; }
    }

    private class NoMatchResponder extends Responder {
        NoMatchResponder(Request request) { super(request); }

        @Override
        public boolean matches() { return false; }

        @Override
        public ArrayList<String> headers() { return null; }

        @Override
        public InputStream body() { return null; }

        @Override
        public boolean isAllowed() { return true; }
    }
}
