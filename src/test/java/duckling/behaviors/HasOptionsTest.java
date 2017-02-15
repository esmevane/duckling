package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.CommonHeaders;
import duckling.responses.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HasOptionsTest {
    @Test
    public void configuresAllowedMethodsOnOptionsRequest() {
        String allowed = "GET,PUT,POST,DELETE";
        Behavior behavior = new HasOptions(allowed);
        Request request = new Request();

        request.add("OPTIONS / HTTP/1.1");

        Response subject = behavior.apply(request);

        assertThat(
            subject.getHeaders().getOrDefault(CommonHeaders.ALLOW, ""),
            is(allowed)
        );
    }

    @Test
    public void doesNotSetAllowedHeadersOnNonOptionsRequests() {
        String allowed = "GET,PUT,POST,DELETE";
        Behavior behavior = new HasOptions(allowed);
        Response subject = behavior.apply(new Request());

        assertThat(
            subject.getHeaders().getOrDefault(CommonHeaders.ALLOW, ""),
            is("")
        );
    }

    @Test
    public void setsADefaultContentTypeOnNormalRequests() {
        String allowed = "GET,PUT,POST,DELETE";
        Behavior behavior = new HasOptions(allowed);
        Response subject = behavior.apply(new Request());

        assertThat(
            subject.getHeaders().getOrDefault(CommonHeaders.CONTENT_TYPE, ""),
            is("text/html")
        );
    }
}
