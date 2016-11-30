package duckling.responses;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import duckling.Server;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ResponseHeadersTest {
    @Test
    public void hasDefaultResponse() throws Exception {
        ResponseHeaders responseHeaders = new ResponseHeaders();
        ArrayList<String> list = new ArrayList<>(Arrays.asList(
            "HTTP/1.0 200 OK" + Server.CRLF,
            "Content-Type: null" + Server.CRLF,
            Server.CRLF
        ));

        assertThat(responseHeaders.toList(), is(list));
    }

    @Test
    public void allowsCustomContentType() throws Exception {
        ResponseHeaders responseHeaders = new ResponseHeaders().
            withContentType("text/html");

        ArrayList<String> list = new ArrayList<>(Arrays.asList(
            "HTTP/1.0 200 OK" + Server.CRLF,
            "Content-Type: text/html" + Server.CRLF,
            Server.CRLF
        ));

        assertThat(responseHeaders.toList(), is(list));
    }

    @Test
    public void supports404NotFound() throws Exception {
        ResponseHeaders responseHeaders = new ResponseHeaders().notFound();

        ArrayList<String> list = new ArrayList<>(Arrays.asList(
            "HTTP/1.0 404 NOT FOUND" + Server.CRLF,
            "Content-Type: null" + Server.CRLF,
            Server.CRLF
        ));

        assertThat(responseHeaders.toList(), is(list));
    }

    @Test
    public void supports405MethodNotAllowed() throws Exception {
        ResponseHeaders responseHeaders = new ResponseHeaders().notAllowed();

        ArrayList<String> list = new ArrayList<>(Arrays.asList(
            "HTTP/1.0 405 METHOD NOT ALLOWED" + Server.CRLF,
            "Content-Type: null" + Server.CRLF,
            Server.CRLF
        ));

        assertThat(responseHeaders.toList(), is(list));
    }

    @Test
    public void supportsMethodChaining() throws Exception {
        ResponseHeaders responseHeaders = new ResponseHeaders().
            notFound().withContentType("text/html");

        ArrayList<String> list = new ArrayList<>(Arrays.asList(
            "HTTP/1.0 404 NOT FOUND" + Server.CRLF,
            "Content-Type: text/html" + Server.CRLF,
            Server.CRLF
        ));

        assertThat(responseHeaders.toList(), is(list));
    }

    @Test
    public void hasOptionsMode() throws Exception {
        ArrayList<String> methods = new ArrayList<>(
            Arrays.asList("GET", "HEAD", "OPTIONS")
        );

        String allowed = methods.stream().collect(Collectors.joining(","));

        ArrayList<String> responseList = new ArrayList<>(Arrays.asList(
            "HTTP/1.0 200 OK" + Server.CRLF,
            "Allow: " + allowed + Server.CRLF,
            Server.CRLF
        ));

        ResponseHeaders responseHeaders = new ResponseHeaders().allowedMethods(methods);

        assertThat(responseHeaders.toList(), is(responseList));
    }

    @Test
    public void asListCreatesList() throws Exception {
        ResponseHeaders responseHeaders = new ResponseHeaders().
            notFound().withContentType("text/html");

        ArrayList<String> list = new ArrayList<>(Arrays.asList(
            "HTTP/1.0 404 NOT FOUND" + Server.CRLF,
            "Content-Type: text/html" + Server.CRLF,
            Server.CRLF
        ));

        assertThat(responseHeaders.toList(), is(list));
    }
}
