package duckling;

public class Main {
    private Configuration config;
    private Server server;

    public Main(Configuration config) {
        this.config = config;
        this.server = new Server(config.port);
    }

    public void start() {
        beforeListen();
        this.server.listen();
    }

    private void beforeListen() {
        String[] output = {
                String.format("Port: %s", this.config.port),
                String.format("Root: %s", this.config.root)
        };

        for (String line: output) {
            System.out.println(line);
        }
    }

    public static void main(String[] arguments) {
        try {
            Configuration config = new Configuration(arguments);
            Main main = new Main(config);

            main.start();
        } catch (BadArgumentsError exception) {
            System.out.println("Usage: -p <port> -d <root directory>");
            System.exit(1);
        }
    }

}
