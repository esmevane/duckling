package duckling.behaviors;

import duckling.requests.Request;

public interface Behavior {
    String apply(Request request);
}
