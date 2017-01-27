package duckling.behaviors;

import duckling.MemoryCache;
import duckling.requests.Request;

public class EraseMemory implements Behavior {
    @Override
    public String apply(Request request) {
        MemoryCache cache = new MemoryCache();

        cache.remove(request.getPath());

        return "";
    }
}
