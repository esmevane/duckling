package duckling;

import static org.junit.Assert.assertEquals;

import duckling.errors.BadArgumentsError;
import org.junit.Test;

public class ConfigurationTest {
    @Test
    public void configuresRoot() throws Exception {
        String root = "/home/duckling";
        String[] arguments = {"-d", root};
        Configuration config = new Configuration(arguments);

        assertEquals(config.root, root);
    }

    @Test
    public void defaultRoot() throws Exception {
        String[] arguments = {};
        Configuration config = new Configuration(arguments);

        assertEquals(config.root, "/var/www/duckling/public");
    }

    @Test(expected = BadArgumentsError.class)
    public void withNoRoot() throws Exception {
        String[] arguments = {"-d"};
        Configuration config = new Configuration(arguments);
    }

    @Test
    public void configuresPort() throws Exception {
        String[] arguments = {"-p", "5000"};
        Configuration config = new Configuration(arguments);

        assertEquals(config.port, 5000);
    }

    @Test
    public void hasDefaultPort() throws Exception {
        String[] arguments = {};
        Configuration config = new Configuration(arguments);

        assertEquals(config.port, 80);
    }

    @Test(expected = BadArgumentsError.class)
    public void withNoPort() throws Exception {
        String[] arguments = {"-p"};
        Configuration config = new Configuration(arguments);
    }

    @Test(expected = BadArgumentsError.class)
    public void withMalformedPort() throws Exception {
        String[] arguments = {"-p", "unparseable"};
        Configuration config = new Configuration(arguments);
    }
}
