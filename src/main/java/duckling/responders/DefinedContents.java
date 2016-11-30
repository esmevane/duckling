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
    private RouteDefinitions routes = new RouteDefinitions();
    private String template =
        "<html><head><title>%s</title></head>" +
            "<body>%s</body></html>";

    public DefinedContents(Request request, Configuration config) {
        super(request, config);

        this.routes = config.routes;
    }

    @Override
    public boolean matches() {
        return routes.hasRoute(request);
    }

    @Override
    public ArrayList<String> headers() throws IOException {
        Optional<Route> maybeRoute = routes.getMatch(request);

        return maybeRoute.
            map(this::headersFromRoute).
            orElseGet(this::buildHeaders);
    }

    @Override
    public InputStream body() throws IOException {
        Optional<Route> maybeRoute = routes.getMatch(request);
        String fileContent = maybeRoute.
            map(route -> buildContent(request.getPath(), route.getContent())).
            orElseGet(this::buildContent);

        return new ByteArrayInputStream(fileContent.getBytes());
    }

    @Override
    public boolean isAllowed() {
        return this.allowedMethods.contains(request.getMethod());
    }

    private String buildContent() {
        return buildContent("", "");
    }

    private String buildContent(String path, String content) {
        return String.format(this.template, path, content);
    }

    private ArrayList<String> headersFromRoute(Route route) {
        ResponseHeaders headers = new ResponseHeaders();

        return headers.
            withStatus(route.getResponseCode()).
            withContentType("text/html").
            toList();
    }

    private ArrayList<String> buildHeaders() {
        ResponseHeaders headers = new ResponseHeaders();

        return headers.withStatus(ResponseCode.notFound()).toList();
    }

}
