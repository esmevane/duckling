package duckling.responders;

import duckling.*;
import duckling.requests.Request;
import duckling.responses.ResponseCode;
import duckling.responses.ResponseHeaders;
import duckling.routing.Route;
import duckling.routing.RouteDefinitions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;

public class DefinedContents extends Responder {
    private RouteDefinitions routes;

    public DefinedContents(Request request, Configuration config) {
        super(request, config);

        this.routes = config.routes;
    }

    @Override
    public boolean matches() {
        if (routes == null) return false; // TODO: test for this line (or remove it)

        return routes.hasRoute(request);
    }

    @Override
    public ArrayList<String> headers() throws IOException {
        Optional<Route> maybeRoute = routes.getMatch(request);
        ResponseHeaders headers = new ResponseHeaders();

        return maybeRoute.map(route -> headers.withStatus(route.getResponseCode()).withContentType("text/html").toList()).
                orElseGet(() -> headers.withStatus(ResponseCode.notFound()).toList());
    }

    @Override
    public InputStream body() throws IOException {
        Optional<Route> maybeRoute = routes.getMatch(request);
        String template =
            "<html><head><title>%s</title></head>" +
                "<body>%s</body></html>";

        String fileContent = maybeRoute.map(route -> String.format(template, request.getPath(), route.getContent())).
                orElseGet(() -> String.format(template, "", ""));

        return new ByteArrayInputStream(fileContent.getBytes());
    }
}
