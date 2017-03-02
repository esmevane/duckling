package duckling;

import duckling.errors.BadArgumentsError;

import java.io.IOException;

class Main {
    private Configuration config;
    private Server server;

    private Main(Configuration config) throws IOException {
        this.config = config;
        this.server = new Server(config);
    }

    private void start() throws IOException {
        System.out.println(String.format("Port: %s", this.config.port));
        System.out.println(String.format("Root: %s", this.config.root));

        try {
            this.server.listen();
        } finally {
            System.exit(1);
        }
    }

    public static void main(String[] arguments) throws IOException {
        try {
            Configuration config = new Configuration(arguments);
            Main main = new Main(config);

            main.start();
        } catch (BadArgumentsError exception) {
            System.out.println("Usage: -p <port> -d <root directory>");
        } finally {
            System.exit(1);
        }
    }

}
