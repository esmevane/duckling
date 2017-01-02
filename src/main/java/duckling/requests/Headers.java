package duckling.requests;

import duckling.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Headers {
    private List<HeaderPair> contents = new ArrayList<>();

    public Headers(String... lines) {
        Arrays.asList(lines).forEach(this::add);
    }

    public void add(String line) {
        this.contents.add(new HeaderPair(line));
    }

    public String get(String key) {
        return this.
            contents.
            stream().
            filter(pair -> pair.matches(key)).
            findFirst().
            map(pair -> pair.getValue()).
            orElse("");
    }

    public boolean isEmpty() {
        return this.contents.size() == 0;
    }

    @Override
    public int hashCode() {
        return this.contents.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return equals((Headers) other);
    }

    public boolean equals(Headers other) {
        return this.contents.equals(other.contents);
    }

    public String toString() {
        return this.
            contents.
            stream().
            map(pair -> pair.toString()).
            collect(Collectors.joining(Server.CRLF));
    }

}
