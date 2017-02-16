package duckling.writers;

import duckling.requests.Request;
import duckling.responders.Responder;

import java.io.OutputStream;

public abstract class Writer {
    Responder responder;
    OutputStream output;

    Writer(Responder responder, OutputStream output) {
        this.responder = responder;
        this.output = output;
    }

    abstract public void write();

    public abstract boolean respondsTo(Request request);
}

