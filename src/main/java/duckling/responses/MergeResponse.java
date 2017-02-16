package duckling.responses;

import duckling.requests.Request;

import java.util.HashMap;

class MergeResponse {
    private Response response = new Response(new Request());

    public MergeResponse(Response original) {
        this.response = original;
    }

    public Response apply(Response other) {
        String body = response.responseBody.merge(other.responseBody).body;

        ResponseCodes responseCode =
            other.responseCode == null ?
                response.responseCode :
                other.responseCode;

        HashMap<CommonHeaders, String> headers = new HashMap<>(response.headers);

        other.headers.forEach(headers::put);

        return Response
            .wrap(other.request)
            .withBody(body)
            .withBehaviors(response.behaviors)
            .withBehaviors(other.behaviors)
            .withHeaders(headers)
            .withResponseCode(responseCode);
    }
}
