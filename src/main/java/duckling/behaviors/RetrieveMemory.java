package duckling.behaviors;

import duckling.MemoryCache;
import duckling.requests.Request;

public class RetrieveMemory implements Behavior {
    @Override
    public String apply(Request request) {
        MemoryCache cache = new MemoryCache();

        return cache.get(request.getPath());
    }
}
