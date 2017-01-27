package duckling.behaviors;

import duckling.MemoryCache;
import duckling.requests.Request;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EraseMemoryTest {
    @Before
    public void setup() throws Exception {
        MemoryCache.flush();
    }

    @Test
    public void applyReturnsRetrievedMemory() throws Exception {
        Request request = new Request();
        String expectation = "Stuff=Things";
        String key = "/";
        EraseMemory behavior = new EraseMemory();

        MemoryCache.put(key, expectation);

        request.add("GET / HTTP/1.1");
        behavior.apply(request);
        assertThat(MemoryCache.get("/"), is(""));
    }
}
