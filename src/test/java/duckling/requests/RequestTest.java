package duckling.requests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import duckling.Server;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class RequestTest {
    private Request request;

    @Before
    public void setup() throws Exception {
        request = new Request();
    }

    @Test
    public void withRoot() throws Exception {
        String root = "/some/directory/path";
        Request request = new Request(root);
        assertEquals(root, request.getRoot());
    }

    @Test
    public void withoutRoot() throws Exception {
        Request request = new Request();
        assertEquals(".", request.getRoot());
    }

    @Test
    public void buildsFilePath() throws Exception {
        String root = "/some/directory/path";
        Request request = new Request(root);
        request.add("GET /crazy.html HTTP/1.1");
        assertEquals(root + "/crazy.html", request.fullFilePath());

    }

    @Test
    public void similarRequestsHaveSameHashCode() throws Exception {
        Request otherRequest = new Request();
        request.add("GET / HTTP/1.1");
        otherRequest.add("GET / HTTP/1.1");
        assertEquals(request.hashCode(), otherRequest.hashCode());
    }

    @Test
    public void dissimilarRequestsHaveDifferentHashCodes() throws Exception {
        Request otherRequest = new Request();
        request.add("GET / HTTP/1.1");
        otherRequest.add("GET / HTTP/1.0");
        assertNotEquals(request.hashCode(), otherRequest.hashCode());
    }

    @Test
    public void similarRequestsAreEquivalent() throws Exception {
        Request otherRequest = new Request();
        request.add("GET / HTTP/1.1");
        otherRequest.add("GET / HTTP/1.1");
        assertEquals(true, request.equals(otherRequest));
    }

    @Test
    public void dissimilarRequestsAreNotEquivalent() throws Exception {
        Request otherRequest = new Request();
        request.add("GET / HTTP/1.1");
        otherRequest.add("GET / HTTP/1.0");
        assertEquals(false, request.equals(otherRequest));
    }

    @Test
    public void firstLineIsInitialRequest() throws Exception {
        request.add("GET / HTTP/1.1");
        assertEquals(request.initialRequestString(), "GET / HTTP/1.1");
    }

    @Test
    public void RequestizesInitialRequestMethod() throws Exception {
        request.add("GET / HTTP/1.1");
        assertEquals(request.getMethod(), "GET");
    }

    @Test
    public void marshalsInitialRequestPath() throws Exception {
        request.add("GET / HTTP/1.1");
        assertEquals(request.getPath(), "/");
    }

    @Test
    public void providesPathFileReference() throws Exception {
        request.add("GET / HTTP/1.1");
        assertEquals(request.getFile(), new File("."));
    }

    @Test
    public void marshalsInitialRequestProtocol() throws Exception {
        request.add("GET / HTTP/1.1");
        assertEquals(request.getProtocol(), "HTTP/1.1");
    }

    @Test
    public void additionalLinesAreHeaders() throws Exception {
        request.add("GET / HTTP/1.1");
        request.add("Host: localhost");
        assertEquals(request.getHeader("Host"), "localhost");
    }

    @Test
    public void setAcceptingBody() throws Exception {
        request.setAcceptingBody();
        assertEquals(true, request.isAcceptingBody());
    }

    @Test
    public void bodyFollowsAnEmptyLine() throws Exception {
        request.add("GET / HTTP/1.1");
        request.add("");
        request.add("Body line one");
        request.add("Body line two");

        assertEquals(
            request.getBody(),
            "Body line one" + Server.CRLF + "Body line two"
        );
    }
}
