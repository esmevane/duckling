package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;

public class SetCookieFromParam implements Behavior {
    String key;

    public SetCookieFromParam(String key) {
        this.key = key;
    }

    @Override
    public Response apply(Request request) {
        String value = request.getParams().getOrDefault(key, "");

        return new SetCookie(key, value).apply(request);
    }
}
