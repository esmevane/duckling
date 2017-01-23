package duckling.responses;

import duckling.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class ResponseHeaders {
    protected ResponseCode responseCode;
    protected String contentType;
    protected String location;
    protected ArrayList<String> permittedMethods;

    public ResponseHeaders() {
        this(ResponseCode.ok(), "null", null);
    }

    public ResponseHeaders(ResponseCode responseCode) {
        this(responseCode, "null", null);
    }

    public ResponseHeaders(
        ResponseCode responseCode,
        String contentType
    ) {
        this(
            responseCode,
            contentType,
            null,
            new ArrayList<>()
        );
    }

    public ResponseHeaders(
        ResponseCode responseCode,
        String contentType,
        String location
    ) {
        this(
            responseCode,
            contentType,
            location,
            new ArrayList<>()
        );
    }

    public ResponseHeaders(
        ResponseCode responseCode,
        String contentType,
        String location,
        ArrayList<String> permittedMethods
    ) {
        this.contentType = contentType;
        this.responseCode = responseCode;
        this.location = location;
        this.permittedMethods = permittedMethods;
    }

    public ResponseHeaders allowedMethods(ArrayList<String> permittedMethods) {
        return new ResponseHeaders(
            this.responseCode,
            this.contentType,
            this.location,
            permittedMethods
        );
    }

    public ArrayList<String> toList() {
        return this.permittedMethods.size() > 0 ? optionsResponse() : completeResponse();
    }

    public ResponseHeaders notAllowed() {
        return withStatus(ResponseCode.notAllowed());
    }

    public ResponseHeaders notFound() {
        return withStatus(ResponseCode.notFound());
    }

    public ResponseHeaders found(String uri) {
        return withStatus(ResponseCode.found()).withLocation(uri);
    }

    public ResponseHeaders withLocation(String uri) {
        return new ResponseHeaders(responseCode, contentType, uri);
    }


    public ResponseHeaders withContentType(String newContentType) {
        return new ResponseHeaders(responseCode, newContentType, location);
    }

    public ResponseHeaders withStatus(ResponseCode responseCode) {
        return new ResponseHeaders(responseCode, contentType, location);
    }

    @Override
    public String toString() {
        return toList().stream().collect(Collectors.joining());
    }

    private ArrayList<String> completeResponse() {
        ArrayList<String> response = new ArrayList<>();

        Collections.addAll(
            response,
            "HTTP/1.0 " + responseCode + Server.CRLF,
            "Content-Type: " + contentType + Server.CRLF
        );

        if (this.location != null) {
            response.add("Location: " + this.location + Server.CRLF);
        }

        response.add(Server.CRLF);

        return response;
    }

    private ArrayList<String> optionsResponse() {
        String allowed = this.permittedMethods.stream().collect(Collectors.joining(","));

        return new ArrayList<>(Arrays.asList(
            "HTTP/1.0 " + responseCode + Server.CRLF,
            "Allow: " + allowed + Server.CRLF,
            Server.CRLF
        ));
    }

}
