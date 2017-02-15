package duckling.pages;

import duckling.MemoryCache;
import duckling.requests.Request;

public class RetrieveMemory implements Page {
    @Override
    public String apply(Request request) {
        return MemoryCache.get(request.getPath());
    }
}
