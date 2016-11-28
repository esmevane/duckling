package duckling.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SpySocket extends Socket {
    private boolean inputStreamCalled = false;
    private boolean outputStreamCalled = false;
    private boolean closed = false;

    public boolean wasClosed() {
        return this.closed;
    }

    public boolean wasInputStreamCalled() {
        return this.inputStreamCalled;
    }

    public boolean wasOutputStreamCalled() {
        return this.outputStreamCalled;
    }

    @Override
    public void close() {
        this.closed = true;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        this.outputStreamCalled = true;

        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        };
    }

    @Override
    public InputStream getInputStream() throws IOException {
        this.inputStreamCalled = true;

        return new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
    }

}
