package duckling.requests;

import java.util.Arrays;
import java.util.List;

class HeaderPair {
    private static final String KEY_VALUE_SEPARATOR = ": ";
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    private String key = "";
    private String value = "";

    public HeaderPair(String line) {
        List<String> headers = Arrays.asList(line.split(KEY_VALUE_SEPARATOR));

        try {
            this.key = headers.get(KEY_INDEX);
            this.value = headers.get(VALUE_INDEX);
        } catch (ArrayIndexOutOfBoundsException exception) {
        }
    }

    public String getValue() {
        return this.value;
    }

    public boolean matches(String key) {
        return this.key.equals(key);
    }

    @Override
    public int hashCode() {
        return (this.key + this.value).hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof HeaderPair) {
            HeaderPair other = (HeaderPair) object;

            return this.key.equals(other.key) &&
                this.value.equals(other.value);
        }

        return false;
    }

    @Override
    public String toString() {
        return this.key + ": " + this.value;
    }
}
