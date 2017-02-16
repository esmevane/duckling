package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.Response;

import java.io.File;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DisplayFolder implements Behavior {
    private String titleTemplate = "<title>%s</title>";
    private String pageTemplate = "<html><head>%s</head><body>%s</body></html>";
    private String linkTemplate = "<a href=\"%s\">%s</a><br />";

    @Override
    public Response apply(Request request) {
        Response response = Response.wrap(request);
        File directory = request.getFile();
        String title = String.format(titleTemplate, directory.getName());
        String[] list = directory.list();

        if (list == null) {
            return response.withBody(String.format(pageTemplate, title, ""));
        }

        Stream<String> links = Stream.of(list).map((item) -> {
            File file = new File(directory, item);
            String name = file.getName() + (file.isDirectory() ? "/" : "");
            String path = Paths.get(request.getPath(), name).toString();

            return String.format(linkTemplate, path, name);
        });

        String contents = links.collect(Collectors.joining());
        String body = String.format(pageTemplate, title, contents);

        return response.withBody(body);
    }

}
