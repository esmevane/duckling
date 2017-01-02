package duckling.requests;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class BaseRequestTest {

    @Test
    public void storesGivenMethod() throws Exception {
        BaseRequest baseRequest = new BaseRequest("GET / HTTP/1.1");
        assertThat(baseRequest.getMethod(), is("GET"));
    }

    @Test
    public void storesGivenPath() throws Exception {
        BaseRequest baseRequest = new BaseRequest("GET / HTTP/1.1");
        assertThat(baseRequest.getPath(), is("/"));
    }

    @Test
    public void isEqualToSameRequest() throws Exception {
        BaseRequest baseRequest = new BaseRequest("GET / HTTP/1.1");
        BaseRequest other = new BaseRequest("GET / HTTP/1.1");
        assertThat(baseRequest, is(other));
    }

    @Test
    public void isNotEqualToDifferentRequest() throws Exception {
        BaseRequest baseRequest = new BaseRequest("GET / HTTP/1.1");
        BaseRequest other = new BaseRequest("GET /beans HTTP/1.1");
        assertThat(baseRequest, is(not(other)));
    }

    @Test
    public void canBeSetToNewRequestImmutably() throws Exception {
        BaseRequest baseRequest = new BaseRequest("GET / HTTP/1.1");
        BaseRequest other = baseRequest.set("POST / HTTP/1.1");
        assertThat(baseRequest, is(not(other)));

    }

    @Test
    public void isEmptyWhenGivenEmptyRequest() throws Exception {
        BaseRequest baseRequest = new BaseRequest("");
        assertThat(baseRequest.isEmpty(), is(true));
    }

    @Test
    public void isOptionsWhenGivenOptionsRequest() throws Exception {
        BaseRequest baseRequest = new BaseRequest("OPTIONS / HTTP/1.1");
        assertThat(baseRequest.isOptions(), is(true));
    }

    @Test
    public void isHeadWhenGivenHeadRequest() throws Exception {
        BaseRequest baseRequest = new BaseRequest("HEAD / HTTP/1.1");
        assertThat(baseRequest.isHead(), is(true));
    }

    @Test
    public void rendersGivenRequestWhenTurnedToString() throws Exception {
        String requestLine = "GET / HTTP/1.1";
        BaseRequest baseRequest = new BaseRequest(requestLine);
        assertThat(baseRequest.toString(), is(requestLine));
    }

}
