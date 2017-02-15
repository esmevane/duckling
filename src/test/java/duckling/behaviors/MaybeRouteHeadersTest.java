package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.CommonHeaders;
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
            Routes.get("/hello").with(new StaticBody("Hey"))
        );

        Behavior behavior = new MaybeRouteHeaders(definitions);

        Request request = new Request();
        request.add("GET /hello HTTP/1.1");

        assertThat(
            behavior.apply(request).getResponseHeaders(),
            is(
                Response
                    .wrap(new Request())
                    .respondWith(ResponseCodes.OK)
                    .contentType("text/html")
                    .getResponseHeaders()
            )
        );
    }

    @Test
    public void suppliesRedirectHeadersWhenDefined() throws Exception {
        RouteDefinitions definitions = new RouteDefinitions(
            Routes.get("/redirect").andRedirectTo("/")
        );

        Behavior behavior = new MaybeRouteHeaders(definitions);

        Request request = new Request();
        request.add("GET /redirect HTTP/1.1");

        assertThat(
            behavior.apply(request).getResponseHeaders(),
            is(
                Response
                    .wrap(new Request())
                    .respondWith(ResponseCodes.FOUND)
                    .withHeader(CommonHeaders.LOCATION, "/")
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
                    .respondWith(ResponseCodes.NOT_FOUND)
                    .getResponseHeaders()
            )
        );
    }
}
