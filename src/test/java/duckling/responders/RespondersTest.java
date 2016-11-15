package duckling.responders;

import static org.junit.Assert.assertEquals;

import duckling.requests.Request;
import duckling.requests.RequestBuilder;
import duckling.responders.Responder;
import duckling.responders.Responders;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RespondersTest {
    private OutputStream outputStream;
    private Request request;

    @Before
    public void setup() throws Exception {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };

        this.outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException { }
        };

        this.request = new RequestBuilder(inputStream).build();
    }

    @Test
    public void getResponderReturnsFirstMatch() throws IOException {
        Responders responders = new Responders(
            this.request,
            this.outputStream,
            new NoMatchResponder(this.request, this.outputStream),
            new MatchResponder(this.request, this.outputStream)
        );

        assertEquals(
            MatchResponder.class,
            responders.getResponder().getClass()
        );
    }

    private class MatchResponder extends Responder {
        MatchResponder(Request request, OutputStream outputStream) {
            super(request, outputStream);
        }

        @Override
        public boolean matches() { return true; }

        @Override
        public void respond() throws IOException { }
    }

    private class NoMatchResponder extends Responder {
        NoMatchResponder(Request request, OutputStream outputStream) {
            super(request, outputStream);
        }

        @Override
        public boolean matches() { return false; }

        @Override
        public void respond() throws IOException { }
    }
}
