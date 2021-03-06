package duckling;

import duckling.behaviors.*;
import duckling.errors.BadArgumentsError;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;
import duckling.routing.RouteDefinitions;
import duckling.routing.Routes;

import java.util.Arrays;

public class Configuration {
    private static final int DEFAULT_PORT = 5000;
    private static final String DEFAULT_ROOT = ".";
    public static final RouteDefinitions DEFAULT_ROUTES = new RouteDefinitions(
        Routes.put("/method_options").with(new StaticBody("")),
        Routes.post("/method_options").with(new StaticBody("")),
        Routes.get("/method_options").with(new StaticBody("")),
        Routes.get("/method_options2").with(new StaticBody("")),
        Routes.put("/form").with(new StoreMemoryWithPath()),
        Routes.post("/form").with(new StoreMemoryWithPath()),
        Routes.get("/form").with(new RetrieveMemoryWithPath()),
        Routes.delete("/form").with(new EraseMemoryWithPath()),
        Routes.get("/redirect").with(new RedirectTo("/")),
        Routes.get("/parameters").with(new ParamEcho()),
        Routes.get("/cookie").with(
            new SetCookieFromParam("type"),
            new StaticBody("Eat")
        ),
        Routes
            .get("/logs")
            .with(
                new HasBasicAuth("admin", "hunter2"),
                new RetrieveMemoryWithPath(),
                (request) -> Response.wrap(request).withContentType("text/plain")
            ),
        Routes.get("/eat_cookie").with(new YummyTastyCookie("type")),
        Routes
            .get("/coffee")
            .with(
                new StaticBody("I'm a teapot"),
                new RespondWith(ResponseCodes.TEAPOT)
            ),
        Routes.get("/tea").with(new StaticBody("Tea indeed"))
    );

    public int port;
    public String root;
    public RouteDefinitions routes;

    public Configuration() {
        this(DEFAULT_PORT, DEFAULT_ROOT);
    }

    public Configuration(RouteDefinitions routes) {
        this(DEFAULT_PORT, DEFAULT_ROOT, routes);
    }

    public Configuration(int port, String root) {
        this(port, root, DEFAULT_ROUTES);
    }

    private Configuration(int port, String root, RouteDefinitions routes) {
        this.root = root;
        this.port = port;
        this.routes = routes;
    }

    public Configuration(String[] arguments) throws BadArgumentsError {
        try {
            this.port = getPort(arguments);
            this.root = getRoot(arguments);
            this.routes = getRoutes();
        } catch (Exception exception) {
            throw new BadArgumentsError();
        }
    }

    private RouteDefinitions getRoutes() throws Exception {
        return DEFAULT_ROUTES;
    }

    private int getPort(String[] arguments) {
        int index = Arrays.asList(arguments).indexOf("-p");

        if (index > -1) {
            return Integer.parseInt(arguments[index + 1]);
        } else {
            return DEFAULT_PORT;
        }
    }

    private String getRoot(String[] arguments) {
        int index = Arrays.asList(arguments).indexOf("-d");

        if (index > -1) {
            return arguments[index + 1];
        } else {
            return DEFAULT_ROOT;
        }
    }
}

