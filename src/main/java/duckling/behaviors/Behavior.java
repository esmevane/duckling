package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;

public interface Behavior {
    Response apply(Request request);
}
