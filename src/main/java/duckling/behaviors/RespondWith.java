package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;

public class RespondWith implements Behavior {
    private ResponseCodes responseCode;

    public RespondWith(ResponseCodes responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public Response apply(Request request) {
        return Response.wrap(request).withResponseCode(responseCode);
    }
}
