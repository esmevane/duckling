package duckling.requests;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class HeaderPairTest {

    @Test
    public void getValueReturnsValue() {
        HeaderPair headerPair = new HeaderPair("Key: Value");
        assertThat(headerPair.getValue(), is("Value"));
    }

    @Test
    public void equalsSameKeyValue() {
        HeaderPair headerPair = new HeaderPair("Key: Value");
        HeaderPair other = new HeaderPair("Key: Value");
        assertThat(headerPair, is(other));
    }

    @Test
    public void doesNotEqualDifferentKey() {
        HeaderPair headerPair = new HeaderPair("Key: Value");
        HeaderPair other = new HeaderPair("Kia: Value");
        assertThat(headerPair, is(not(other)));
    }

    @Test
    public void doesNotEqualDifferentValue() {
        HeaderPair headerPair = new HeaderPair("Key: Value");
        HeaderPair other = new HeaderPair("Key: Valois");
        assertThat(headerPair, is(not(other)));
    }

    @Test
    public void matchesAgainstKey() {
        HeaderPair headerPair = new HeaderPair("Key: Value");
        assertThat(headerPair.matches("Key"), is(true));
    }

    @Test
    public void presentsKeyValueAsToString() {
        String keyValue = "Key: Value";
        HeaderPair headerPair = new HeaderPair(keyValue);
        assertThat(headerPair.toString(), is(keyValue));
    }

}
