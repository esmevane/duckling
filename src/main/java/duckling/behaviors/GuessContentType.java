package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;

import java.io.*;

import static java.net.URLConnection.guessContentTypeFromName;
import static java.net.URLConnection.guessContentTypeFromStream;

public class GuessContentType implements Behavior {
    @Override
    public Response apply(Request request) {
        return Response.wrap(request).withContentType(guessContentType(request.getFile()));
    }

    private String guessContentType(File file) {
        String fromName = guessContentTypeFromName(file.getName());

        try {
            return fromName != null ?
                fromName :
                guessContentTypeFromStream(getFileStream(file));
        } catch (IOException exception) {
            return fromName;
        }
    }

    private InputStream getFileStream(File file) {
        try {
            return new BufferedInputStream(new FileInputStream(file.getAbsoluteFile()));
        } catch (FileNotFoundException exception) {
            return new ByteArrayInputStream("".getBytes());
        }
    }
}
