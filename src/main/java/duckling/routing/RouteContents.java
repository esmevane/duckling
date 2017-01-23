package duckling.routing;

import duckling.requests.Request;

import java.util.function.Function;

public class RouteContents {
    private Request nullRequest = new Request();
    private Function<Request, String> contents;

    public RouteContents(String contents) {
        this((request) -> contents);
    }

    public RouteContents(Function<Request, String> getContents) {
        this.contents = getContents;
    }

    public String get(Request request) {
        return this.contents.apply(request);
    }

    public String get() {
        return get(this.nullRequest);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof RouteContents) {
            return equals((RouteContents) object);
        } else if (object instanceof String) {
            return equals((String) object);
        }

        return false;
    }

    public boolean equals(RouteContents other) {
        return toString().equals(other.toString());
    }

    public boolean equals(String contents) {
        return toString().equals(contents);
    }

    @Override
    public String toString() {
        return this.contents.apply(this.nullRequest);
    }

}
