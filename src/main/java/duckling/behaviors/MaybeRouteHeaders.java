package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.CommonHeaders;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;
import duckling.routing.Route;
import duckling.routing.RouteDefinitions;

import java.util.Optional;
import java.util.stream.Collectors;

public class MaybeRouteHeaders implements Behavior {
    private RouteDefinitions routes;

    public MaybeRouteHeaders(RouteDefinitions routes) {
        this.routes = routes;
    }

    @Override
    public Response apply(Request request) {
        if (request.isOptions() && routes.hasPath(request.getPath())) {
            return Response
                .wrap(request)
                .withResponseCode(ResponseCodes.OK)
                .withHeader(
                    CommonHeaders.ALLOW,
                    routes
                        .allMethodsForRoute(request.getPath())
                        .stream()
                        .collect(Collectors.joining(","))
                );
        }

        Optional<Route> maybeRoute = routes.getMatch(request);

        return maybeRoute.
            map(
                route ->
                    Response
                        .wrap(request)
                        .withBehaviors(route.getBehaviors())
                        .withContentType("text/html")
            )
            .orElseGet(
                () ->
                    Response
                        .wrap(request)
                        .withResponseCode(ResponseCodes.NOT_FOUND)
            );
    }
}
