package duckling;

import duckling.errors.BadArgumentsError;

import java.io.IOException;

public class Main {
    public static Logger LOGGER = new Logger();

    private Configuration config;
    private Server server;
    private Logger logger;

    public Main(Configuration config) throws IOException {
        this.config = config;
        this.server = new Server(config);
        this.logger = LOGGER;
    }

    public void start() throws IOException {
        logger.info(
            String.format("Port: %s", this.config.port),
            String.format("Root: %s", this.config.root)
        );

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
            LOGGER.info("Usage: -p <port> -d <root directory>");
        } finally {
            System.exit(1);
        }
    }

}
