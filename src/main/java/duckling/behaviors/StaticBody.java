package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;

public class StaticBody implements Behavior {
    private String content;

    public StaticBody(String content) {
        this.content = content;
    }

    @Override
    public Response apply(Request request) {
        return Response.wrap(request).withBody(content);
    }
}
