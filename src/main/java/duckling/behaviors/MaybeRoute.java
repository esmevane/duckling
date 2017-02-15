package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;
import duckling.routing.RouteDefinitions;

public class MaybeRoute implements Behavior {
    private RouteDefinitions routes;

    public MaybeRoute(RouteDefinitions routes) {
        this.routes = routes;
    }

    @Override
    public Response apply(Request request) {
        Response headers = new MaybeRouteHeaders(routes).apply(request);
        Response body = new MaybeRouteBody(routes).apply(request);

        return headers.merge(body);
    }
}
