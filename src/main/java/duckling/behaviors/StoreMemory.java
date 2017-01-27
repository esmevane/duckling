package duckling.behaviors;

import duckling.MemoryCache;
import duckling.requests.Request;

import java.util.stream.Collectors;

public class StoreMemory implements Behavior {
    @Override
    public String apply(Request request) {
        MemoryCache cache = new MemoryCache();

        cache.put(
            request.getPath(),
            request.getBody().stream().collect(Collectors.joining(""))
        );

        return "";
    }
}
