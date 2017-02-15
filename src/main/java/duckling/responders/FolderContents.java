package duckling.responders;

import duckling.behaviors.HasOptions;
import duckling.behaviors.EitherBody;
import duckling.behaviors.RestrictToMethods;
import duckling.behaviors.DisplayFolder;
import duckling.requests.Request;

public class FolderContents extends Responder {
    public FolderContents(Request request) {
        super(request);

        response.bind(new HasOptions(allowedMethodsString()));
        response.bind(new RestrictToMethods(allowedMethods));
        response.bind(new EitherBody(isAllowed(), new DisplayFolder()));
    }

    @Override
    public boolean matches() {
        return request.getFile().exists() && request.getFile().isDirectory();
    }

}
