package duckling;

import java.util.Arrays;

public class Configuration {
    public int port;
    public String root;

    public Configuration(String[] arguments) throws BadArgumentsError {
        try {
            this.port = getPort(arguments);
            this.root = getRoot(arguments);
        } catch (Exception exception) {
            throw new BadArgumentsError();
        }
    }

    private int getPort(String[] arguments) {
        int index = Arrays.asList(arguments).indexOf("-p");

        if (index > -1) {
            return Integer.parseInt(arguments[index + 1]);
        } else {
            return 80;
        }
    }

    private String getRoot(String[] arguments) {
        int index = Arrays.asList(arguments).indexOf("-d");

        if (index > -1) {
            return arguments[index + 1];
        } else {
            return "/var/www/duckling/public";
        }
    }
}

