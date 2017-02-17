package duckling.responses;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ResponseBody {
    boolean emptied;
    String body;
    InputStream inputStream;

    public ResponseBody(String body) {
        this(body, false);
    }

    public ResponseBody(String body, boolean emptied) {
        this.body = body;
        this.emptied = emptied;
    }

    public ResponseBody(String body, InputStream inputStream) {
        this(body);

        this.inputStream = inputStream;
    }

    public ResponseBody merge(ResponseBody other) {
        if (other.wasEmptied()) {
            return new ResponseBody("", true);
        } else if (other.body == null || other.body.isEmpty()) {
            return new ResponseBody(body);
        } else {
            return new ResponseBody(other.body);
        }
    }

    public boolean isStream() {
        return inputStream != null;
    }

    public boolean wasEmptied() {
        return emptied;
    }

    public byte[] getBytes() {
        if (isStream()) {
            return getStreamBytes();
        }

        return body.getBytes();
    }

    public byte[] getBytes(int start) {
        byte[] fullContent = getBytes();
        return Arrays.copyOfRange(fullContent, start, fullContent.length);
    }

    public byte[] getBytes(int start, int finish) {
        byte[] fullContent = getBytes();
        return Arrays.copyOfRange(fullContent, start, finish + 1);
    }

    @Override
    public String toString() {
        return body;
    }

    private byte[] getStreamBytes() {
        try {
            int input;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((input = inputStream.read()) != -1) output.write(input);
            return output.toByteArray();
        } catch (IOException exception) {
            return new byte[0];
        }
    }

}
