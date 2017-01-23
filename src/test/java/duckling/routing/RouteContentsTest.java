package duckling.routing;

import duckling.requests.Request;
import org.junit.Test;

import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class RouteContentsTest {
    @Test
    public void getReturnsGivenString() throws Exception {
        String body = "Heyyyy youuu guyyyysss";
        RouteContents contents = new RouteContents(body);

        assertThat(
            contents.get(),
            is(body)
        );
    }

    @Test
    public void getReturnsContentsOfGivenLambda() throws Exception {
        String body = "Heyyyy youuu guyyyysss";
        Function<Request, String> lambda = (Request request) -> body;
        RouteContents contents = new RouteContents(lambda);

        assertThat(
            contents.get(),
            is(body)
        );
    }

    @Test
    public void twoMatchingBodiesAreEquivalentContents() throws Exception {
        String body = "Heyyyy youuu guyyyysss";
        Function<Request, String> lambda = (Request request) -> body;
        RouteContents closureContents = new RouteContents(lambda);
        RouteContents stringContents = new RouteContents(body);

        assertThat(
            closureContents,
            is(stringContents)
        );
    }

    @Test
    public void twoMismatchedContentsAreNotEquivalent() throws Exception {
        String body = "Heyyyy youuu guyyyysss";
        Function<Request, String> lambda = (Request request) -> "Baby ruth!";
        RouteContents closureContents = new RouteContents(lambda);
        RouteContents stringContents = new RouteContents(body);

        assertThat(
            closureContents,
            is(not(stringContents))
        );
    }

}
