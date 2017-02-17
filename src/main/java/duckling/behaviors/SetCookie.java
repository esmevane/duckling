package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.CommonHeaders;
import duckling.responses.Response;

public class SetCookie implements Behavior {
    String key;
    String value;

    public SetCookie(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Response apply(Request request) {
        return Response
            .wrap(request)
            .withHeader(CommonHeaders.SET_COOKIE, key + "=" + value);
    }
}
