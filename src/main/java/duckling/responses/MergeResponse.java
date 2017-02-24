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

        ResponseCodes responseCode =
            other.responseCode == null ?
                response.responseCode :
                other.responseCode;

        HashMap<CommonHeaders, String> headers = new HashMap<>(response.headers);

        other.headers.forEach(headers::put);

        return Response
            .wrap(other.request)
            .withResponseBody(responseBody)
            .withBehaviors(response.behaviors)
            .withBehaviors(other.behaviors)
            .withResponseBodyFilters(response.responseBodyFilters)
            .withResponseBodyFilters(other.responseBodyFilters)
            .withHeaders(headers)
            .withResponseCode(responseCode);
    }
}
