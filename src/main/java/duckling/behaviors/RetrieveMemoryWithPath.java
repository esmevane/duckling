package duckling.behaviors;

import duckling.MemoryCache;
import duckling.requests.Request;
import duckling.responses.Response;

public class RetrieveMemoryWithPath implements Behavior {
    @Override
    public Response apply(Request request) {
        String body = MemoryCache.get(request.getPath());

        return Response.wrap(request).withBody(body);
    }
}
