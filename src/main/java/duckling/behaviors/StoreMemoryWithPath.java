package duckling.behaviors;

import duckling.MemoryCache;
import duckling.requests.Request;
import duckling.responses.Response;

import java.util.stream.Collectors;

public class StoreMemoryWithPath implements Behavior {
    @Override
    public Response apply(Request request) {
        MemoryCache.put(
            request.getPath(),
            request.getBody().stream().collect(Collectors.joining(""))
        );

        return Response.wrap(request);
    }
}
