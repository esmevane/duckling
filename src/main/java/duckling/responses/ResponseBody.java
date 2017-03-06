package duckling.responses;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResponseBody {
    String body;
    InputStream inputStream;
    boolean isStream = false;
    boolean emptied = false;

    public ResponseBody(String body) {
        this(body, false);
    }

    public ResponseBody(String body, boolean emptied) {
        this.body = body;
        this.emptied = emptied;
    }

    public ResponseBody(String body, InputStream inputStream) {
        this(body);

        this.isStream = true;
        this.inputStream = inputStream;
    }

    public ResponseBody merge(ResponseBody other) {
        if (other.isStream()) {
            return new ResponseBody("", other.getInputStream());
        } else if (isStream()) {
            return new ResponseBody("", getInputStream());
        } else if (other.wasEmptied()) {
            return new ResponseBody("", true);
        } else if (other.body == null || other.body.isEmpty()) {
            return new ResponseBody(body);
        } else {
            return new ResponseBody(other.body);
        }
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public boolean isStream() {
        return isStream;
    }

    public boolean wasEmptied() {
        return emptied;
    }

    public InputStream getContent() {
        return isStream() ? inputStream : new ByteArrayInputStream(body.getBytes());
    }

    public byte[] getBytes() {
        return isStream() ? readBody() : body.getBytes();
    }

    @Override
    public String toString() {
        return body;
    }

    private byte[] readBody() {
        byte[] inputContent;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            int input;
            while ((input = this.inputStream.read()) != -1) output.write(input);
            inputContent = output.toByteArray();
        } catch (IOException exception) {
            inputContent = new byte[0];
        }

        return inputContent;
    }

}
