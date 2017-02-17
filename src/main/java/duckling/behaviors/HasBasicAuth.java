package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.CommonHeaders;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;

import java.util.Base64;

public class HasBasicAuth implements Behavior {
    String username;
    String password;
    String target;

    public HasBasicAuth(String username, String password) {
        this.username = username;
        this.password = password;
        this.target = username + ":" + password;
    }

    @Override
    public Response apply(Request request) {
        Response response = Response.wrap(request);
        String[] authPair = request.headers.get("Authorization").split(" ");
        Response denied = response
            .withResponseCode(ResponseCodes.ACCESS_DENIED)
            .withHeader(CommonHeaders.AUTHENTICATE, "Basic realm=\"duckling\"");

        try {
            String credentials = authPair[1];
            String target = new String(Base64.getEncoder().encode(this.target.getBytes()));

            return credentials.equals(target) ? response : denied;
        } catch (ArrayIndexOutOfBoundsException exception) {
            return denied;
        }

    }
}
