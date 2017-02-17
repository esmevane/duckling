package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.CommonHeaders;
import duckling.responses.Response;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SetCookieTest {
    @Test
    public void addsSetCookieHeaderToResponse() throws Exception {
        String key = "stuff";
        String value = "things";
        Response subject = new SetCookie(key, value).apply(new Request());
        ArrayList<String> expectation =
            Response
                .wrap(new Request())
                .withHeader(CommonHeaders.SET_COOKIE, key + "=" + value)
                .getResponseHeaders();

        assertThat(subject.getResponseHeaders(), is(expectation));
    }
}
