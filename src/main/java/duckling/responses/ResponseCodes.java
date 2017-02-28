package duckling.responses;

public enum ResponseCodes {
    OK                    (200, "OK"),
    NO_CONTENT            (204, "NO CONTENT"),
    PARTIAL_CONTENT       (206, "PARTIAL CONTENT"),
    FOUND                 (302, "FOUND"),
    BAD_REQUEST           (400, "BAD REQUEST"),
    ACCESS_DENIED         (401, "ACCESS DENIED"),
    NOT_FOUND             (404, "NOT FOUND"),
    METHOD_NOT_ALLOWED    (405, "METHOD NOT ALLOWED"),
    TEAPOT                (418, "TEAPOT");

    final int status;
    final String message;

    ResponseCodes(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String toString() {
        return status + " " + message;
    }

}
