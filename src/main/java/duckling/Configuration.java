package duckling;

import duckling.pages.*;
import duckling.errors.BadArgumentsError;
import duckling.responses.ResponseCodes;
import duckling.routing.RouteDefinitions;
import duckling.routing.Routes;

import java.util.Arrays;

public class Configuration {
    public static final int DEFAULT_PORT = 5000;
    public static final String DEFAULT_ROOT = ".";
    public static final RouteDefinitions DEFAULT_ROUTES = new RouteDefinitions(
        Routes.put("/form").with(new StoreMemory()),
        Routes.post("/form").with(new StoreMemory()),
        Routes.get("/form").with(new RetrieveMemory()),
        Routes.delete("/form").with(new EraseMemory()),
        Routes.get("/redirect").andRedirectTo("/"),
        Routes.get("/parameters").with(new ParamEcho()),
        Routes.get("/coffee").with(new StaticBody("I'm a teapot"))
            .andRejectWith(ResponseCodes.TEAPOT),
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

    public Configuration(int port, String root, RouteDefinitions routes) {
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

