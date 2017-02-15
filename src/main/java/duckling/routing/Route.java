package duckling.routing;

import duckling.pages.Page;
import duckling.pages.StaticBody;
import duckling.requests.Request;
import duckling.responses.ResponseCodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class Route {
    protected String routeName;
    protected String method;

    private ArrayList<Page> pages;
    private ResponseCodes responseCode;

    public Route() {
        this("GET", "/");
    }

    public Route(String method, String routeName) {
        this(method, routeName, new StaticBody("not-found"));
    }

    public Route(String method, String routeName, Page... pages) {
        this(method, routeName, ResponseCodes.OK, pages);
    }

    public Route(
        String method,
        String routeName,
        ResponseCodes responseCode,
        Page... pages
    ) {
        this(method, routeName, responseCode, new ArrayList<>());

        Collections.addAll(this.pages, pages);
    }

    public Route(
        String method,
        String routeName,
        ResponseCodes responseCode,
        ArrayList<Page> pages
    ) {
        this.method = method;
        this.routeName = routeName.startsWith("/") ? routeName : "/" + routeName;
        this.responseCode = responseCode;
        this.pages = pages;
    }

    public Route andRejectWith(ResponseCodes code) {
        return new Route(method, routeName, code, pages);
    }

    public Route andRedirectTo(String uri) {
        return new Route(method, routeName, ResponseCodes.FOUND, new StaticBody(uri));
    }

    public Route with(Page... pages) {
        return new Route(method, routeName, pages);
    }

    public String getContent(Request request) {
        return this.pages.stream().map((page) -> page.apply(request)).collect(Collectors.joining());
    }

    public String getContent() {
        return getContent(new Request());
    }

    public boolean hasMethod(String method) {
        return this.method.equals(method);
    }

    public boolean hasResponder(String routeContents) {
        return pages
            .stream()
            .map((page) -> page.apply(new Request()))
            .anyMatch((string) -> string.equals(routeContents));
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
        return method + " " + routeName + " - " + getContent() + " " + responseCode;
    }

    public boolean isRedirect() {
        return responseCode.equals(ResponseCodes.FOUND);
    }

}
