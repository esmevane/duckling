package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;
import duckling.routing.RouteDefinitions;
import duckling.routing.Routes;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MaybeRouteHeadersTest {
    @Test
    public void suppliesDefinedContentIfGivenMatch() throws Exception {
        RouteDefinitions definitions = new RouteDefinitions(
            Routes.get("/hello").with(new RespondWith(ResponseCodes.TEAPOT), new StaticBody("Hey"))
        );

        Behavior behavior = new MaybeRouteHeaders(definitions);

        Request request = new Request();
        request.add("GET /hello HTTP/1.1");

        assertThat(
            behavior.apply(request).compose().getResponseHeaders(),
            is(
                Response
                    .wrap(new Request())
                    .withResponseCode(ResponseCodes.TEAPOT)
                    .withContentType("text/html")
                    .getResponseHeaders()
            )
        );
    }

    @Test
    public void suppliesEmptyPageIfNoRouteMatch() throws Exception {
        RouteDefinitions definitions = new RouteDefinitions(
            Routes.get("/hello").with(new StaticBody("Hey"))
        );

        Behavior behavior = new MaybeRouteHeaders(definitions);

        assertThat(
            behavior.apply(new Request()).getResponseHeaders(),
            is(
                Response
                    .wrap(new Request())
                    .withResponseCode(ResponseCodes.NOT_FOUND)
                    .getResponseHeaders()
            )
        );
    }
}
