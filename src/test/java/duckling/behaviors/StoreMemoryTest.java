package duckling.behaviors;

import duckling.MemoryCache;
import duckling.requests.Request;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StoreMemoryTest {
    @Before
    public void setup() throws Exception {
        MemoryCache.flush();
    }

    @Test
    public void applySavesRequestBody() throws Exception {
        Request request = new Request();
        String expectation = "Stuff=Things";
        StoreMemory behavior = new StoreMemory();

        request.add("GET / HTTP/1.1", "", expectation);
        behavior.apply(request);

        assertThat(MemoryCache.get("/"), is(expectation));
    }

}

