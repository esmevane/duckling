package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.CommonHeaders;
import duckling.responses.Response;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SetCookieFromParamTest {
    @Test
    public void configuresSetCookieHeaderFromRequestParams() throws Exception {
        Request request = new Request();
        String key = "stuff";
        String value = "things";
        Behavior behavior = new SetCookieFromParam(key);

        request.add("GET /?" + key + "=" + value + " HTTP1.1");

        Response subject = behavior.apply(request);
        ArrayList<String> expectation =
            Response
                .wrap(request)
                .withHeader(CommonHeaders.SET_COOKIE, key + "=" + value)
                .getResponseHeaders();

        assertThat(subject.getResponseHeaders(), is(expectation));
    }
}
