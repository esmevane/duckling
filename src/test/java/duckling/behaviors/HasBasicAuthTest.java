package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.CommonHeaders;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Base64;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HasBasicAuthTest {
    @Test
    public void defaultsToAccessDeniedResponse() throws Exception {
        Behavior behavior = new HasBasicAuth("admin", "pass");
        Response subject = behavior.apply(new Request()) ;

        ArrayList<String> expectation =
            Response
                .wrap(new Request())
                .withResponseCode(ResponseCodes.ACCESS_DENIED)
                .withHeader(CommonHeaders.AUTHENTICATE, "Basic realm=\"duckling\"")
                .getResponseHeaders();

        assertThat(subject.getResponseHeaders(), is(expectation));
    }

    @Test
    public void permitsResponseOnGoodCredentials() throws Exception {
        Behavior behavior = new HasBasicAuth("admin", "pass");
        Request request = new Request();
        String credentials = new String(
            Base64.getEncoder().encode("admin:pass".getBytes())
        );

        request.add(
            "GET / HTTP/1.1",
            "Authorization: Basic " + credentials
        );

        Response subject = behavior.apply(request);

        ArrayList<String> expectation =
            Response
                .wrap(new Request())
                .withHeader(CommonHeaders.AUTHENTICATE, "Basic realm=\"duckling\"")
                .getResponseHeaders();

        assertThat(subject.getResponseHeaders(), is(expectation));
    }

    @Test
    public void deniesResponseOnBadCredentials() throws Exception {
        Behavior behavior = new HasBasicAuth("admin", "pass");
        Request request = new Request();

        request.add(
            "GET / HTTP/1.1",
            "Authorization: Basic arglebargle"
        );

        Response subject = behavior.apply(request);

        ArrayList<String> expectation =
            Response
                .wrap(new Request())
                .withResponseCode(ResponseCodes.ACCESS_DENIED)
                .withHeader(CommonHeaders.AUTHENTICATE, "Basic realm=\"duckling\"")
                .getResponseHeaders();

        assertThat(subject.getResponseHeaders(), is(expectation));
    }


}
