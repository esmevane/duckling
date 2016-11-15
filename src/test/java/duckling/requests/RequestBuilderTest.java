package duckling.requests;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;


public class RequestBuilderTest {

    @Test
    public void withRoot() throws Exception {
        byte[] getRequest = "GET / HTTP/1.1\r\n\r\n".getBytes();
        String root = "/some/directory/path";
        InputStream requestContents = new ByteArrayInputStream(getRequest);
        RequestBuilder builder = new RequestBuilder(root, requestContents);
        Request request = new Request(root);

        assertEquals(request.getRoot(), builder.build().getRoot());
    }

    @Test
    public void withoutRoot() throws Exception {
        Request request = new Request();
        assertEquals(".", request.getRoot());
    }


    @Test
    public void fromInputStream() throws Exception {
        byte[] getRequest = "GET / HTTP/1.1\r\n\r\n".getBytes();
        InputStream requestContents = new ByteArrayInputStream(getRequest);
        RequestBuilder builder = new RequestBuilder(requestContents);
        Request request = new Request();

        request.add("GET / HTTP/1.1");

        assertEquals(true, request.equals(builder.build()));
    }
}
