package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class YummyTastyCookieTest {
    @Test
    public void whatDoesThisTasteLikeToYou() throws Exception {
        Response response = new YummyTastyCookie("type").apply(new Request());
        String expectation = "Doesn't taste like anything to me.";

        assertThat(response.getStringBody(), is(expectation));
    }

    @Test
    public void reportsFlavorWhenGivenOne() throws Exception {
        Request request = new Request();
        String key = "flavor";
        String value = "vanilla";
        Behavior behavior = new YummyTastyCookie(key);

        request.add(
            "GET / HTTP/1.1",
            "Cookie: " + key + "=" + value
        );

        Response subject = behavior.apply(request);

        assertThat(subject.getStringBody(), is("mmmm " + value));
    }

    @Test
    public void ignoresBadValues() throws Exception {
        Request request = new Request();
        String key = "flavor";
        Behavior behavior = new YummyTastyCookie(key);
        String expectation = "Doesn't taste like anything to me.";

        request.add(
            "GET / HTTP/1.1",
            "Cookie: " + key + "="
        );

        Response subject = behavior.apply(request);

        assertThat(subject.getStringBody(), is(expectation));
    }

    @Test
    public void ignoresBadKeys() throws Exception {
        Request request = new Request();
        Behavior behavior = new YummyTastyCookie("flavor");
        String expectation = "Doesn't taste like anything to me.";

        request.add(
            "GET / HTTP/1.1",
            "Cookie: type=vanilla"
        );

        Response subject = behavior.apply(request);

        assertThat(subject.getStringBody(), is(expectation));
    }

}
