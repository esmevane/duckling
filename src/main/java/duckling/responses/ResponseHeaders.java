package duckling.responses;

import duckling.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ResponseHeaders {
    protected ResponseCode responseCode;
    protected String contentType;
    protected ArrayList<String> permittedMethods;

    public ResponseHeaders() {
        this(ResponseCode.ok(), "null");
    }

    public ResponseHeaders(ResponseCode responseCode) {
        this(responseCode, "null");
    }

    public ResponseHeaders(ResponseCode responseCode, String contentType) {
        this(
            responseCode,
            contentType,
            new ArrayList<>()
        );
    }

    public ResponseHeaders(
        ResponseCode responseCode,
        String contentType,
        ArrayList<String> permittedMethods
    ) {
        this.contentType = contentType;
        this.responseCode = responseCode;
        this.permittedMethods = permittedMethods;
    }

    public ResponseHeaders allowedMethods(ArrayList<String> permittedMethods) {
        return new ResponseHeaders(
            this.responseCode,
            this.contentType,
            permittedMethods
        );
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
        return this.permittedMethods.size() > 0 ? optionsResponse() : completeResponse();
    }

    private ArrayList<String> optionsResponse() {
        String allowed = this.permittedMethods.stream().collect(Collectors.joining(","));

        return new ArrayList<>(Arrays.asList(
            "HTTP/1.0 " + responseCode + Server.CRLF,
            "Allow: " + allowed + Server.CRLF,
            Server.CRLF
        ));
    }

    public ResponseHeaders notAllowed() {
        return withStatus(ResponseCode.notAllowed());
    }

    public String toString() {
        return toList().stream().collect(Collectors.joining());
    }

    private ArrayList<String> completeResponse() {
        return new ArrayList<>(Arrays.asList(
            "HTTP/1.0 " + responseCode + Server.CRLF,
            "Content-Type: " + contentType + Server.CRLF,
            Server.CRLF
        ));
    }

}
