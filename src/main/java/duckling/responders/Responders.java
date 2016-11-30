package duckling.responders;

import duckling.Configuration;
import duckling.requests.Request;

import java.util.ArrayList;
import java.util.Collections;

public class Responders {
    private Request request;
    private ArrayList<Responder> responders;

    public Responders(Request request) {
        this(request, new Configuration());
    }

    public Responders(Request request, Configuration config) {
        this(
            request,
            config,
            new DefinedContents(request, config),
            new FileContents(request),
            new FolderContents(request),
            new NotFound(request)
        );
    }

    public Responders(
        Request request,
        Configuration config,
        Responder... responders
    ) {
        ArrayList<Responder> list = new ArrayList<>();
        Collections.addAll(list, responders);

        this.request = request;
        this.responders = list;
    }

    public Responder getResponder() {
        return responders.
            stream().
            filter(Responder::matches).
            findFirst().
            map((Responder candidate) -> candidate).
            orElseGet(() -> new NotFound(request));
    }
}
