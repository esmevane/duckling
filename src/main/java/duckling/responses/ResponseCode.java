package duckling.responses;

import java.util.Hashtable;

public class ResponseCode {
    public static final int OK = 200;
    public static final int NOT_FOUND = 404;
    public static final int TEAPOT = 418;

    private Hashtable<Integer, String> codes;
    private int code;

    {
        codes = new Hashtable<>();

        codes.put(OK, "OK");
        codes.put(NOT_FOUND, "NOT FOUND");
        codes.put(TEAPOT, "TEAPOT");
    }

    public ResponseCode() {
        this(OK);
    }

    public ResponseCode(int code) {
        this.code = code;
    }

    public static ResponseCode ok() {
        return new ResponseCode(OK);
    }

    public static ResponseCode teapot() {
        return new ResponseCode(TEAPOT);
    }

    public static ResponseCode notFound() {
        return new ResponseCode(NOT_FOUND);
    }

    public String message() {
        return codes.get(code);
    }

    public int hashCode() {
        return message().hashCode();
    }

    public boolean equals(Object other) {
        return equals((ResponseCode) other);
    }

    public boolean equals(ResponseCode other) {
        return code == other.code;
    }

    public String toString() {
        return code + " " + message();
    }

}
