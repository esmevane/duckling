package duckling.pages;

import duckling.MemoryCache;
import duckling.requests.Request;

import java.util.stream.Collectors;

public class StoreMemory implements Page {
    @Override
    public String apply(Request request) {
        MemoryCache.put(
            request.getPath(),
            request.getBody().stream().collect(Collectors.joining(""))
        );

        return "";
    }
}
