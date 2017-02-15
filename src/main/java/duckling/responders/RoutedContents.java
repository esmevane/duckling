package duckling.responders;

import duckling.*;
import duckling.behaviors.HasOptions;
import duckling.behaviors.MaybeRoute;
import duckling.requests.Request;
import duckling.routing.RouteDefinitions;

public class RoutedContents extends Responder {
    private RouteDefinitions routes = new RouteDefinitions();

    public RoutedContents(Request request, Configuration config) {
        super(request);

        this.routes = config.routes;

        response.bind(new HasOptions(allowedMethodsString()));
        response.bind(new MaybeRoute(config.routes));
    }

    @Override
    public boolean matches() {
        return routes.hasRoute(request);
    }

}
