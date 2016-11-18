package duckling;

public class Logger {
    public void info(String... messages) {
        for (String message : messages) System.out.println(message);
    }
}
