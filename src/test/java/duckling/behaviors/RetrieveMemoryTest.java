package duckling.behaviors;

import duckling.MemoryCache;
import duckling.requests.Request;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RetrieveMemoryTest {
    @Before
    public void setup() throws Exception {
        MemoryCache.flush();
    }

    @Test
    public void applyReturnsRetrievedMemory() throws Exception {
        Request request = new Request();
        String expectation = "Stuff=Things";
        RetrieveMemory behavior = new RetrieveMemory();

        MemoryCache.put("/", expectation);

        request.add("GET / HTTP/1.1");

        assertThat(behavior.apply(request).getStringBody(), is(expectation));
    }

    @Test
    public void applyMissReturnsEmptyString() throws Exception {
        Request request = new Request();
        String expectation = "Stuff=Things";
        RetrieveMemory behavior = new RetrieveMemory();

        MemoryCache.put("/", expectation);

        request.add("GET /form HTTP/1.1");

        assertThat(behavior.apply(request).getStringBody(), is(""));
    }
}
