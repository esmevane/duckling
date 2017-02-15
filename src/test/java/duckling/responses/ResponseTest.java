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
    public void isInequalWithDifferentHeaders() {
        Request request = new Request();

        request.add("GET /path HTTP/1.1");

        Response response = Response.wrap(request);
        Response subject = Response.wrap(response).withHeader(CommonHeaders.LOCATION, "/");

        assertThat(subject, is(not(response)));
    }

    @Test
    public void isInequalWithDifferentRequests() {
        Request request = new Request();

        request.add("GET /path HTTP/1.1");

        Response response = Response.wrap(request);
        Response subject = Response.wrap(new Request());

        assertThat(subject, is(not(response)));
    }

    @Test
    public void isInequalWithDifferentResponseCodes() {
        Request request = new Request();

        request.add("GET /path HTTP/1.1");

        Response response = Response.wrap(request);
        Response subject = Response.wrap(response).respondWith(ResponseCodes.NOT_FOUND);

        assertThat(subject, is(not(response)));
    }

    @Test
    public void isInequalWithDifferentBodies() {
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

        subject.bind((Request givenRequest) ->
            Response.wrap(givenRequest).respondWith(ResponseCodes.NOT_FOUND)
        );

        expectation.setResponseCode(ResponseCodes.NOT_FOUND);

        assertThat(subject.getResponseCode(), is(expectation.getResponseCode()));
    }

    @Test
    public void remembersResponseCodes() {
        Response response = Response.wrap(new Request()).respondWith(ResponseCodes.NOT_FOUND);

        response.bind(Response::wrap);
        response.bind(Response::wrap);

        assertThat(response.getResponseCode(), is(ResponseCodes.NOT_FOUND));
    }

    @Test
    public void canBeGivenContentTypeHeader() {
        String contentType = "text/html";
        HashMap<CommonHeaders, String> expectation = new HashMap<>();
        HashMap<CommonHeaders, String> subject =
            Response
                .wrap(new Request())
                .contentType(contentType)
                .getHeaders();

        expectation.put(CommonHeaders.CONTENT_TYPE, contentType);

        assertThat(subject, is(expectation));
    }

    @Test
    public void mergesBodies() {
        Response subject = Response.wrap(new Request()).withBody("Hey,");
        Response other = Response.wrap(new Request()).withBody(" you!");

        assertThat(subject.merge(other).getStringBody(), is("Hey, you!"));
    }

    @Test
    public void canBuildABodyGradually() {
        Response response = Response.wrap(new Request());

        response.bind((request) -> Response.wrap(request).withBody("Hey,"));
        response.bind((request) -> Response.wrap(request).withBody(" you!"));
        response.bind(Response::wrap);

        assertThat(response.getStringBody(), is("Hey, you!"));
    }

    @Test
    public void canAbandonABodyInProgress() {
        Response response = Response.wrap(new Request());

        response.bind((request) -> Response.wrap(request).withBody("Hey,"));
        response.bind((request) -> Response.wrap(request).withBody(" you!"));
        response.bind((request) -> Response.wrap(request).withoutBody());

        assertThat(response.getStringBody(), is(""));
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
