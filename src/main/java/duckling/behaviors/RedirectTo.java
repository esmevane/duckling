package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.CommonHeaders;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;

public class RedirectTo implements Behavior {
    private String location;

    public RedirectTo(String location) {
        this.location = location;
    }

    @Override
    public Response apply(Request request) {
        return Response
            .wrap(request)
            .withResponseCode(ResponseCodes.FOUND)
            .withHeader(CommonHeaders.LOCATION, location);
    }
}
