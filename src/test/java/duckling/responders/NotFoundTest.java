package duckling.responders;

import duckling.responses.ResponseHeaders;
import duckling.Server;
import duckling.requests.Request;
import duckling.support.SpyOutputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NotFoundTest {
    private NotFound responder;

    @Before
    public void setup() throws Exception {
        responder = new NotFound(new Request());
    }

    @Test
    public void alwaysMatches() throws Exception {
        assertThat(responder.matches(), is(true));
    }

    @Test
    public void providesHeadersWithHtmlContentType() throws Exception {
        ArrayList<String> headers =
            new ResponseHeaders().notFound().withContentType("text/html").toList();

        assertThat(responder.headers(), is(headers));
    }

    @Test
    public void providesStreamOfEmptyPageAsBody() throws Exception {
        SpyOutputStream outputStream = new SpyOutputStream();
        InputStream inputStream = responder.body();
        String expectation =
            "<html><head><title>Not found</title></head>" +
                "<body>404 not found</body></head>" +
                Server.CRLF;

        int input;

        while ((input = inputStream.read()) != -1) {
            outputStream.write(input);
        }

        assertThat(outputStream.getWrittenOutput(), is(expectation));
    }
}
