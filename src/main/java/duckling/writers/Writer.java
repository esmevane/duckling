package duckling.writers;

import duckling.requests.Request;
import duckling.responders.Responder;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Writer {
    protected Responder responder;
    protected OutputStream output;

    public Writer(Responder responder, OutputStream output) {
        this.responder = responder;
        this.output = output;
    }

    abstract public void write();

    public abstract boolean respondsTo(Request request);
}

