package duckling.routing;

import duckling.requests.Request;
import duckling.responses.ResponseCode;

import java.util.function.Function;

public class Route {
    protected String routeName;
    protected String method;

    private RouteContents routeContents;
    private ResponseCode responseCode;

    public Route() {
        this("GET", "/");
    }

    public Route(String method, String routeName) {
        this(method, routeName, "not-found");
    }

    public Route(String method, String routeName, String routeContents) {
        this(
            method,
            routeName,
            new RouteContents(routeContents),
            ResponseCode.ok()
        );
    }

    public Route(
        String method,
        String routeName,
        Function<Request, String> routeContents
    ) {
        this(
            method,
            routeName,
            new RouteContents(routeContents),
            ResponseCode.ok()
        );
    }


    public Route(
        String method,
        String routeName,
        RouteContents routeContents,
        ResponseCode responseCode
    ) {
        this.method = method;
        this.routeName = routeName.startsWith("/") ? routeName : "/" + routeName;
        this.routeContents = routeContents;
        this.responseCode = responseCode;
    }

    public Route andRejectWith(int code) {
        return new Route(method, routeName, routeContents, new ResponseCode(code));
    }

    public Route andRedirectTo(String uri) {
        RouteContents contents = new RouteContents(uri);
        return new Route(method, routeName, contents, ResponseCode.found());
    }

    public String getContent(Request request) {
        return this.routeContents.get(request);
    }

    public String getContent() {
        return this.routeContents.get();
    }

    public boolean hasMethod(String method) {
        return this.method.equals(method);
    }

    public boolean hasResponder(String routeContents) {
        return this.routeContents.equals(routeContents);
    }

    public boolean hasRoute(String route) {
        return this.routeName.equals(route);
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public boolean matches(Request request) {
        return equals(Routes.fromRequest(request));
    }

    public Route with(Function<Request, String> routeContents) {
        return new Route(method, routeName, routeContents);
    }

    @Override
    public int hashCode() {
        return this.routeName.hashCode() + this.method.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Route) {
            Route other = (Route) object;

            return this.routeName.equals(other.routeName) &&
                this.method.equals(other.method);
        }

        return false;
    }

    @Override
    public String toString() {
        return method + " " + routeName + " - " + routeContents + " " + responseCode;
    }

    public boolean isRedirect() {
        return responseCode.equals(ResponseCode.found());
    }
}
