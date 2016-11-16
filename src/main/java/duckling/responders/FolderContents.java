package duckling.responders;

import duckling.requests.Request;
import duckling.ResponseHeaders;
import duckling.Server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class FolderContents extends Responder {
    private File directory;

    public FolderContents(Request request, OutputStream outputStream) {
        super(request, outputStream);

        this.directory = request.getFile();
    }

    @Override
    public boolean matches() {
        return this.directory.exists() && this.directory.isDirectory();
    }

    @Override
    public void respond() throws IOException {
        ResponseHeaders responseHeaders = new ResponseHeaders().
                withContentType("text/html");

        for (String line: responseHeaders.toArray()) {
            this.outputStream.write(line.getBytes());
        }

        this.outputStream.write(rawFolderResponse().getBytes());
    }

    private String rawFolderResponse() {
        return "<html><head><title>/"
            + this.directory.getName()
            + "</title></head><body>"
            + contents()
            + "</body></head>" + Server.CRLF;
    }

    private String contents() {
        String links = "";
        String[] list = this.directory.list();

        for (String item: list) {
            File file = new File(item);

            if (file.isDirectory()) {
                links = links + "<a href=\"/" + item + "/\">" + item + "/</a><br>";
            } else {
                links = links + "<a href=\"/" + item + "\">" + item + "</a><br>";
            }
        }

        return links;
    }

}
