package duckling.behaviors;

import duckling.pages.Page;
import duckling.requests.Request;
import duckling.responses.Response;

public class EitherBody implements Behavior {
    Page page;
    boolean shouldHaveBody = true;

    public EitherBody(boolean shouldHaveBody, Page page) {
        this.shouldHaveBody = shouldHaveBody;
        this.page = page;
    }

    @Override
    public Response apply(Request request) {
        Response response = Response.wrap(request);

        return shouldHaveBody ?
            response.withBody(page.apply(request)) :
            response.withoutBody();
    }
}
