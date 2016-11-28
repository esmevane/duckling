package duckling.routing;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RouteDefinitionsTest {

    @Test
    public void matchesDefinitionObjectsWithSameContents() {
        RouteDefinitions definitions = new RouteDefinitions(Routes.get("coffee"));
        RouteDefinitions other = new RouteDefinitions(Routes.get("coffee"));
        assertThat(definitions, is(other));
    }

}
