package duckling.responders;

import duckling.requests.Request;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

public class Responders {
    private Request request;
    private OutputStream outputStream;
    private ArrayList<Responder> responders;

    public Responders(Request request, OutputStream outputStream) {
        this(
                request,
            outputStream,
            new FileContents(request, outputStream),
            new FolderContents(request, outputStream),
            new NotFound(request, outputStream)
        );
    }

    public Responders(Request request, OutputStream outputStream, Responder... responders) {
        ArrayList<Responder> list = new ArrayList<>();
        Collections.addAll(list, responders);

        this.request = request;
        this.outputStream = outputStream;
        this.responders = list;
    }

    public Responder getResponder() {
        Stream<Responder> candidates = responders.stream().filter(Responder::matches);
        Optional<Responder> candidate = candidates.findFirst();

        if (candidate.isPresent()) {
            return candidate.get();
        } else {
            return new NotFound(request, outputStream);
        }
    }
}
