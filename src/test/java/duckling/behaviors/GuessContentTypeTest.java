package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.CommonHeaders;
import duckling.responses.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GuessContentTypeTest {
    @Test
    public void returnsGivenBodyIfTrue() throws Exception {
        Request request = new Request();
        Behavior behavior = new GuessContentType();

        request.add("GET /file.txt HTTP/1.1");

        Response subject = behavior.apply(request);

        assertThat(
            subject.getHeaders().getOrDefault(CommonHeaders.CONTENT_TYPE, ""),
            is("text/plain")
        );
    }
}
