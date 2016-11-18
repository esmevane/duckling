package duckling.writers;

import duckling.Server;
import duckling.support.SpyOutputStream;
import duckling.support.SpyResponder;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HeaderWriterTest {
    @Test
    public void writeGetsResponderHeaders() throws Exception {
        OutputStream output = new OutputStream() {
            @Override
            public void write(int input) throws IOException { }
        };

        SpyResponder spyResponder = new SpyResponder();
        HeadersWriter writer = new HeadersWriter(spyResponder, output);

        writer.write();

        assertThat(spyResponder.getWereHeadersCalled(), is(true));
    }

    @Test
    public void writesGivenBodyToOutput() throws Exception {
        ArrayList<String> list = new ArrayList<>();

        list.add("Line one" + Server.CRLF);
        list.add("Line two" + Server.CRLF);
        list.add(Server.CRLF);

        SpyResponder spyResponder = new SpyResponder(list);
        SpyOutputStream output = new SpyOutputStream();
        HeadersWriter writer = new HeadersWriter(spyResponder, output);

        writer.write();

        assertThat(
                output.getWrittenOutput(),
                is("Line one" + Server.CRLF + "Line two" + Server.CRLF + Server.CRLF)
        );
    }
}
