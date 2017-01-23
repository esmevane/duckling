package duckling.requests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
    public void similarRequestsHaveSameHashCode() throws Exception {
        Request otherRequest = new Request();
        request.add("GET / HTTP/1.1");
        otherRequest.add("GET / HTTP/1.1");
        assertThat(request.hashCode(), is(otherRequest.hashCode()));
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

        assertThat(request, is(equalTo(otherRequest)));
    }

    @Test
    public void dissimilarRequestsAreNotEquivalent() throws Exception {
        Request otherRequest = new Request();
        request.add("GET / HTTP/1.1");
        otherRequest.add("GET / HTTP/1.0");
        assertEquals(false, request.equals(otherRequest));
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
    public void marshalsInitialRequestQuery() throws Exception {
        request.add("GET /?stuff=things HTTP/1.1");
        assertEquals(request.getQuery(), "stuff=things");
    }

    @Test
    public void providesPathFileReference() throws Exception {
        request.add("GET / HTTP/1.1");
        assertEquals(request.getFile(), new File("."));
    }

    @Test
    public void additionalLinesAreHeaders() throws Exception {
        request.add(
            "GET / HTTP/1.1",
            "Host: localhost"
        );
        assertEquals(request.headers.get("Host"), "localhost");
    }

    @Test
    public void isHeadReturnsFalseIfMethodIsNotHead() throws Exception {
        request.add("GET / HTTP/1.1");
        assertThat(request.isHead(), is(false));
    }

    @Test
    public void isHeadReturnsTrueIfMethodIsHead() throws Exception {
        request.add("HEAD / HTTP/1.1");
        assertThat(request.isHead(), is(true));
    }

    @Test
    public void isOptionsReturnsFalseIfMethodIsNotOptions() throws Exception {
        request.add("GET / HTTP/1.1");
        assertThat(request.isOptions(), is(false));
    }

    @Test
    public void isOptionsReturnsTrueIfMethodIsOptions() throws Exception {
        request.add("OPTIONS / HTTP/1.1");
        assertThat(request.isOptions(), is(true));
    }

}
