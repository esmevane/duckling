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

    public Optional<Route> getMatch(Request request) {
        return contents.stream().filter(route -> route.matches(request)).findFirst();
    }

    public boolean hasRoute(Request request) {
        return contents.stream().anyMatch(route -> route.matches(request));
    }

    @Override
    public int hashCode() {
        return contents.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof RouteDefinitions) {
            RouteDefinitions other = (RouteDefinitions) object;

            return this.contents.equals(other.contents);
        }

        return false;
    }

    @Override
    public String toString() {
        return contents
            .stream()
            .map(Route::toString)
            .collect(Collectors.joining(Server.CRLF));
    }

    public ArrayList<String> allMethodsForRoute(String route) {
        ArrayList<String> methods = new ArrayList<>();

        methods.add("HEAD");
        methods.add("OPTIONS");

        contents
            .stream()
            .filter(candidate -> candidate.hasRoute(route))
            .forEach(candidate -> methods.add(candidate.getMethod()));

        methods.sort(Comparator.comparing(String::toString));

        return methods;
    }

    public boolean hasPath(String path) {
        return contents
            .stream()
            .filter(candidate -> candidate.hasRoute(path))
            .count() > 0;
    }
}
