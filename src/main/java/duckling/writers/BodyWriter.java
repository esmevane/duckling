package duckling.writers;

import duckling.responders.Responder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BodyWriter extends Writer {
    public BodyWriter(Responder responder, OutputStream output) {
        super(responder, output);
    }

    public void write() throws IOException {
        int input;
        InputStream body = responder.body();

        while ((input = body.read()) != -1) output.write(input);
    }
}
