package duckling.responses;

public enum ResponseCodes {
    OK                 (200, "OK"),
    NOT_FOUND          (404, "NOT FOUND"),
    TEAPOT             (418, "TEAPOT"),
    METHOD_NOT_ALLOWED (405, "METHOD NOT ALLOWED"),
    FOUND              (302, "FOUND");

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
