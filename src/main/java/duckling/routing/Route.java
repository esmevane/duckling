package duckling.routing;

import duckling.behaviors.Behavior;
import duckling.behaviors.StaticBody;
import duckling.requests.Request;

import java.util.ArrayList;
import java.util.Collections;

public class Route {
    private String routeName;
    private String method;

    private ArrayList<Behavior> behaviors;

    public Route() {
        this("GET", "/");
    }

    public Route(String method, String routeName) {
        this(method, routeName, new StaticBody("not-found"));
    }

    private Route(
        String method,
        String routeName,
        Behavior... behaviors
    ) {
        this(method, routeName, new ArrayList<>());

        Collections.addAll(this.behaviors, behaviors);
    }

    private Route(
        String method,
        String routeName,
        ArrayList<Behavior> behaviors
    ) {
        this.method = method;
        this.routeName = routeName.startsWith("/") ? routeName : "/" + routeName;
        this.behaviors = behaviors;
    }

    public Route with(Behavior... pages) {
        return new Route(method, routeName, pages);
    }

    public String getMethod() {
        return this.method;
    }

    public boolean hasMethod(String method) {
        return this.method.equals(method);
    }

    public boolean hasRoute(String route) {
        return this.routeName.equals(route);
    }

    public ArrayList<Behavior> getBehaviors() {
        return behaviors;
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
        return method + " " + routeName;
    }

}
