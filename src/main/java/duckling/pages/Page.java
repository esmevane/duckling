package duckling.pages;

import duckling.requests.Request;

public interface Page {
    String apply(Request request);
}
