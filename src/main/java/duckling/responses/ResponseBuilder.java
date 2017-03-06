package duckling.responses;

import duckling.behaviors.Behavior;
import duckling.requests.Request;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ResponseBuilder {
    ResponseCodes responseCode;
    ResponseBody responseBody = new ResponseBody("");
    Request request = new Request();
    HashMap<CommonHeaders, String> headers = new HashMap<>();
    ArrayList<Behavior> behaviors = new ArrayList<>();
    ArrayList<ResponseBodyFilter> responseBodyFilters = new ArrayList<>();

    public ResponseBuilder addBehaviors(ArrayList<Behavior> behaviors) {
        behaviors.forEach(this.behaviors::add);

        return this;
    }

    public ResponseBuilder addResponseBodyFilters(ArrayList<ResponseBodyFilter> responseBodyFilters) {
        responseBodyFilters.forEach(this.responseBodyFilters::add);

        return this;
    }

    public ResponseBuilder addBehavior(Behavior behavior) {
        behaviors.add(behavior);

        return this;
    }

    public ResponseBuilder addResponseBodyFilter(ResponseBodyFilter responseBodyFilter) {
        responseBodyFilters.add(responseBodyFilter);

        return this;
    }

    public ResponseBuilder setBody(String body) {
        if (body == null) {
            this.responseBody = new ResponseBody(null, true);
        } else {
            this.responseBody = new ResponseBody(body);
        }

        return this;
    }

    public ResponseBuilder setHeader(CommonHeaders key, String content) {
        if (content == null) {
            headers.remove(key);
        } else {
            headers.put(key, content);
        }

        return this;
    }

    public ResponseBuilder setHeaders(HashMap<CommonHeaders, String> headers) {
        this.headers = headers;

        return this;
    }

    public ResponseBuilder setResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;

        return this;
    }

    public ResponseBuilder setResponseBodyFilters(ArrayList<ResponseBodyFilter> responseBodyFilters) {
        this.responseBodyFilters = responseBodyFilters;

        return this;
    }

    public ResponseBuilder setResponseCode(ResponseCodes responseCode) {
        this.responseCode = responseCode;

        return this;
    }

    public ResponseBuilder setRequest(Request request) {
        this.request = request;

        return this;
    }

    public ResponseBuilder setStreamBody(InputStream inputStream) {
        this.responseBody = new ResponseBody("", inputStream);

        return this;
    }

    public ResponseBuilder setBehaviors(ArrayList<Behavior> behaviors) {
        this.behaviors = behaviors;

        return this;
    }

    public Response toResponse() {
        return new Response(
            request,
            behaviors,
            responseBodyFilters,
            headers,
            responseCode,
            responseBody
        );
    }

}
