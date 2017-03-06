package duckling.responses;

import duckling.requests.Request;

import java.util.HashMap;

class MergeResponse {
    private Response response = new Response(new Request());

    public MergeResponse(Response original) {
        this.response = original;
    }

    public Response apply(Response other) {
        ResponseBody responseBody = response.responseBody.merge(other.responseBody);

        HashMap<CommonHeaders, String> headers = new HashMap<>(response.headers);

        other.headers.forEach(headers::put);

        return new ResponseBuilder()
            .setRequest(other.request)
            .setResponseBody(responseBody)
            .setResponseCode(other.responseCode == null ? response.responseCode : other.responseCode)
            .addBehaviors(response.behaviors)
            .addBehaviors(other.behaviors)
            .addResponseBodyFilters(response.responseBodyFilters)
            .addResponseBodyFilters(other.responseBodyFilters)
            .setHeaders(headers)
            .toResponse();
    }
}
