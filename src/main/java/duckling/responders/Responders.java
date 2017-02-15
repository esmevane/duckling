package duckling.responders;

import duckling.Configuration;
import duckling.requests.Request;
import duckling.writers.BodyWriter;
import duckling.writers.HeadersWriter;
import duckling.writers.Writer;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Responders {
    private Request request;
    private ArrayList<Responder> responders;

    public Responders(Request request, Configuration config) {
        this(
            request,
            new RoutedContents(request, config),
            new FileContents(request),
            new FolderContents(request)
        );
    }

    public Responders(
        Request request,
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
            map(Function.identity()).
            orElseGet(() -> new NotFound(request));
    }

    public static void respondTo(
        Request request,
        OutputStream output,
        Configuration config
    ) {
        Responder responder = new Responders(request, config).getResponder();
        Predicate<Writer> respondsTo = writer -> writer.respondsTo(request);
        Consumer<Writer> write = writer -> writer.write();

        Stream.of(
            new HeadersWriter(responder, output),
            new BodyWriter(responder, output)
        ).filter(respondsTo).forEach(write);
    }
}
