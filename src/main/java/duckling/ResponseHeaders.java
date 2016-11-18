package duckling;

import java.util.ArrayList;
import java.util.Arrays;

public class ResponseHeaders {
    protected String contentType;
    protected String status;

    public ResponseHeaders() {
        this("200 OK", "null");
    }

    public ResponseHeaders(String status, String contentType) {
        this.contentType = contentType;
        this.status = status;
    }

    public ResponseHeaders notFound() {
        return withStatus("404 NOT FOUND");
    }

    public ResponseHeaders withContentType(String newContentType) {
        return new ResponseHeaders(status, newContentType);
    }

    public ResponseHeaders withStatus(String newStatus) {
        return new ResponseHeaders(newStatus, contentType);
    }

    public ArrayList<String> toList() {
        return new ArrayList<>(Arrays.asList(
                "HTTP/1.0 " + status + Server.CRLF,
                "Content-Type: " + contentType + Server.CRLF,
                Server.CRLF
        ));
    }
}
