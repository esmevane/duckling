package duckling.writers;

import duckling.responders.Responder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class HeadersWriter extends Writer {

    public HeadersWriter(Responder responder, OutputStream output) {
        super(responder, output);
    }

    public void write() throws IOException {
        List<String> headers = responder.headers();

        headers.forEach(this::writeLine);
    }

    private void writeLine(String line) {
        try {
            output.write(line.getBytes());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
