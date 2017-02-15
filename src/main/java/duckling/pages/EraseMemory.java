package duckling.pages;

import duckling.MemoryCache;
import duckling.requests.Request;

public class EraseMemory implements Page {
    @Override
    public String apply(Request request) {
        MemoryCache cache = new MemoryCache();

        cache.remove(request.getPath());

        return "";
    }
}
