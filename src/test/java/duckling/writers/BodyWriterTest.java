package duckling.writers;

import duckling.support.SpyOutputStream;
import duckling.support.SpyResponder;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BodyWriterTest {
    @Test
    public void writeGetsResponderBody() throws Exception {
        OutputStream output = new OutputStream() {
            @Override
            public void write(int input) throws IOException {
            }
        };

        SpyResponder spyResponder = new SpyResponder();
        BodyWriter writer = new BodyWriter(spyResponder, output);

        writer.write();

        assertThat(spyResponder.getWasBodyCalled(), is(true));
    }

    @Test
    public void writesGivenBodyToOutput() throws Exception {
        String example = "An example input string";
        InputStream inputStream = new ByteArrayInputStream(example.getBytes());
        SpyResponder spyResponder = new SpyResponder(inputStream);
        SpyOutputStream output = new SpyOutputStream();
        BodyWriter writer = new BodyWriter(spyResponder, output);

        writer.write();

        assertThat(output.getWrittenOutput(), is(example));
    }
}
