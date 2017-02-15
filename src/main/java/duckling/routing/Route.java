package duckling.routing;

import duckling.pages.Page;
import duckling.requests.Request;
import duckling.responses.ResponseCodes;

public class Route {
    protected String routeName;
    protected String method;

    private RouteContents routeContents;
    private ResponseCodes responseCode;

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
            ResponseCodes.OK
        );
    }

    public Route(
        String method,
        String routeName,
        Page routeContents
    ) {
        this(
            method,
            routeName,
            new RouteContents(routeContents),
            ResponseCodes.OK
        );
    }


    public Route(
        String method,
        String routeName,
        RouteContents routeContents,
        ResponseCodes responseCode
    ) {
        this.method = method;
        this.routeName = routeName.startsWith("/") ? routeName : "/" + routeName;
        this.routeContents = routeContents;
        this.responseCode = responseCode;
    }

    public Route andRejectWith(ResponseCodes code) {
        return new Route(method, routeName, routeContents, code);
    }

    public Route andRedirectTo(String uri) {
        RouteContents contents = new RouteContents(uri);
        return new Route(method, routeName, contents, ResponseCodes.FOUND);
    }

    public Route with(Page page) {
        return new Route(method, routeName, page);
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

    public ResponseCodes getResponseCode() {
        return responseCode;
    }

    public boolean matches(Request request) {
        return equals(Routes.fromRequest(request));
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
        return responseCode.equals(ResponseCodes.FOUND);
    }

}
