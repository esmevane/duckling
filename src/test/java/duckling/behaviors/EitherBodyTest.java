package duckling.behaviors;

import duckling.pages.StaticBody;
import duckling.requests.Request;
import duckling.responses.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EitherBodyTest {
    @Test
    public void returnsGivenBodyIfTrue() throws Exception {
        EitherBody behavior = new EitherBody(true, new StaticBody("Hey"));
        Response subject = behavior.apply(new Request());

        assertThat(subject.getStringBody(), is("Hey"));
    }

    @Test
    public void clearsBodyIfFalse() throws Exception {
        EitherBody behavior = new EitherBody(false, new StaticBody("Hey"));
        Response subject = behavior.apply(new Request());

        assertThat(subject.getStringBody(), is(equalTo(null)));
    }

}
