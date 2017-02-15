package duckling.responders;

import duckling.*;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RoutedContentsTest {
    private Configuration config;
    private Request request;
    private RoutedContents responder;

    @Before
    public void setup() throws Exception {
        Route routes = Routes.get("/tea").with((request) -> "Tea indeed");

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
                .contentType("text/html")
                .getResponseHeaders();

        assertThat(responder.headers(), is(headers));
    }

    @Test
    public void includesLocationDetailForRedirects() throws Exception {
        Route routes = Routes.get("/redirect").andRedirectTo("/howdy");
        Request request = new Request();
        Configuration config = new Configuration(new RouteDefinitions(routes));

        request.add("GET /redirect HTTP/1.1");

        Responder responder = new RoutedContents(request, config);

        ArrayList<String> headers =
            Response
                .wrap(new Request())
                .respondWith(ResponseCodes.FOUND)
                .withHeader(CommonHeaders.LOCATION, "/howdy")
                .contentType("text/html")
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
                    with((request) -> "I'm a teapot").
                    andRejectWith(ResponseCodes.TEAPOT)
            )
        );

        responder = new RoutedContents(request, config);

        ArrayList<String> headers =
            Response
                .wrap(new Request())
                .respondWith(ResponseCodes.TEAPOT)
                .contentType("text/html")
                .getResponseHeaders();

        assertThat(responder.headers(), is(headers));
    }

    @Test
    public void presentsGetHeadOptionsAsOptions() throws Exception {
        request = new Request();
        request.add("OPTIONS /coffee HTTP/1.1");

        responder = new RoutedContents(request, config);

        ArrayList<String> headers =
            Response
                .wrap(request)
                .respondWith(ResponseCodes.NOT_FOUND)
                .withHeader(CommonHeaders.ALLOW, responder.allowedMethodsString())
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
