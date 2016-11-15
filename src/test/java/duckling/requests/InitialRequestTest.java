package duckling.requests;

import duckling.requests.InitialRequest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InitialRequestTest {
    private String rawInitialRequest;
    private InitialRequest initialRequest;

    @Before
    public void setup() throws Exception {
        rawInitialRequest = "GET / HTTP/1.1";
        initialRequest = new InitialRequest(rawInitialRequest);
    }

    @Test
    public void setsThePath() throws Exception {
        assertEquals("/", initialRequest.getPath());
    }

    @Test
    public void setsTheProtocol() throws Exception {
        assertEquals("HTTP/1.1", initialRequest.getProtocol());
    }

    @Test
    public void setsTheMethod() throws Exception {
        assertEquals("GET", initialRequest.getMethod());
    }

    @Test
    public void matchesSimilarInitialRequests() throws Exception {
        InitialRequest otherRequest = new InitialRequest(rawInitialRequest);
        assertEquals(true, initialRequest.equals(otherRequest));
    }

    @Test
    public void doesNotMatchDissimilarInitialRequests() throws Exception {
        InitialRequest otherRequest = new InitialRequest("POST / HTTP/1.1");
        assertEquals(false, initialRequest.equals(otherRequest));
    }

}
