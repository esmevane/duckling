package duckling.responses;

public enum CommonHeaders {
    CONTENT_TYPE ("Content-Type"),
    LOCATION     ("Location"),
    ALLOW        ("Allow"),
    SET_COOKIE   ("Set-Cookie");

    final String name;

    CommonHeaders(String name) {
       this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
