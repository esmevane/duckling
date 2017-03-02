package duckling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MemoryCache {
    private static HashMap<String,String> STORE = new HashMap<>();

    public static void put(String key, String value) {
        STORE.put(key, value);
    }

    public static String get(String key) {
        return STORE.getOrDefault(key, "");
    }

    public static void remove(String key) {
        STORE.remove(key);
    }

    public static void flush() {
        STORE = new HashMap<>();
    }

    public static void shortList(String key, String nextString) {
        ArrayList<String> contentList = new ArrayList<>();
        Collections.addAll(contentList, get(key).split(Server.CRLF));
        contentList.add(nextString);

        List<String> lastTwentyEntries = contentList.subList(
            Math.max(contentList.size() - 20, 0),
            contentList.size()
        );

        put(
            key,
            lastTwentyEntries.stream().collect(Collectors.joining(Server.CRLF))
        );
    }

}
