package duckling.writers;

import duckling.requests.Request;
import duckling.responders.Responder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BodyWriter extends Writer {
    public BodyWriter(Responder responder, OutputStream output) {
        super(responder, output);
    }

    public void write() {
        int input;
        InputStream body = null;

        try {
            body = responder.body();
            while ((input = body.read()) != -1) output.write(input);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public boolean respondsTo(Request request) {
        return !request.isHead();
    }
}
