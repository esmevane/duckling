package duckling;

import duckling.errors.BadArgumentsError;

import java.io.IOException;
import java.util.stream.Stream;

public class Main {

    private Configuration config;
    private Server server;

    public Main(Configuration config) throws IOException {
        this.config = config;
        this.server = new Server(config.port, config.root);
    }

    public void start() throws IOException {
        Stream<String> output = Stream.of(
            String.format("Port: %s", this.config.port),
            String.format("Root: %s", this.config.root)
        );

        output.forEach(System.out::println);

        this.server.listen();
    }

    public static void main(String[] arguments) throws IOException {
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
