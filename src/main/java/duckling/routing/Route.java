package duckling.routing;

import duckling.requests.Request;
import duckling.responses.ResponseCode;

public class Route {
    protected String routeName;
    protected String method;
    private String responderKey;
    private ResponseCode responseCode;

    public Route() {
        this("GET", "/");
    }

    public Route(String method, String routeName) {
        this(method, routeName, "not-found");
    }

    public Route(String method, String routeName, String responderKey) {
        this(
            method,
            routeName,
            responderKey,
            ResponseCode.ok()
        );
    }

    public Route(
        String method,
        String routeName,
        String responderKey,
        ResponseCode responseCode
    ) {
        this.method = method;
        this.routeName = routeName.startsWith("/") ? routeName : "/" + routeName;
        this.responderKey = responderKey;
        this.responseCode = responseCode;
    }

    public Route with(String responderKey) {
        return new Route(method, routeName, responderKey);
    }

    public boolean hasMethod(String method) {
        return this.method.equals(method);
    }

    public boolean hasResponder(String responderKey) {
        return this.responderKey.equals(responderKey);
    }

    public boolean hasRoute(String route) {
        return this.routeName.equals(route);
    }

    public int hashCode() {
        return routeName.hashCode() + method.hashCode();
    }

    public boolean equals(Object other) {
        return equals((Route) other);
    }

    public boolean equals(Route other) {
        return routeName.equals(other.routeName) && method.equals(other.method);
    }

    public boolean matches(Request request) {
        return equals(Routes.fromRequest(request));
    }

    public Route andRejectWith(int code) {
        return new Route(method, routeName, responderKey, new ResponseCode(code));
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public String toString() {
        return method + " " + routeName + " - " + responderKey + " " + responseCode;
    }

    public String getContent() {
        return responderKey;
    }

}
