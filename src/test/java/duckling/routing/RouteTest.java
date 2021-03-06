package duckling.routing;

import duckling.requests.Request;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class RouteTest {

    @Test
    public void hasDefaultRoute() throws Exception {
        Route route = new Route();
        assertThat(route.hasRoute("/"), is(true));
    }

    @Test
    public void acceptsRouteWithPrefix() throws Exception {
        String routeName = "/routes";
        Route route = new Route("GET", routeName);
        assertThat(route.hasRoute("/routes"), is(true));
    }

    @Test
    public void matchesRequestsWithSameMethodAndRoute() throws Exception {
        String routeName = "/routes";
        Route route = new Route("GET", routeName);
        Route otherRoute = new Route("GET", routeName);
        assertThat(route, equalTo(otherRoute));
    }

    @Test
    public void doesNotMatchRequestWithDifferentMethod() throws Exception {
        String routeName = "/routes";
        Route route = new Route("GET", routeName);
        Route otherRoute = new Route("POST", routeName);
        assertThat(route, not(equalTo(otherRoute)));
    }

    @Test
    public void doesNotMatchRequestWithDifferentRoute() throws Exception {
        Route route = new Route("GET", "/routes");
        Route otherRoute = new Route("GET", "/shoes");
        assertThat(route, not(equalTo(otherRoute)));
    }

    @Test
    public void matchesRequestObjectWithSameRoute() throws Exception {
        Route route = new Route("GET", "/routes");
        Request request = new Request();

        request.add("GET /routes HTTP/1.1");

        assertThat(route.matches(request), is(true));
    }

    @Test
    public void doesNotMatchRequestObjectWithOtherRoute() throws Exception {
        Route route = new Route("GET", "/routes");
        Request request = new Request();

        request.add("GET / HTTP/1.1");

        assertThat(route.matches(request), is(not(true)));
    }

}
