package duckling.behaviors;

import duckling.MemoryCache;
import duckling.requests.Request;
import duckling.responses.Response;

public class EraseMemory implements Behavior {
    @Override
    public Response apply(Request request) {
        MemoryCache.remove(request.getPath());

        return Response.wrap(request);
    }
}
