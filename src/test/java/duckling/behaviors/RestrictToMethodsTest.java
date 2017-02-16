package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RestrictToMethodsTest {
    @Test
    public void deniesRequestsForRestrictedMethods() throws Exception {
        ArrayList<String> methods = new ArrayList<>();
        methods.add("POST");

        Behavior behavior = new RestrictToMethods(methods);
        Response subject = behavior.apply(new Request());

        assertThat(subject.compose().responseCode, is(ResponseCodes.METHOD_NOT_ALLOWED));
    }

    @Test
    public void permitsRequestsForAllowedMethods() throws Exception {
        ArrayList<String> methods = new ArrayList<>();
        Request request = new Request();

        request.add("POST / HTTP/1.1");
        methods.add("POST");

        Behavior behavior = new RestrictToMethods(methods);
        Response subject = behavior.apply(request);

        assertThat(subject.getResponseCodeWithDefault(), is(ResponseCodes.OK));
    }
}
