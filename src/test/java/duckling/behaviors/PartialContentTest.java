package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;
import duckling.support.SpyOutputStream;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PartialContentTest {
    @Test
    public void returnsBasicResponseWithoutRangeHeaders() {
        Response response = Response.wrap(new Request());
        Behavior behavior = new PartialContent();

        assertThat(behavior.apply(new Request()), is(response));
    }

    @Test
    public void returnsPartialContentHeader() {
        Request request = new Request();
        Behavior behavior = new PartialContent();

        request.add("GET / HTTP/1.1");
        request.add("Range: byte=");

        ArrayList<String> expectation =
            Response
                .wrap(new Request())
                .withResponseCode(ResponseCodes.PARTIAL_CONTENT)
                .getResponseHeaders();

        assertThat(
            behavior.apply(request).compose().getResponseHeaders(),
            is(expectation)
        );
    }

    @Test
    public void abortsPartialContentAttemptWithMalformedRange() throws Exception {
        Request request = new Request();
        Behavior behavior = new PartialContent();

        request.add("GET / HTTP/1.1");
        request.add("Range: byte=2");

        Response response =
            Response
                .wrap(request)
                .withBody("Hello!")
                .bind(behavior);

        int input;
        SpyOutputStream outputStream = new SpyOutputStream();
        InputStream inputStream = response.compose().getBody();

        while ((input = inputStream.read()) != -1) outputStream.write(input);

        assertThat(outputStream.getWrittenOutput(), is("Hello!"));
    }

    @Test
    public void returnsRemainderWhenGivenStartOnly() throws Exception {
        Request request = new Request();
        Behavior behavior = new PartialContent();

        request.add("GET / HTTP/1.1");
        request.add("Range: byte=2-");

        Response response =
            Response
                .wrap(request)
                .withBody("Hello!")
                .bind(behavior);

        int input;
        SpyOutputStream outputStream = new SpyOutputStream();
        InputStream inputStream = response.compose().getBody();

        while ((input = inputStream.read()) != -1) outputStream.write(input);

        assertThat(outputStream.getWrittenOutput(), is("llo!"));
    }

    @Test
    public void returnsSliceWhenGivenBothParameters() throws Exception {
        Request request = new Request();
        Behavior behavior = new PartialContent();

        request.add("GET / HTTP/1.1");
        request.add("Range: byte=2-4");

        Response response =
            Response
                .wrap(request)
                .withBody("Hello!")
                .bind(behavior);

        int input;
        SpyOutputStream outputStream = new SpyOutputStream();
        InputStream inputStream = response.compose().getBody();

        while ((input = inputStream.read()) != -1) outputStream.write(input);

        assertThat(outputStream.getWrittenOutput(), is("llo"));
    }

    @Test
    public void returnsTailEndWhenOnlyGivenSecondValue() throws Exception {
        Request request = new Request();
        Behavior behavior = new PartialContent();

        request.add("GET / HTTP/1.1");
        request.add("Range: byte=-2");

        Response response =
            Response
                .wrap(request)
                .withBody("Hello!")
                .bind(behavior);

        int input;
        SpyOutputStream outputStream = new SpyOutputStream();
        InputStream inputStream = response.compose().getBody();

        while ((input = inputStream.read()) != -1) outputStream.write(input);

        assertThat(outputStream.getWrittenOutput(), is("o!"));
    }

}
