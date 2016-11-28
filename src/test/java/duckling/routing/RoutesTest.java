package duckling.routing;

import duckling.requests.Request;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RoutesTest {

    @Test
    public void fromRequestUsesRequestMethod() throws Exception {
        Request request = new Request();
        request.add("GET / HTTP/1.1");
        Route route = Routes.fromRequest(request);
        assertThat(route.hasMethod("GET"), is(true));
    }

    @Test
    public void fromRequestUsesRequestPath() throws Exception {
        Request request = new Request();
        request.add("GET /routes HTTP/1.1");
        Route route = Routes.fromRequest(request);
        assertThat(route.hasRoute("/routes"), is(true));
    }

    @Test
    public void getReturnsGetRoute() throws Exception {
        Route getRoute = Routes.get();
        assertThat(getRoute.hasMethod("GET"), is(true));
    }

    @Test
    public void postReturnsPostRoute() throws Exception {
        Route postRoute = Routes.post();
        assertThat(postRoute.hasMethod("POST"), is(true));
    }

    @Test
    public void putReturnsPutRoute() throws Exception {
        Route putRoute = Routes.put();
        assertThat(putRoute.hasMethod("PUT"), is(true));
    }

    @Test
    public void patchReturnsPatchRoute() throws Exception {
        Route patchRoute = Routes.patch();
        assertThat(patchRoute.hasMethod("PATCH"), is(true));
    }

    @Test
    public void deleteReturnsDeleteRoute() throws Exception {
        Route deleteRoute = Routes.delete();
        assertThat(deleteRoute.hasMethod("DELETE"), is(true));
    }

    @Test
    public void getAcceptsRoute() throws Exception {
        String routeName = "routes";
        Route getRoute = Routes.get(routeName);
        assertThat(getRoute.hasRoute("/routes"), is(true));
    }

    @Test
    public void postAcceptsRoute() throws Exception {
        String routeName = "routes";
        Route postRoute = Routes.post(routeName);
        assertThat(postRoute.hasRoute("/routes"), is(true));
    }

    @Test
    public void putAcceptsRoute() throws Exception {
        String routeName = "routes";
        Route putRoute = Routes.put(routeName);
        assertThat(putRoute.hasRoute("/routes"), is(true));
    }

    @Test
    public void patchAcceptsRoute() throws Exception {
        String routeName = "routes";
        Route patchRoute = Routes.patch(routeName);
        assertThat(patchRoute.hasRoute("/routes"), is(true));
    }

    @Test
    public void deleteAcceptsRoute() throws Exception {
        String routeName = "routes";
        Route deleteRoute = Routes.delete(routeName);
        assertThat(deleteRoute.hasRoute("/routes"), is(true));
    }

}
