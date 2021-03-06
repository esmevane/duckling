package duckling.responders;

import duckling.behaviors.*;
import duckling.requests.Request;

import java.io.*;

public class FileContents extends Responder {
    private File file;

    public FileContents(Request request) {
        super(request);

        this.file = request.getFile();

        response.bind(new HasOptions(allowedMethodsString()));
        response.bind(new RestrictToMethods(allowedMethods));
        response.bind(new GuessContentType());
        response.bind(new DisplayFile());

        response.bind(
            new MaybeBehavior(
                (givenRequest) -> !givenRequest.headers.get("Range").isEmpty(),
                new PartialContent()
            )
        );

        response.bind(
            new MaybeBehavior(
                (givenRequest) -> !givenRequest.headers.get("If-Match").isEmpty(),
                new PatchTextContent()
            )
        );
    }

    @Override
    public boolean matches() {
        return this.file.exists() && !this.file.isDirectory();
    }

}
