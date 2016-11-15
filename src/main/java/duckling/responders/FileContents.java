package duckling.responders;

import duckling.requests.Request;
import duckling.ResponseHeaders;

import java.io.*;

import static java.net.URLConnection.guessContentTypeFromName;
import static java.net.URLConnection.guessContentTypeFromStream;

public class FileContents extends Responder {
    private File file;

    public FileContents(Request request, OutputStream outputStream) {
        super(request, outputStream);

        this.file = request.getFile();
    }

    @Override
    public boolean matches() {
        return this.file.exists() && !this.file.isDirectory();
    }

    @Override
    public void respond() throws IOException {
        int bytes;

        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
        ResponseHeaders responseHeaders = new ResponseHeaders().
                withContentType(getContentType());

        for (String line: responseHeaders.toArray()) {
            this.outputStream.write(line.getBytes());
        }

        while ((bytes = fileInputStream.read(buffer)) != -1) {
            this.outputStream.write(buffer, 0, bytes);
        }
    }

    private String getContentType() throws IOException {
        String fromName = guessContentTypeFromName(file.getName());

        if (fromName == null) {
            InputStream buffer = new BufferedInputStream(
                    new FileInputStream(file)
            );

            return guessContentTypeFromStream(buffer);
        } else {
            return fromName;
        }
    }
}
