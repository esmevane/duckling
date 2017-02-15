package duckling.responders;

import duckling.behaviors.GuessContentType;
import duckling.behaviors.HasOptions;
import duckling.behaviors.RestrictToMethods;
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
    }

    @Override
    public InputStream body() {
        try {
            return new BufferedInputStream(
                new FileInputStream(this.file.getAbsoluteFile())
            );
        } catch (FileNotFoundException exception) {
            return new ByteArrayInputStream("".getBytes());
        }
    }

    @Override
    public boolean matches() {
        return this.file.exists() && !this.file.isDirectory();
    }

}
