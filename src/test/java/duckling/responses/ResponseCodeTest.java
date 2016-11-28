package duckling.responses;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ResponseCodeTest {

    @Test
    public void defaultsToOk() {
        assertThat(new ResponseCode(), is(ResponseCode.ok()));
    }

}
