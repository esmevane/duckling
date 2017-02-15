package duckling.pages;

import duckling.requests.Request;

public class StaticBody implements Page {
    private String content;

    public StaticBody(String content) {
        this.content = content;
    }

    @Override
    public String apply(Request request) {
        return content;
    }
}
