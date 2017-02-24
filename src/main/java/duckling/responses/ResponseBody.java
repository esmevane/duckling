package duckling.responses;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ResponseBody {
    String body;
    byte[] inputContent = new byte[0];
    boolean isStream;
    boolean emptied;

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

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            int input;
            while ((input = inputStream.read()) != -1) output.write(input);
            inputContent = output.toByteArray();
        } catch (IOException exception) {
            inputContent = new byte[0];
        }
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
        return new ByteArrayInputStream(inputContent);
    }

    public boolean isStream() {
        return isStream;
    }

    public boolean wasEmptied() {
        return emptied;
    }

    public byte[] getBytes() {
        return isStream() ? inputContent : body.getBytes();
    }

    public byte[] getBytes(int start) {
        byte[] fullContent = getBytes();
        return getBytes(start, fullContent.length - 1);
    }

    public byte[] getBytes(int start, int finish) {
        byte[] fullContent = getBytes();
        try {
            return Arrays.copyOfRange(fullContent, start, finish + 1);
        } catch (IllegalArgumentException exception) {
            return fullContent;
        }
    }

    @Override
    public String toString() {
        return body;
    }

}
