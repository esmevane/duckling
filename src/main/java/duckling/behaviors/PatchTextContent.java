package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;

import java.util.stream.Collectors;

public class PatchTextContent implements Behavior {

    @Override
    public Response apply(Request request) {
        Response response = Response.wrap(request);
        String ifMatch = request.headers.get("If-Match");

        if (!request.fileExists() || ifMatch.isEmpty()) return response;

        String update = request.getBody().stream().collect(Collectors.joining());

        if (request.maybeWrite(ifMatch, update)) {
            return response.withResponseCode(ResponseCodes.NO_CONTENT).withBody("");
        }

        return response;
    }

}
