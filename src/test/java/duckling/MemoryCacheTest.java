package duckling;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MemoryCacheTest {
    @Before
    public void setup() throws Exception {
        MemoryCache.flush();
    }

    @Test
    public void getReturnsEmptyStringByDefault() {
        assertThat(MemoryCache.get("nothing"), is(""));
    }

    @Test
    public void getReturnsStoredStringIfPresent() {
        String expectation = "a thing";

        MemoryCache.put("something", expectation);

        assertThat(MemoryCache.get("something"), is(expectation));
    }

    @Test
    public void deleteRemovesStoredStrings() {
        String key = "something";
        String expectation = "a thing";

        MemoryCache.put(key, expectation);
        assertThat(MemoryCache.get(key), is(expectation));

        MemoryCache.remove(key);
        assertThat(MemoryCache.get(key), is(""));
    }
}
