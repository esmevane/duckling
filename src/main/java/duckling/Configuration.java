package duckling;

import duckling.errors.BadArgumentsError;
import duckling.routing.RouteDefinitions;
import duckling.routing.Routes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class Configuration {
    public static final int DEFAULT_PORT = 5000;
    public static final String DEFAULT_ROOT = ".";
    public static final RouteDefinitions DEFAULT_ROUTES = new RouteDefinitions(
        Routes.get("/coffee").with("I'm a teapot").andRejectWith(418),
        Routes.get("/tea").with("Tea indeed")
    );

    public int port;
    public String root;
    public RouteDefinitions routes;

    public Configuration() {
        this(DEFAULT_PORT, DEFAULT_ROOT);
    }

    public Configuration(String root) {
        this(DEFAULT_PORT, root, DEFAULT_ROUTES);
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
            this.routes = getRoutes(arguments);
        } catch (Exception exception) {
            throw new BadArgumentsError();
        }
    }

    private RouteDefinitions getRoutes(String[] arguments) throws Exception {
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

