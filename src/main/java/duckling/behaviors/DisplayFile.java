package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DisplayFile implements Behavior {
    @Override
    public Response apply(Request request) {
        Response response = Response.wrap(request);

        try {
            return response.withStreamBody(
                new BufferedInputStream(
                    new FileInputStream(request.getFile().getAbsoluteFile())
                )
            );
        } catch (FileNotFoundException exception) {
            return response.withStreamBody(new ByteArrayInputStream("".getBytes()));
        }
    }
}
