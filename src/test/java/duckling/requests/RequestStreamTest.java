package duckling.requests;

import duckling.Server;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class RequestStreamTest {
    @Test
    public void producesList() throws Exception {
        String request = "GET / HTTP/1.1" + Server.CRLF + Server.CRLF;
        byte[] getRequest = request.getBytes();
        InputStream requestContents = new ByteArrayInputStream(getRequest);
        RequestStream requestStream = new RequestStream(requestContents);

        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, request.split(Server.CRLF));

        assertThat(requestStream.toList(), is(equalTo(list)));
    }
}
