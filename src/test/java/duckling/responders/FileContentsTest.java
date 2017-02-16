package duckling.responders;

import duckling.responses.CommonHeaders;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;
import duckling.requests.Request;
import duckling.support.SpyOutputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileContentsTest {
    private Request request;

    @Before
    public void setup() throws Exception {
        request = new Request();
    }

    @Test
    public void matchesWhenExistsAndNotDirectory() throws Exception {
        request = new Request() {
            @Override
            public File getFile() {
                return new File(".") {
                    @Override
                    public boolean exists() { return true; }

                    @Override
                    public boolean isDirectory() { return false; }
                };
            }
        };

        FileContents responder = new FileContents(request);

        assertThat(responder.matches(), is(true));
    }

    @Test
    public void doesNotMatchWhenDoesNotExist() throws Exception {
        request = new Request() {
            @Override
            public File getFile() {
                return new File(".") {
                    @Override
                    public boolean exists() { return false; }
                };
            }
        };

        FileContents responder = new FileContents(request);

        assertThat(responder.matches(), is(false));
    }

    @Test
    public void doesNotMatchWhenIsDirectory() throws Exception {
        request = new Request() {
            @Override
            public File getFile() {
                return new File(".") {
                    @Override
                    public boolean exists() {
                        return false;
                    }

                    @Override
                    public boolean isDirectory() {
                        return true;
                    }
                };
            }
        };

        FileContents responder = new FileContents(request);

        assertThat(responder.matches(), is(false));
    }

    @Test
    public void providesHeadersWithContentType() throws Exception {
        File file = new File("file.txt");

        request = new Request() {
            @Override
            public File getFile() { return file; }
        };

        request.add("GET /file.txt HTTP/1.1");

        FileContents responder = new FileContents(request);
        ArrayList<String> headers =
            Response
                .wrap(request)
                .withContentType("text/plain")
                .getResponseHeaders();

        assertThat(responder.headers(), is(headers));
    }

    @Test
    public void providesStreamOfEmptyPageAsBody() throws Exception {
        FileContents responder = new FileContents(new Request());
        SpyOutputStream outputStream = new SpyOutputStream();
        InputStream inputStream = responder.body();
        String expectation = "";

        int input;

        while ((input = inputStream.read()) != -1) {
            outputStream.write(input);
        }

        assertThat(outputStream.getWrittenOutput(), is(expectation));
    }

    @Test
    public void disallowsNonGetRequests() throws Exception {
        request.add("PUT / HTTP/1.1");

        FileContents responder = new FileContents(request);
        ArrayList<String> headers =
            Response
                .wrap(request)
                .withResponseCode(ResponseCodes.METHOD_NOT_ALLOWED)
                .withContentType("null")
                .getResponseHeaders();

        assertThat(responder.headers(), is(headers));
    }

    @Test
    public void presentsGetHeadOptionsAsOptions() throws Exception {
        request.add("OPTIONS /coffee HTTP/1.1");

        FileContents responder = new FileContents(request);

        ArrayList<String> headers =
            Response
                .wrap(request)
                .withHeader(CommonHeaders.ALLOW, responder.allowedMethodsString())
                .getResponseHeaders();

        assertThat(responder.headers(), is(headers));
    }

    @Test
    public void respondsWithEmptyBodyOnMethodNotAllowed() throws Exception {
        request.add("PUT / HTTP/1.1");

        FileContents responder = new FileContents(request);
        SpyOutputStream outputStream = new SpyOutputStream();
        InputStream inputStream = responder.body();
        String expectation = "";

        int input;

        while ((input = inputStream.read()) != -1) {
            outputStream.write(input);
        }

        assertThat(outputStream.getWrittenOutput(), is(expectation));
    }

}
