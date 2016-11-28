package duckling.responders;

import duckling.responses.ResponseHeaders;
import duckling.requests.Request;
import duckling.support.SpyOutputStream;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FolderContentsTest {
    @Test
    public void matchesWhenExistsAndIsDirectory() throws Exception {
        File file = new File(".") {
            @Override
            public boolean exists() {
                return true;
            }

            @Override
            public boolean isDirectory() {
                return true;
            }
        };

        FolderContents responder = new FolderContents(file);

        assertThat(responder.matches(), is(true));
    }

    @Test
    public void doesNotMatchWhenDoesNotExist() throws Exception {
        File file = new File(".") {
            @Override
            public boolean exists() {
                return false;
            }
        };

        FolderContents responder = new FolderContents(file);

        assertThat(responder.matches(), is(false));
    }

    @Test
    public void doesNotMatchWhenIsNotDirectory() throws Exception {
        File file = new File(".") {
            @Override
            public boolean exists() {
                return true;
            }

            @Override
            public boolean isDirectory() {
                return false;
            }
        };

        FolderContents responder = new FolderContents(file);

        assertThat(responder.matches(), is(false));
    }

    @Test
    public void providesHeadersWithHtmlContentType() throws Exception {
        FolderContents responder = new FolderContents(new Request());
        ArrayList<String> headers =
            new ResponseHeaders().withContentType("text/html").toList();

        assertThat(responder.headers(), is(headers));
    }

    @Test
    public void providesStreamOfEmptyPageAsBody() throws Exception {
        FolderContents responder = new FolderContents(new Request()) {
            @Override
            public String contents() {
                return "";
            }
        };

        SpyOutputStream outputStream = new SpyOutputStream();
        InputStream inputStream = responder.body();
        String expectation =
            "<html><head><title>.</title></head><body></body></html>";

        int input;

        while ((input = inputStream.read()) != -1) {
            outputStream.write(input);
        }

        assertThat(outputStream.getWrittenOutput(), is(expectation));
    }
}
