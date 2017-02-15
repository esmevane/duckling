package duckling.pages;

import duckling.MemoryCache;
import duckling.requests.Request;

public class RetrieveMemory implements Page {
    @Override
    public String apply(Request request) {
        MemoryCache cache = new MemoryCache();

        return cache.get(request.getPath());
    }
}
