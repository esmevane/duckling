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
    String body = "";
    Request request;
    public ResponseCodes responseCode;
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

    public String getStringBody() {
        return compose().body;
    }

    public InputStream getBody() {
        return new ByteArrayInputStream(getStringBody().getBytes());
    }

    public ArrayList<String> getResponseHeaders() {
        ArrayList<String> lines = new ArrayList<>();

        lines.add("HTTP/1.0 " + getResponseCodeWithDefault() + Server.CRLF);
        lines.addAll(getHeaderList());
        lines.add(Server.CRLF);

        return lines;
    }

    public ResponseCodes getResponseCode() {
        return compose().responseCode;
    }

    public Request getRequest() {
        return request;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setHeader(CommonHeaders key, String content) {
        if (content == null) {
            headers.remove(key);
        } else {
            headers.put(key, content);
        }
    }

    public void setHeaders(HashMap<CommonHeaders,String> headers) {
        this.headers = headers;
    }

    public void setResponseCode(ResponseCodes responseCode) {
        this.responseCode = responseCode;
    }

    private Response toCopy() {
        Response response = Response.wrap(getRequest());

        response.setResponseCode(getResponseCode());
        response.setBody(getStringBody());
        response.setHeaders(getHeaders());

        return response;
    }

    public Response contentType(String type) {
        return withHeader(CommonHeaders.CONTENT_TYPE, type);
    }

    public Response respondWith(ResponseCodes code) {
        Response response = toCopy();
        response.setResponseCode(code);
        return response;
    }

    public Response withHeader(CommonHeaders header, String content) {
        Response response = toCopy();
        response.setHeader(header, content);
        return response;
    }

    public Response withHeaders(HashMap<CommonHeaders, String> headers) {
        Response response = toCopy();
        response.setHeaders(headers);
        return response;
    }

    public Response withBody(String body) {
        Response response = toCopy();
        response.setBody(body);
        return response;
    }

    public Response withoutBody() {
        Response response = toCopy();
        response.setBody(null);
        return response;
    }

    public Response merge(Response other) {
        String newBody = other.getStringBody() == null ? "" : body + other.getStringBody();
        ResponseCodes code = other.responseCode == null ? responseCode : other.getResponseCode();
        HashMap<CommonHeaders, String> newHeaders = new HashMap<>(headers);

        other.getHeaders().forEach(newHeaders::put);

        return Response
            .wrap(other.getRequest())
            .respondWith(code)
            .withBody(newBody)
            .withHeaders(newHeaders);
    }

    private Response compose() {
        return behaviors
            .stream()
            .map((behavior) -> behavior.apply(request))
            .reduce(this, Response::merge);
    }

    public ResponseCodes getResponseCodeWithDefault() {
        return Optional
            .ofNullable(getResponseCode())
            .map(Function.identity())
            .orElseGet(() -> ResponseCodes.OK);
    }

    @Override
    public int hashCode() {
        return this.request.hashCode() +
            this.headers.hashCode() +
            this.responseCode.hashCode() +
            this.body.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Response) {
            Response other = (Response) object;

            boolean sameRequest = this.request.equals(other.getRequest());
            boolean sameHeaders = this.headers.equals(other.getHeaders());
            boolean sameBody = this.body.equals(other.getStringBody());

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

        builder.append(Server.CRLF).append(getStringBody()).append(Server.CRLF);

        return builder.toString();
    }

}
