package duckling.responders;

import duckling.*;
import duckling.behaviors.RedirectTo;
import duckling.behaviors.RespondWith;
import duckling.behaviors.StaticBody;
import duckling.requests.Request;
import duckling.responses.CommonHeaders;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;
import duckling.routing.Route;
import duckling.routing.RouteDefinitions;
import duckling.routing.Routes;
import duckling.support.SpyOutputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RoutedContentsTest {
    private Configuration config;
    private Request request;
    private RoutedContents responder;

    @Before
    public void setup() throws Exception {
        Route routes = Routes.get("/tea").with(new StaticBody("Tea indeed"));

        request = new Request();
        request.add("GET /tea HTTP/1.1");

        config = new Configuration(new RouteDefinitions(routes));
        responder = new RoutedContents(request, config);
    }

    @Test
    public void matchesRequestWithRoute() throws Exception {
        assertThat(responder.matches(), is(true));
    }

    @Test
    public void suppliesDefaultRouteHeaders() throws Exception {
        ArrayList<String> headers =
            Response
                .wrap(new Request())
                .withContentType("text/html")
                .getResponseHeaders();

        assertThat(responder.headers(), is(headers));
    }

    @Test
    public void includesLocationDetailForRedirects() throws Exception {
        RouteDefinitions definitions = new RouteDefinitions(
            Routes.get("/redirect").with(new RedirectTo("/howdy"))
        );

        Configuration config = new Configuration(definitions);

        Request request = new Request();
        request.add("GET /redirect HTTP/1.1");

        Responder responder = new RoutedContents(request, config);

        ArrayList<String> headers =
            Response
                .wrap(new Request())
                .withResponseCode(ResponseCodes.FOUND)
                .withHeader(CommonHeaders.LOCATION, "/howdy")
                .withContentType("text/html")
                .getResponseHeaders();

        assertThat(responder.headers(), is(headers));
    }

    @Test
    public void suppliesCustomDefinedHeaders() throws Exception {
        request = new Request();
        request.add("GET /coffee HTTP/1.1");

        config = new Configuration(
            new RouteDefinitions(
                Routes.get("/coffee").
                    with(
                        new RespondWith(ResponseCodes.TEAPOT),
                        new StaticBody("I'm a teapot")
                    )
            )
        );

        responder = new RoutedContents(request, config);

        ArrayList<String> headers =
            Response
                .wrap(new Request())
                .withResponseCode(ResponseCodes.TEAPOT)
                .withContentType("text/html")
                .getResponseHeaders();

        assertThat(responder.headers(), is(headers));
    }

    @Test
    public void presentsGetHeadOptionsAsOptions() throws Exception {
        String methods = config
            .routes
            .allMethodsForRoute("/coffee")
            .stream()
            .collect(Collectors.joining(","));

        request = new Request();
        request.add("OPTIONS /coffee HTTP/1.1");

        responder = new RoutedContents(request, config);
        ArrayList<String> headers =
            Response
                .wrap(request)
                .withResponseCode(ResponseCodes.NOT_FOUND)
                .withHeader(CommonHeaders.ALLOW, methods)
                .getResponseHeaders();

        assertThat(responder.headers(), is(headers));
    }

    @Test
    public void suppliesTheRouteDefinedBody() throws Exception {
        SpyOutputStream outputStream = new SpyOutputStream();
        InputStream inputStream = responder.body();
        String expectation =
            "<html>" +
                "<head><title>/tea</title></head>" +
                "<body>Tea indeed</body>" +
                "</html>";

        int input;

        while ((input = inputStream.read()) != -1) {
            outputStream.write(input);
        }

        assertThat(outputStream.getWrittenOutput(), is(expectation));
    }
}
