package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;
import duckling.routing.Route;
import duckling.routing.RouteDefinitions;

import java.util.Optional;

public class MaybeRouteBody implements Behavior {
    private RouteDefinitions routes;
    private String template =
        "<html><head><title>%s</title></head>" +
            "<body>%s</body></html>";

    public MaybeRouteBody(RouteDefinitions routes) {
        this.routes = routes;
    }

    @Override
    public Response apply(Request request) {
        Response response = Response.wrap(request);
        Optional<Route> maybeRoute = routes.getMatch(request);
        String routeContent =
            maybeRoute
                .map(
                    (route) ->
                        String.format(
                            template,
                            request.getPath(),
                            response.withBehaviors(route.getBehaviors()).compose().getStringBody()
                        )
                )
                .orElseGet(() -> String.format(template, "", ""));

        return response.withBody(routeContent);
    }
}
