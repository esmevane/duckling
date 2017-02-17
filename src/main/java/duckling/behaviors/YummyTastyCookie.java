package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;

public class YummyTastyCookie implements Behavior {
    String key;

    public YummyTastyCookie(String key) {
        this.key = key;
    }

    @Override
    public Response apply(Request request) {
        Response response = Response.wrap(request);
        String[] flavorPair = request.headers.get("Cookie").split("=");
        String notFound = "Doesn't taste like anything to me.";

        try {
            return key.equals(flavorPair[0]) ?
                response.withBody("mmmm " + flavorPair[1]) :
                response.withBody(notFound);
        } catch (ArrayIndexOutOfBoundsException exception) {
            return Response.wrap(request).withBody(notFound);
        }
    }
}
