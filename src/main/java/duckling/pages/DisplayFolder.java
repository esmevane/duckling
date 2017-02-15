package duckling.pages;

import duckling.requests.Request;

import java.io.File;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DisplayFolder implements Page {
    String titleTemplate = "<title>%s</title>";
    String pageTemplate = "<html><head>%s</head><body>%s</body></html>";
    String linkTemplate = "<a href=\"%s\">%s</a><br />";

    @Override
    public String apply(Request request) {
        File directory = request.getFile();
        String title = String.format(titleTemplate, directory.getName());
        String[] list = directory.list();

        if (list == null) {
            return String.format(pageTemplate, title, "");
        }

        Stream<String> links = Stream.of(list).map((item) -> {
            File file = new File(directory, item);
            String name = file.getName() + (file.isDirectory() ? "/" : "");
            String path = Paths.get(request.getPath(), name).toString();

            return String.format(linkTemplate, path, name);
        });

        String contents = links.collect(Collectors.joining());

        return String.format(pageTemplate, title, contents);
    }

}
