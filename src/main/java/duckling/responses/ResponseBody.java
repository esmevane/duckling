package duckling.responses;

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
            return new ResponseBody("");
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
}
