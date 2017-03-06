package duckling.behaviors;

import duckling.requests.Request;
import duckling.routing.RouteDefinitions;
import duckling.routing.Routes;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MaybeRouteBodyTest {
    @Test
    public void suppliesDefinedContentIfGivenMatch() throws Exception {
        RouteDefinitions definitions = new RouteDefinitions(
            Routes.get("/hello").with(new StaticBody("Hey"))
        );

        Behavior behavior = new MaybeRouteBody(definitions);

        Request request = new Request();
        request.add("GET /hello HTTP/1.1");

        assertThat(behavior.apply(request).compose().getStringBody(), is("Hey"));
    }

    @Test
    public void suppliesEmptyPageIfNoRouteMatch() throws Exception {
        RouteDefinitions definitions = new RouteDefinitions(
            Routes.get("/hello").with(new StaticBody("Hey"))
        );

        Behavior behavior = new MaybeRouteBody(definitions);

        assertThat(
            behavior.apply(new Request()).compose().getStringBody(),
            is("<html><head><title></title></head><body></body></html>")
        );
    }

}
