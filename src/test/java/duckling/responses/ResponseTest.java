package duckling.responses;

import duckling.Server;
import duckling.requests.Request;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ResponseTest {
    @Test
    public void wrapsARequestOrResponse() {
        Request request = new Request();

        request.add("GET /path HTTP/1.1");

        Response response = Response.wrap(request);
        Response subject = Response.wrap(response);

        assertThat(subject, is(response));
    }

    @Test
    public void isUnequalWithDifferentHeaders() {
        Request request = new Request();

        request.add("GET /path HTTP/1.1");

        Response response = Response.wrap(request);
        Response subject = Response.wrap(response).withHeader(CommonHeaders.LOCATION, "/");

        assertThat(subject, is(not(response)));
    }

    @Test
    public void isUnequalWithDifferentRequests() {
        Request request = new Request();

        request.add("GET /path HTTP/1.1");

        Response response = Response.wrap(request);
        Response subject = Response.wrap(new Request());

        assertThat(subject, is(not(response)));
    }

    @Test
    public void isUnequalWithDifferentResponseCodes() {
        Request request = new Request();

        request.add("GET /path HTTP/1.1");

        Response response = Response.wrap(request);
        Response subject = Response.wrap(response).withResponseCode(ResponseCodes.NOT_FOUND);

        assertThat(subject, is(not(response)));
    }

    @Test
    public void isUnequalWithDifferentBodies() {
        Request request = new Request();

        request.add("GET /path HTTP/1.1");

        Response response = Response.wrap(request);
        Response subject = Response.wrap(response).withBody("Hey, you!");

        assertThat(subject, is(not(response)));
    }

    @Test
    public void canBeGivenFunctionsToBindItsValuesTo() {
        Request request = new Request();

        request.add("GET /path HTTP/1.1");

        Response subject = Response.wrap(request);
        Response expectation = Response.wrap(request);

        subject.bind(
            (givenRequest) ->
                Response
                    .wrap(givenRequest)
                    .withResponseCode(ResponseCodes.NOT_FOUND)
        );

        expectation.setResponseCode(ResponseCodes.NOT_FOUND);

        assertThat(subject.compose().responseCode, is(expectation.compose().responseCode));
    }

    @Test
    public void remembersResponseCodes() {
        Response response = Response.wrap(new Request()).withResponseCode(ResponseCodes.NOT_FOUND);

        response.bind(Response::wrap);
        response.bind(Response::wrap);

        assertThat(response.compose().responseCode, is(ResponseCodes.NOT_FOUND));
    }

    @Test
    public void canBeGivenContentTypeHeader() {
        String contentType = "text/html";
        HashMap<CommonHeaders, String> expectation = new HashMap<>();
        HashMap<CommonHeaders, String> subject =
            Response
                .wrap(new Request())
                .withContentType(contentType)
                .getHeaders();

        expectation.put(CommonHeaders.CONTENT_TYPE, contentType);

        assertThat(subject, is(expectation));
    }

    @Test
    public void canAbandonABodyInProgress() {
        Response response = Response.wrap(new Request());

        response.bind((request) -> Response.wrap(request).withBody("Hey, you!"));
        response.bind((request) -> Response.wrap(request).withoutBody());

        assertThat(response.compose().getStringBody(), is(""));
    }

    @Test
    public void canBuildHeaders() {
        HashMap<CommonHeaders, String> expectation = new HashMap<>();
        Response response =
            Response
                .wrap(new Request())
                .withHeader(CommonHeaders.CONTENT_TYPE, "application/json")
                .withHeader(CommonHeaders.LOCATION, "/");

        expectation.put(CommonHeaders.LOCATION, "/");
        expectation.put(CommonHeaders.CONTENT_TYPE, "application/json");

        assertThat(response.getHeaders(), is(expectation));
    }

    @Test
    public void canBuildHeadersGradually() {
        HashMap<CommonHeaders, String> expectation = new HashMap<>();
        Response response = Response.wrap(new Request());

        response.bind(
            (request) ->
                Response
                    .wrap(request)
                    .withHeader(CommonHeaders.CONTENT_TYPE, "application/json")
        );

        response.bind(
            (request) ->
                Response
                    .wrap(request)
                    .withHeader(CommonHeaders.LOCATION, "/")
        );

        response.bind(Response::wrap);

        expectation.put(CommonHeaders.LOCATION, "/");
        expectation.put(CommonHeaders.CONTENT_TYPE, "application/json");

        assertThat(response.getHeaders(), is(expectation));
    }

    @Test
    public void createsHeaderList() {
        ArrayList<String> expectation = new ArrayList<>();

        Response response =
            Response
                .wrap(new Request())
                .withHeader(CommonHeaders.CONTENT_TYPE, "application/json")
                .withHeader(CommonHeaders.LOCATION, "/");

        expectation.add(CommonHeaders.CONTENT_TYPE + ": application/json" + Server.CRLF);
        expectation.add(CommonHeaders.LOCATION + ": /" + Server.CRLF);

        assertThat(response.getHeaderList(), equalTo(expectation));
    }


}
