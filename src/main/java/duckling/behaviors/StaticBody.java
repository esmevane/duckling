package duckling.behaviors;

import duckling.requests.Request;

public class StaticBody implements Behavior {
    private String content;

    public StaticBody(String content) {
        this.content = content;
    }

    @Override
    public String apply(Request request) {
        return content;
    }
}
