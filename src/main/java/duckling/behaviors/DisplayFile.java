package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;

import java.io.*;

public class DisplayFile implements Behavior {
    @Override
    public Response apply(Request request) {
        return Response
            .wrap(request)
            .withStreamBody(new BufferedInputStream(getStream(request)));
    }

    private InputStream getStream(Request request) {
        try {
            return new FileInputStream(request.getFilePath());
        } catch (FileNotFoundException exception) {
            return new ByteArrayInputStream("".getBytes());
        }

    }
}
