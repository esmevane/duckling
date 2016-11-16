package duckling;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ResponseHeaderTest {
    @Test
    public void hasDefaultResponse() throws Exception {
        ResponseHeaders responseHeaders = new ResponseHeaders();
        String[] expectation = {
            "HTTP/1.0 200 OK" + Server.CRLF,
            "Content-Type: null" + Server.CRLF,
            Server.CRLF
        };
        assertEquals(expectation, responseHeaders.toArray());
    }

    @Test
    public void allowsCustomContentType() throws Exception {
        ResponseHeaders responseHeaders = new ResponseHeaders().
                withContentType("text/html");

        String[] expectation = {
                "HTTP/1.0 200 OK" + Server.CRLF,
                "Content-Type: text/html" + Server.CRLF,
                Server.CRLF
        };

        assertEquals(expectation, responseHeaders.toArray());
    }

    @Test
    public void supports400NotFound() throws Exception {
        ResponseHeaders responseHeaders = new ResponseHeaders().notFound();

        String[] expectation = {
                "HTTP/1.0 404 NOT FOUND" + Server.CRLF,
                "Content-Type: null" + Server.CRLF,
                Server.CRLF
        };

        assertEquals(expectation, responseHeaders.toArray());
    }

    @Test
    public void supportsMethodChaining() throws Exception {
        ResponseHeaders responseHeaders = new ResponseHeaders().
                notFound().withContentType("text/html");

        String[] expectation = {
                "HTTP/1.0 404 NOT FOUND" + Server.CRLF,
                "Content-Type: text/html" + Server.CRLF,
                Server.CRLF
        };

        assertEquals(expectation, responseHeaders.toArray());
    }
}
