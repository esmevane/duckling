package duckling.responses;

import duckling.Server;
import duckling.behaviors.Behavior;
import duckling.requests.Request;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class Response {
    public ResponseCodes responseCode;

    Request request = new Request();
    ResponseBody responseBody = new ResponseBody("");
    HashMap<CommonHeaders, String> headers = new HashMap<>();
    ArrayList<Behavior> behaviors = new ArrayList<>();

    public Response(Request request) {
        this.request = request;
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

    public HashMap<CommonHeaders, String> getHeaders() {
        return compose().headers;
    }

    public ArrayList<String> getHeaderList() {
        ArrayList<String> list = new ArrayList<>();

        getHeaders().forEach((key, value) -> list.add(key + ": " + value + Server.CRLF));

        list.sort(String::compareTo);

        return list;
    }

    public InputStream getBody() {
        return new ByteArrayInputStream(responseBody.getBytes());
    }

    public String getStringBody() {
        return responseBody.wasEmptied() ? "" : responseBody.body;
    }

    public ArrayList<String> getResponseHeaders() {
        ArrayList<String> lines = new ArrayList<>();

        lines.add("HTTP/1.0 " + compose().getResponseCodeWithDefault() + Server.CRLF);
        lines.addAll(getHeaderList());
        lines.add(Server.CRLF);

        return lines;
    }

    public ResponseCodes getResponseCodeWithDefault() {
        return Optional
            .ofNullable(responseCode)
            .map(Function.identity())
            .orElseGet(() -> ResponseCodes.OK);
    }

    private Response setBody(String body) {
        if (body == null) {
            this.responseBody = new ResponseBody(null, true);
        } else {
            this.responseBody = new ResponseBody(body);
        }

        return this;
    }

    private Response setHeader(CommonHeaders key, String content) {
        if (content == null) {
            headers.remove(key);
        } else {
            headers.put(key, content);
        }

        return this;
    }

    private Response setHeaders(HashMap<CommonHeaders, String> headers) {
        this.headers = headers;

        return this;
    }

    public Response setResponseCode(ResponseCodes responseCode) {
        this.responseCode = responseCode;

        return this;
    }

    public Response setRequest(Request request) {
        this.request = request;

        return this;
    }

    private Response toCopy() {
        Response response = Response.wrap(request);

        response.setResponseCode(responseCode);
        response.setBody(responseBody.body);
        response.setHeaders(headers);

        behaviors.forEach(response::bind);

        return response;
    }

    public Response withBehaviors(ArrayList<Behavior> behaviors) {
        Response response = toCopy();
        behaviors.forEach(response::bind);
        return response;
    }

    public Response withContentType(String type) {
        return withHeader(CommonHeaders.CONTENT_TYPE, type);
    }

    public Response withResponseCode(ResponseCodes code) {
        return toCopy().setResponseCode(code);
    }

    public Response withHeader(CommonHeaders header, String content) {
        return toCopy().setHeader(header, content);
    }

    public Response withHeaders(HashMap<CommonHeaders, String> headers) {
        return toCopy().setHeaders(headers);
    }

    public Response withBody(String body) {
        return toCopy().setBody(body);
    }

    public Response withoutBody() {
        return toCopy().setBody(null);
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
