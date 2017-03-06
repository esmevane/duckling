package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MaybeBehaviorTest {
    @Test
    public void applyAddsGivenBehaviorWhenConditionIsTrue() throws Exception {
        String body = "Yay!";
        Behavior behavior = new MaybeBehavior(
            (request) -> true,
            (request) -> Response.wrap(request).withBody(body)
        );

        assertThat(behavior.apply(new Request()).getStringBody(), is(body));
    }

    @Test
    public void applyDoesNotAddGivenBehaviorWhenConditionIsFalse() throws Exception {
        String body = "Yay!";
        Behavior behavior = new MaybeBehavior(
            (request) -> false,
            (request) -> Response.wrap(request).withBody(body)
        );

        assertThat(behavior.apply(new Request()).getStringBody(), is(""));
    }
}
