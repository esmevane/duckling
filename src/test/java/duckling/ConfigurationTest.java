package duckling;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import duckling.errors.BadArgumentsError;
import duckling.routing.RouteDefinitions;
import duckling.routing.Routes;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
        new Configuration(arguments);
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

        assertEquals(config.port, 5000);
    }

    @Test(expected = BadArgumentsError.class)
    public void withNoPort() throws Exception {
        String[] arguments = {"-p"};
        new Configuration(arguments);
    }

    @Test(expected = BadArgumentsError.class)
    public void withMalformedPort() throws Exception {
        String[] arguments = {"-p", "unparseable"};
        new Configuration(arguments);
    }

    @Test
    public void hasDefaultRoutes() throws Exception {
        String[] arguments = {};
        Configuration config = new Configuration(arguments);

        assertThat(config.routes, is(Configuration.DEFAULT_ROUTES));
    }
}
