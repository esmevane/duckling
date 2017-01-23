package duckling.responses;

import java.util.Hashtable;

public class ResponseCode {
    public static final int OK = 200;
    public static final int NOT_FOUND = 404;
    public static final int TEAPOT = 418;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int FOUND = 302;

    private Hashtable<Integer, String> codes;
    private int code;

    {
        codes = new Hashtable<>();

        codes.put(OK, "OK");
        codes.put(NOT_FOUND, "NOT FOUND");
        codes.put(TEAPOT, "TEAPOT");
        codes.put(METHOD_NOT_ALLOWED, "METHOD NOT ALLOWED");
        codes.put(FOUND, "FOUND");
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

    public static ResponseCode found() {
        return new ResponseCode(FOUND);
    }

    public static ResponseCode notAllowed() {
        return new ResponseCode(METHOD_NOT_ALLOWED);
    }

    public String message() {
        return codes.get(code);
    }

    @Override
    public int hashCode() {
        return message().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ResponseCode) {
            ResponseCode other = (ResponseCode) object;

            return this.code == other.code;
        }

        return false;
    }

    @Override
    public String toString() {
        return code + " " + message();
    }

}
