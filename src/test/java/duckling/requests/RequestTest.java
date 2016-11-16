package duckling.requests;

import static org.junit.Assert.assertEquals;

import duckling.Server;
import org.junit.Assert;
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
    public void similarTokensAreEquivalent() throws Exception {
        Request otherTokens = new Request();
        request.add("GET / HTTP/1.1");
        otherTokens.add("GET / HTTP/1.1");
        assertEquals(true, request.equals(otherTokens));
    }

    @Test
    public void dissimilarTokensAreNotEquivalent() throws Exception {
        Request otherTokens = new Request();
        request.add("GET / HTTP/1.1");
        otherTokens.add("GET / HTTP/1.0");
        assertEquals(false, request.equals(otherTokens));
    }


    @Test
    public void firstLineIsInitialRequest() throws Exception {
        request.add("GET / HTTP/1.1");
        assertEquals(request.initialRequestString(), "GET / HTTP/1.1");
    }

    @Test
    public void tokenizesInitialRequestMethod() throws Exception {
        request.add("GET / HTTP/1.1");
        assertEquals(request.getMethod(), "GET");
    }

    @Test
    public void tokenizesInitialRequestPath() throws Exception {
        request.add("GET / HTTP/1.1");
        assertEquals(request.getPath(), "/");
    }

    @Test
    public void providesPathFileReference() throws Exception {
        request.add("GET / HTTP/1.1");
        assertEquals(request.getFile(), new File("./"));
    }

    @Test
    public void tokenizesInitialRequestProtocol() throws Exception {
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
        Assert.assertEquals(
            request.getBody(),
            "Body line one" + Server.CRLF + "Body line two"
        );
    }
}
