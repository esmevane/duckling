package duckling.writers;

import duckling.requests.Request;
import duckling.responders.Responder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

public class HeadersWriter extends Writer {

    public HeadersWriter(Responder responder, OutputStream output) {
        super(responder, output);
    }

    public void write() {
        List<String> headers;
        headers = responder.headers();
        Consumer<String> writeLine = line -> {
            try {
                this.output.write(line.getBytes());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        };

        headers.forEach(writeLine);
    }

    @Override
    public boolean respondsTo(Request request) {
        return true;
    }

}
