package duckling.requests;

import java.util.Arrays;
import java.util.List;

public class HeaderPair {
    private static final String KEY_VALUE_SEPARATOR = ": ";
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    public String key = "";
    public String value = "";

    public HeaderPair(String line) {
        List<String> headers = Arrays.asList(line.split(KEY_VALUE_SEPARATOR));

        try {
            this.key = headers.get(KEY_INDEX);
            this.value = headers.get(VALUE_INDEX);
        } catch (ArrayIndexOutOfBoundsException exception) {
        }
    }

    public boolean matches(String key) {
        return this.key.equals(key);
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return (this.key + this.value).hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return equals((HeaderPair) other);
    }

    public boolean equals(HeaderPair other) {
        return this.key.equals(other.key) && this.value.equals(other.value);
    }

    public String toString() {
        return this.key + ": " + this.value;
    }
}
