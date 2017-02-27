package duckling.routing;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RouteDefinitionsTest {

    @Test
    public void matchesDefinitionObjectsWithSameContents() {
        RouteDefinitions definitions = new RouteDefinitions(Routes.get("/coffee"));
        RouteDefinitions other = new RouteDefinitions(Routes.get("/coffee"));
        assertThat(definitions, is(other));
    }

    @Test
    public void hasPathIsTrueWhenPathDefined() {
        RouteDefinitions definitions = new RouteDefinitions(Routes.get("/coffee"));
        assertThat(definitions.hasPath("/coffee"), is(true));
    }

    @Test
    public void hasPathIsFalseWhenPathNotDefined() {
        RouteDefinitions definitions = new RouteDefinitions(Routes.get("/coffee"));
        assertThat(definitions.hasPath("/tea"), is(false));
    }

    @Test
    public void allMethodsForRoute() {
        RouteDefinitions definitions = new RouteDefinitions(
            Routes.post("/coffee"),
            Routes.get("/coffee")
        );

        ArrayList<String> expectation = new ArrayList<>(
            Arrays.asList("POST", "GET", "HEAD", "OPTIONS")
        );

        expectation.sort(Comparator.comparing(String::toString));

        assertThat(
            definitions.allMethodsForRoute("/coffee"),
            is(expectation)
        );
    }

}
