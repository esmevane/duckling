package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.CommonHeaders;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;
import duckling.routing.Route;
import duckling.routing.RouteDefinitions;

import java.util.Optional;

public class MaybeRouteHeaders implements Behavior {
    private RouteDefinitions routes;

    public MaybeRouteHeaders(RouteDefinitions routes) {
        this.routes = routes;
    }
    @Override
    public Response apply(Request request) {
        Optional<Route> maybeRoute = routes.getMatch(request);

        return maybeRoute.
            map((route) -> route.isRedirect() ?
                Response.wrap(request)
                    .respondWith(ResponseCodes.FOUND)
                    .withHeader(CommonHeaders.LOCATION, route.getContent()) :
                Response.wrap(request)
                    .respondWith(route.getResponseCode())
                    .contentType("text/html")).
            orElseGet(() -> Response.wrap(request).respondWith(ResponseCodes.NOT_FOUND));
    }
}
