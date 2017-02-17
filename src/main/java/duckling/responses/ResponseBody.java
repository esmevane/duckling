package duckling.responses;

import java.util.Arrays;

public class ResponseBody {
    private boolean emptied;
    String body;

    public ResponseBody(String body) {
        this(body, false);
    }

    public ResponseBody(String body, boolean emptied) {
        this.body = body;
        this.emptied = emptied;
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

    public boolean wasEmptied() {
        return emptied;
    }

    public byte[] getBytes() {
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

}
