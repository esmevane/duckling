package duckling.support;

import java.io.IOException;
import java.io.OutputStream;

public class SpyOutputStream extends OutputStream {
    private StringBuilder writtenOutput = new StringBuilder();

    @Override
    public void write(int input) throws IOException {
        writtenOutput.append((char) input);
    }

    public String getWrittenOutput() {
        return writtenOutput.toString();
    }
}
