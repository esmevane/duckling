package duckling;

import java.util.HashMap;

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

    public static void append(String key, String nextString) {
        String currentString = get(key);
        put(key, currentString + nextString);
    }

}
