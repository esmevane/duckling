package duckling.support;

import duckling.requests.Request;
import duckling.responders.Responders;

public class SpyResponders extends Responders {
    private boolean wasRespondCalled = false;
    public SpyResponders() {
        this(new Request("."));
    }

    public SpyResponders(Request request) {
        super(request);
    }
}
