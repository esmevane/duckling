package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;

public class EitherBody implements Behavior {
    Behavior behavior;
    boolean shouldHaveBody = true;

    public EitherBody(boolean shouldHaveBody, Behavior behavior) {
        this.shouldHaveBody = shouldHaveBody;
        this.behavior = behavior;
    }

    @Override
    public Response apply(Request request) {
        Response response = Response.wrap(request);

        return shouldHaveBody ?
            response.merge(behavior.apply(request)) :
            response.withoutBody();
    }
}
