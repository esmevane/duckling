package duckling.routing;

import duckling.requests.Request;

public class Routes {
    public static Route get() {
        return get("/");
    }

    public static Route get(String routeName) {
        return new Route("GET", routeName);
    }

    public static Route post() {
        return post("/");
    }

    public static Route post(String routeName) {
        return new Route("POST", routeName);
    }

    public static Route put() {
        return put("/");
    }

    public static Route put(String routeName) {
        return new Route("PUT", routeName);
    }

    public static Route patch() {
        return patch("/");
    }

    public static Route patch(String routeName) {
        return new Route("PATCH", routeName);
    }

    public static Route delete() {
        return delete("/");
    }

    public static Route delete(String routeName) {
        return new Route("DELETE", routeName);
    }

    public static Route fromRequest(Request request) {
        return new Route(request.getMethod(), request.getPath());
    }

}
