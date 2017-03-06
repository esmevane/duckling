package duckling.responses;

import duckling.behaviors.RespondWith;
import duckling.behaviors.StaticBody;
import duckling.requests.Request;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MergeResponseTest {

    @Test
    public void replacesResponseBody() {
        Response original = Response.wrap(new Request()).withBody("Hey,");
        Response other = Response.wrap(new Request()).withBody(" you!");
        MergeResponse merge = new MergeResponse(original);

        assertThat(merge.apply(other).compose().getStringBody(), is(" you!"));
    }

    @Test
    public void replacesResponseCode() {
        Response original = Response.wrap(new Request()).withResponseCode(ResponseCodes.NOT_FOUND);
        Response other = Response.wrap(new Request()).withResponseCode(ResponseCodes.FOUND);
        MergeResponse merge = new MergeResponse(original);

        assertThat(merge.apply(other).compose().responseCode, is(ResponseCodes.FOUND));
    }

    @Test
    public void combinesHeaders() {
        HashMap<CommonHeaders, String> expectation = new HashMap<>();
        Response original = Response.wrap(new Request()).withHeader(CommonHeaders.LOCATION, "/here");
        Response other = Response.wrap(new Request()).withHeader(CommonHeaders.ALLOW, "GET");
        MergeResponse merge = new MergeResponse(original);

        expectation.put(CommonHeaders.LOCATION, "/here");
        expectation.put(CommonHeaders.ALLOW, "GET");

        assertThat(
            merge.apply(other).getHeaders(),
            is(expectation)
        );
    }

    @Test
    public void replacesRepeatedHeaders() {
        HashMap<CommonHeaders, String> expectation = new HashMap<>();
        Response original = Response.wrap(new Request()).withHeader(CommonHeaders.LOCATION, "/here");
        Response other = Response.wrap(new Request()).withHeader(CommonHeaders.LOCATION, "/there");
        MergeResponse merge = new MergeResponse(original);

        expectation.put(CommonHeaders.LOCATION, "/there");

        assertThat(
            merge.apply(other).getHeaders(),
            is(expectation)
        );
    }

    @Test
    public void combinesBehaviors() {
        Response original = Response.wrap(new Request());
        Response other = Response.wrap(new Request());

        original.bind(new StaticBody("Hey hey."));
        other.bind(new RespondWith(ResponseCodes.NOT_FOUND));

        MergeResponse merge = new MergeResponse(original);

        assertThat(
            merge.apply(other).behaviors.size(),
            is(2)
        );
    }

    @Test
    public void combinesResponseBodyFilters() {
        Response original = Response.wrap(new Request());
        Response other = Response.wrap(new Request());

        original.withResponseBodyFilter(Response::wrap);
        other.withResponseBodyFilter(Response::wrap);

        MergeResponse merge = new MergeResponse(original);

        assertThat(
            merge.apply(other).responseBodyFilters.size(),
            is(2)
        );
    }

    @Test
    public void replacesRequests() {
        Request originalRequest = new Request();
        Request otherRequest = new Request();

        originalRequest.add("GET / HTTP/1.1");
        otherRequest.add("GET /different-path HTTP/1.1");

        Response original = Response.wrap(originalRequest);
        Response other = Response.wrap(otherRequest);
        MergeResponse merge = new MergeResponse(original);

        assertThat(
            merge.apply(other).request,
            is(otherRequest)
        );
    }

}
