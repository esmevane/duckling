package duckling.responses;

import duckling.Server;
import duckling.behaviors.Behavior;
import duckling.requests.Request;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class Response {
    public ResponseCodes responseCode;
    public ResponseBody responseBody = new ResponseBody("");

    Request request = new Request();
    HashMap<CommonHeaders, String> headers = new HashMap<>();
    ArrayList<Behavior> behaviors = new ArrayList<>();
    ArrayList<ResponseBodyFilter> responseBodyFilters = new ArrayList<>();

    public Response(Request request) {
        this.request = request;
    }

    public Response(
        Request request,
        ArrayList<Behavior> behaviors,
        ArrayList<ResponseBodyFilter> responseBodyFilters,
        HashMap<CommonHeaders, String> headers,
        ResponseCodes responseCode,
        ResponseBody responseBody
    ) {
        this.behaviors = behaviors;
        this.headers = headers;
        this.request = request;
        this.responseBody = responseBody;
        this.responseBodyFilters = responseBodyFilters;
        this.responseCode = responseCode;
    }

    public static Response wrap(Request request) {
        return new Response(request);
    }

    public static Response wrap(Response response) {
        return new Response(new Request()).merge(response);
    }

    public Response bind(Behavior function) {
        behaviors.add(function);

        return this;
    }

    public InputStream getBody() {
        if (responseBodyFilters.size() > 0) {
            Response nextResponse =
                responseBodyFilters
                    .stream()
                    .map((responseBodyFilter) -> responseBodyFilter.apply(this))
                    .reduce(Response::merge)
                    .map(Function.identity())
                    .orElse(this);

            return nextResponse.responseBody.getContent();
        }

        return responseBody.getContent();
    }

    public String getStringBody() {
        return responseBody.wasEmptied() ? "" : responseBody.body;
    }

    public HashMap<CommonHeaders, String> getHeaders() {
        return headers;
    }

    public ArrayList<String> getHeaderList() {
        ArrayList<String> list = new ArrayList<>();

        getHeaders().forEach((key, value) -> list.add(key + ": " + value + Server.CRLF));

        list.sort(String::compareTo);

        return list;
    }

    public ResponseCodes getResponseCodeWithDefault() {
        return Optional
            .ofNullable(responseCode)
            .map(Function.identity())
            .orElseGet(() -> ResponseCodes.OK);
    }

    public ArrayList<String> getResponseHeaders() {
        ArrayList<String> lines = new ArrayList<>();

        lines.add("HTTP/1.0 " + getResponseCodeWithDefault() + Server.CRLF);
        lines.addAll(getHeaderList());
        lines.add(Server.CRLF);

        return lines;
    }

    public Response withBehaviors(ArrayList<Behavior> behaviors) {
        ResponseBuilder builder = getBuilder();

        behaviors.forEach(builder::addBehavior);

        return builder.toResponse();
    }

    public Response withBody(String body) {
        return getBuilder().setBody(body).toResponse();
    }

    public Response withoutBody() {
        return getBuilder().setBody(null).toResponse();
    }

    public Response withContentType(String type) {
        return withHeader(CommonHeaders.CONTENT_TYPE, type);
    }

    public Response withHeader(CommonHeaders header, String content) {
        return getBuilder().setHeader(header, content).toResponse();
    }

    public Response withResponseBodyFilter(ResponseBodyFilter responseBodyFilter) {
        return getBuilder().addResponseBodyFilter(responseBodyFilter).toResponse();
    }

    public Response withResponseCode(ResponseCodes code) {
        return getBuilder().setResponseCode(code).toResponse();
    }

    public Response withStreamBody(InputStream inputStream) {
        return getBuilder().setStreamBody(inputStream).toResponse();
    }

    public Response merge(Response other) {
        return new MergeResponse(this).apply(other);
    }

    public Response compose() {
        return behaviors
            .stream()
            .map((behavior) -> behavior.apply(request))
            .reduce(this, Response::merge);
    }

    private ResponseBuilder getBuilder() {
        return new ResponseBuilder()
            .setRequest(request)
            .setResponseCode(responseCode)
            .setResponseBody(responseBody)
            .setHeaders(headers)
            .setResponseBodyFilters(responseBodyFilters)
            .setBehaviors(behaviors);
    }

    @Override
    public int hashCode() {
        return this.request.hashCode() +
            this.headers.hashCode() +
            this.responseCode.hashCode() +
            this.responseBody.body.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Response) {
            Response other = (Response) object;

            boolean sameRequest = this.request.equals(other.request);
            boolean sameHeaders = this.headers.equals(other.headers);
            boolean sameBody = this.responseBody.body.equals(other.responseBody.body);

            boolean sameCode =
                this.getResponseCodeWithDefault().equals(
                    other.getResponseCodeWithDefault()
                );

            return sameRequest && sameHeaders && sameCode && sameBody;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        getResponseHeaders().forEach(builder::append);

        builder.append(Server.CRLF).append(responseBody.body).append(Server.CRLF);

        return builder.toString();
    }

}
