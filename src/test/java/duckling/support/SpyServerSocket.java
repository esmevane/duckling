package duckling.support;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class SpyServerSocket extends ServerSocket {
    private boolean isBound = false;

    public SpyServerSocket() throws IOException {
    }

    @Override
    public Socket accept() {
        return new Socket();
    }

    @Override
    public void bind(SocketAddress address) {
        this.isBound = true;
    }

    @Override
    public void close() {
        this.isBound = false;
    }

    @Override
    public boolean isBound() {
        return this.isBound;
    }

    @Override
    public boolean isClosed() {
        return !this.isBound;
    }

}
