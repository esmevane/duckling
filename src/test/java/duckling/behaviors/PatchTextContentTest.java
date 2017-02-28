package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PatchTextContentTest {
    @Test
    public void returnsEmptyResponseUnlessFileExists() throws Exception {
        Behavior behavior = new PatchTextContent();
        Request request = new Request() {
            @Override
            public boolean fileExists() { return false; }
        };

        assertThat(behavior.apply(request), is(Response.wrap(request)));
    }

    @Test
    public void returnsEmptyResponseWhenIfMatchIsAbsent() throws Exception {
        Behavior behavior = new PatchTextContent();
        Request request = new Request();

        assertThat(behavior.apply(request), is(Response.wrap(request)));
    }

    @Test
    public void returnEmptyResponseWhenMaybeWriteIsFalse() throws Exception {
        Behavior behavior = new PatchTextContent();
        Request request = new Request() {
            @Override
            public boolean maybeWrite(String string, String content) { return false; }
        };

        assertThat(behavior.apply(request), is(Response.wrap(request)));
    }

    @Test
    public void returnsNoContentOtherwise() throws Exception {
        Behavior behavior = new PatchTextContent();
        Request request = new Request() {
            @Override
            public boolean fileExists() { return true; }

            @Override
            public boolean maybeWrite(String string, String content) { return true; }
        };

        request.add("GET / HTTP/1.1");
        request.add("If-Match: abcdefgH");

        Response expectation =
            Response
                .wrap(request)
                .withResponseCode(ResponseCodes.NO_CONTENT);

        assertThat(behavior.apply(request), is(expectation));
    }

}
