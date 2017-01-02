package duckling.responders;

import duckling.requests.Request;
import duckling.responses.ResponseHeaders;

import java.io.*;
import java.util.ArrayList;

import static java.net.URLConnection.guessContentTypeFromName;
import static java.net.URLConnection.guessContentTypeFromStream;

public class FileContents extends Responder {
    private File file;

    public FileContents(Request request) {
        super(request);

        this.file = request.getFile();
    }

    public FileContents(Request request, File file) {
        super(request);

        this.file = file;
    }

    public FileContents(File file) {
        this(new Request(), file);
    }

    @Override
    public boolean matches() {
        return this.file.exists() && !this.file.isDirectory();
    }

    @Override
    public ArrayList<String> headers() {
        ResponseHeaders headers = new ResponseHeaders();

        if (request.isOptions()) {
            return headers.allowedMethods(this.allowedMethods).toList();
        }

        if (!isAllowed()) {
            return headers.notAllowed().toList();
        }

        return headers.withContentType(getContentType()).toList();
    }

    @Override
    public InputStream body() {
        return getFileStream();
    }

    private String getContentType() {
        String fromName = guessContentTypeFromName(this.file.getName());

        try {
            if (fromName == null) {
                return guessContentTypeFromStream(getFileStream());
            } else {
                return fromName;
            }
        } catch (IOException exception) {
            return fromName;
        }
    }

    private InputStream getFileStream() {
        try {
            return new BufferedInputStream(
                new FileInputStream(this.file.getAbsoluteFile())
            );
        } catch (FileNotFoundException exception) {
            return new ByteArrayInputStream("".getBytes());
        }
    }

}
