package duckling.requests;

import duckling.Server;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

public class HeadersTest {
    @Test
    public void isEmptyAtFirst() throws Exception {
        Headers headers = new Headers();
        assertThat(headers.isEmpty(), is(true));
    }

    @Test
    public void gettingAnUndefinedHeaderReturnsAnEmptyString() throws Exception {
        Headers headers = new Headers();
        assertThat(headers.get("Key"), is(""));
    }

    @Test
    public void gettingAnExistingHeaderReturnsTheHeaderValue() throws Exception {
        Headers headers = new Headers("Key: Value");
        assertThat(headers.get("Key"), is("Value"));
    }

    @Test
    public void isEqualWhenAllPairsMatch() throws Exception {
        Headers headers = new Headers("Key: Value", "KeyTwo: ValueTwo");
        Headers other = new Headers("Key: Value", "KeyTwo: ValueTwo");
        assertThat(headers, is(other));
    }

    @Test
    public void isNotEqualWhenPairsDoNotMatch() throws Exception {
        Headers headers = new Headers("Key: Value", "KeyTwo: ValueTwo");
        Headers other = new Headers("Key: Value");
        assertThat(headers, is(not(other)));
    }

    @Test
    public void outputsKeyValuePairsForToString() throws Exception {
        String[] lines = { "Key: Value", "KeyTwo: ValueTwo" };
        Headers headers = new Headers(lines);

        String asOutput =
            Arrays.
                stream(lines).
                collect(
                    Collectors.joining(Server.CRLF)
                );

        assertThat(headers.toString(), is(asOutput));
    }

}
