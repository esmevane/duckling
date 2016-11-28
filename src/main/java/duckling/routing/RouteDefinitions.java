package duckling.routing;

import duckling.Server;
import duckling.requests.Request;

import java.util.*;
import java.util.stream.Collectors;

public class RouteDefinitions {
    private List<Route> contents = new ArrayList<>();

    public RouteDefinitions(Route... routes) {
        Collections.addAll(contents, routes);
    }

    public int hashCode() {
        return contents.hashCode();
    }

    public boolean equals(Object other) {
        return equals((RouteDefinitions) other);
    }

    public boolean equals(RouteDefinitions other) {
        return contents.equals(other.contents);
    }

    public boolean hasRoute(Request request) {
        return contents.stream().anyMatch(route -> route.matches(request));
    }

    public Optional<Route> getMatch(Request request) {
        return contents.stream().filter(route -> route.matches(request)).findFirst();
    }

    public String toString() {
        return contents.stream().map(Route::toString).collect(Collectors.joining(Server.CRLF));
    }
}
