package duckling;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

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

    @Test
    public void shortListTracksLastTwentyStrings() {
        String key = "a string key";
        ArrayList<String> numbers = new ArrayList<>();

        for (int i = 0; i <= 25; i++) {
            if (i > 5) numbers.add("" + i);

            MemoryCache.shortList(key, "" + i);
        }

        String expectation = numbers.stream().collect(Collectors.joining(Server.CRLF));

        assertThat(MemoryCache.get(key), is(expectation));
    }
}
