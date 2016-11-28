package duckling.responses;

import duckling.Server;

import java.util.ArrayList;
import java.util.Arrays;

public class ResponseHeaders {
    protected ResponseCode responseCode;
    protected String contentType;

    public ResponseHeaders() {
        this(ResponseCode.ok(), "null");
    }

    public ResponseHeaders(ResponseCode responseCode) {
        this(responseCode, "null");
    }

    public ResponseHeaders(ResponseCode responseCode, String contentType) {
        this.contentType = contentType;
        this.responseCode = responseCode;
    }

    public ResponseHeaders notFound() {
        return withStatus(ResponseCode.notFound());
    }

    public ResponseHeaders withContentType(String newContentType) {
        return new ResponseHeaders(responseCode, newContentType);
    }

    public ResponseHeaders withStatus(ResponseCode responseCode) {
        return new ResponseHeaders(responseCode, contentType);
    }

    public ArrayList<String> toList() {
        return new ArrayList<>(Arrays.asList(
            "HTTP/1.0 " + responseCode + Server.CRLF,
            "Content-Type: " + contentType + Server.CRLF,
            Server.CRLF
        ));
    }
}
