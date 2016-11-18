package duckling.support;

import duckling.requests.Request;
import duckling.responders.Responder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SpyResponder extends Responder {
    private boolean wereHeadersCalled = false;
    private boolean wasBodyCalled = false;

    private ArrayList<String> list = new ArrayList<>();
    private InputStream inputStream = new InputStream() {
        @Override
        public int read() throws IOException { return -1; }
    };

    public SpyResponder() { this(new Request()); }
    public SpyResponder(Request request) { super(request); }

    public SpyResponder(InputStream stream) {
        super(new Request());
        this.inputStream = stream;
    }

    public SpyResponder(ArrayList<String> list) {
        super(new Request());
        this.list = list;
    }

    public boolean getWereHeadersCalled() {
        return wereHeadersCalled;
    }

    public boolean getWasBodyCalled() {
        return wasBodyCalled;
    }

    @Override
    public boolean matches() { return false; }

    @Override
    public ArrayList<String> headers() throws IOException {
        this.wereHeadersCalled = true;
        return this.list;
    }

    @Override
    public InputStream body() throws IOException {
        this.wasBodyCalled = true;
        return this.inputStream;
    }
}
