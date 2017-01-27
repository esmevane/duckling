package duckling.requests;

import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueryParamsTest {
    @Test
    public void applyReturnsStringOfDecodedParameters() throws Exception {
        String encoded = "/parameters?variable_1=Operators%20%3C%2C%20%" +
            "3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C%20%26%2C%2" +
            "0%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that" +
            "%20all%22%3F&variable_2=stuff";

        Request request = new Request();
        QueryParams extractor = new QueryParams();

        request.add("GET /" + encoded + " HTTP/1.1");

        HashMap<String, String> expectation = new HashMap<>();

        expectation.put(
            "variable_1",
            "Operators <, >, =, !=; +, -, *, &, @, #, $, [, ]: \"is that all\"?"
        );

        expectation.put("variable_2", "stuff");

        assertThat(extractor.apply(request), is(expectation));
    }

}
