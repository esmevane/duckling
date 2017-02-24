package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DisplayFileTest {
    @Test
    public void apply() throws Exception {
        Request request = new Request();
        Behavior behavior = new DisplayFile();

        request.add("GET /file.txt HTTP/1.1");

        Response subject = behavior.apply(request);

        assertThat(subject.responseBody.isStream(), is(true));
    }

}