package duckling.routing;

import duckling.behaviors.Behavior;
import duckling.behaviors.StaticBody;
import duckling.requests.Request;
import duckling.responses.ResponseCodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class Route {
    protected String routeName;
    protected String method;

    private ArrayList<Behavior> behaviors;
    private ResponseCodes responseCode;

    public Route() {
        this("GET", "/");
    }

    public Route(String method, String routeName) {
        this(method, routeName, new StaticBody("not-found"));
    }

    public Route(String method, String routeName, Behavior... behaviors) {
        this(method, routeName, ResponseCodes.OK, behaviors);
    }

    public Route(
        String method,
        String routeName,
        ResponseCodes responseCode,
        Behavior... behaviors
    ) {
        this(method, routeName, responseCode, new ArrayList<>());

        Collections.addAll(this.behaviors, behaviors);
    }

    public Route(
        String method,
        String routeName,
        ResponseCodes responseCode,
        ArrayList<Behavior> behaviors
    ) {
        this.method = method;
        this.routeName = routeName.startsWith("/") ? routeName : "/" + routeName;
        this.responseCode = responseCode;
        this.behaviors = behaviors;
    }

    public Route andRejectWith(ResponseCodes code) {
        return new Route(method, routeName, code, behaviors);
    }

    public Route andRedirectTo(String uri) {
        return new Route(method, routeName, ResponseCodes.FOUND, new StaticBody(uri));
    }

    public Route with(Behavior... pages) {
        return new Route(method, routeName, pages);
    }

    public String getContent(Request request) {
        return behaviors
            .stream()
            .map((page) -> page.apply(request).getStringBody())
            .collect(Collectors.joining());
    }

    public String getContent() {
        return getContent(new Request());
    }

    public boolean hasMethod(String method) {
        return this.method.equals(method);
    }

    public boolean hasResponder(String routeContents) {
        return behaviors
            .stream()
            .map((page) -> page.apply(new Request()).getStringBody())
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
