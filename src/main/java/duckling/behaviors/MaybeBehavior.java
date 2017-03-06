package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;

import java.util.function.Predicate;

public class MaybeBehavior implements Behavior {
    Predicate<Request> condition;
    Behavior behavior;

    public MaybeBehavior(Predicate<Request> condition, Behavior behavior) {
        this.condition = condition;
        this.behavior = behavior;
    }

    @Override
    public Response apply(Request request) {
        if (condition.test(request)) return behavior.apply(request);

        return Response.wrap(request);
    }

}
