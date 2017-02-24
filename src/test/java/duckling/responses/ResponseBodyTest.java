package duckling.responses;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ResponseBodyTest {

    @Test
    public void wasEmptiedIsTrueIfInstructed() throws Exception {
        assertThat(new ResponseBody("", true).wasEmptied(), is(true));
    }

    @Test
    public void wasEmptiedIsFalseByDefault() throws Exception {
        assertThat(new ResponseBody("").wasEmptied(), is(false));
    }

    @Test
    public void isStreamIsFalseByDefault() throws Exception {
        assertThat(new ResponseBody("").isStream(), is(false));
    }

    @Test
    public void isStreamIsTrueIfHandedStream() throws Exception {
        InputStream stream = new ByteArrayInputStream("123".getBytes());

        assertThat(new ResponseBody(null, stream).isStream(), is(true));
    }

    @Test
    public void mergeReplacesBodyContent() throws Exception {
        ResponseBody original = new ResponseBody("Hey");
        ResponseBody other = new ResponseBody("You!");

        assertThat(original.merge(other).toString(), is("You!"));
    }

    @Test
    public void mergingNullPreservesOriginalContent() throws Exception {
        ResponseBody original = new ResponseBody("Hey");
        ResponseBody other = new ResponseBody(null);

        assertThat(original.merge(other).toString(), is("Hey"));
    }

    @Test
    public void mergingEmptyStringsPreservesOriginalContent() throws Exception {
        ResponseBody original = new ResponseBody("Hey");
        ResponseBody other = new ResponseBody("");

        assertThat(original.merge(other).toString(), is("Hey"));
    }

    @Test
    public void mergingEmptiedResponsesRemovesContent() throws Exception {
        ResponseBody original = new ResponseBody("Hey");
        ResponseBody other = new ResponseBody("You!", true);

        assertThat(original.merge(other).toString(), is(""));
    }

    @Test
    public void requestingBytesReturnsContentBytes() throws Exception {
        String content = "Hey";

        assertThat(new ResponseBody(content).getBytes(), is(content.getBytes()));
    }

    @Test
    public void requestingBytesWithStartReturnsClippedBytes() throws Exception {
        assertThat(new ResponseBody("Heyo").getBytes(1), is("eyo".getBytes()));
    }

    @Test
    public void requestingBytesWithStartAndEndReturnsClippedBytes() throws Exception {
        assertThat(new ResponseBody("Heyo").getBytes(1, 2), is("ey".getBytes()));
    }

    @Test
    public void getBytesReturnsStreamBytesIfIsStream() throws Exception {
        byte[] content = "Hello".getBytes();
        InputStream stream = new ByteArrayInputStream(content);
        ResponseBody responseBody = new ResponseBody(null, stream);

        assertThat(responseBody.getBytes(), is(content));
    }

    @Test
    public void requestingBytesMultipleTimes() throws Exception {
        byte[] content = "Hello".getBytes();
        InputStream stream = new ByteArrayInputStream(content);
        ResponseBody responseBody = new ResponseBody(null, stream);

        responseBody.getBytes();

        assertThat(responseBody.getBytes(), is(content));
    }

}
