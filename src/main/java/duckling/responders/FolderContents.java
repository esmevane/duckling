package duckling.responders;

import duckling.requests.Request;
import duckling.responses.ResponseHeaders;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FolderContents extends Responder {
    private File directory;

    public FolderContents(Request request) {
        super(request);

        this.directory = request.getFile();
    }

    public FolderContents(Request request, File directory) {
        super(request);

        this.directory = directory;
    }

    public FolderContents(File directory) {
        this(new Request(), directory);
    }

    @Override
    public InputStream body() {
        String responseContent = !isAllowed() ? "" : rawFolderResponse();

        return new ByteArrayInputStream(responseContent.getBytes());
    }

    public String contents() {
        String[] list = this.directory.list();

        if (list == null) {
            return "";
        }

        Stream<String> links = Stream.of(list).map(this::toLink);

        return links.collect(Collectors.joining());
    }

    @Override
    public ArrayList<String> headers() {
        ResponseHeaders headers = new ResponseHeaders();

        if (request.isOptions()) {
            return headers.allowedMethods(this.allowedMethods).toList();
        }

        if (!isAllowed()) {
            return headers.notAllowed().toList();
        }

        return headers.withContentType("text/html").toList();
    }

    @Override
    public boolean matches() {
        return this.directory.exists() && this.directory.isDirectory();
    }

    private String rawFolderResponse() {
        String title = String.format("<title>%s</title>", this.directory.getName());
        String template = "<html><head>%s</head><body>%s</body></html>";

        return String.format(template, title, contents());
    }

    private String toLink(String item) {
        File file = new File(this.directory, item);
        String name = file.getName() + (file.isDirectory() ? "/" : "");
        String path = Paths.get(request.getPath(), name).toString();
        String template = "<a href=\"%s\">%s</a><br />";

        return String.format(template, path, name);
    }

}
