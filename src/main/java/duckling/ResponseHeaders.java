package duckling;

public class ResponseHeaders {
    protected String contentType = "null";
    protected String status = "200 OK";

    public String[] toArray() {
        return new String[] {
            "HTTP/1.0 " + status + Server.CRLF,
            "Content-Type: " + contentType + Server.CRLF,
            Server.CRLF
        };
    }

    public ResponseHeaders notFound() {
        return withStatus("404 NOT FOUND");
    }

    public ResponseHeaders withContentType(String givenContentType) {
        String currentStatus = status;

        return new ResponseHeaders() {{
            status = currentStatus;
            contentType = givenContentType;
        }};
    }

    public ResponseHeaders withStatus(String newStatus) {
        String currentContentType = contentType;

        return new ResponseHeaders() {{
            status = newStatus;
            contentType = currentContentType;
        }};

    }
}
