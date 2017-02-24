package duckling.behaviors;

import duckling.requests.Request;
import duckling.responses.ResponseBodyFilter;
import duckling.responses.Response;
import duckling.responses.ResponseCodes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class PartialContent implements Behavior {

    @Override
    public Response apply(Request request) {
        String rawRange = request.headers.get("Range");
        Response response = Response.wrap(request);

        if (rawRange.isEmpty()) return Response.wrap(request);

        ResponseBodyFilter responseBodyFilter = (givenResponse) -> {
            try {
                byte[] content;
                Range range = new Range(rawRange);

                if (range.noDefinedEnd()) {
                    content = givenResponse.responseBody.getBytes(range.from());
                } else if (range.noDefinedStart()) {
                    content = givenResponse.responseBody.getBytes(
                        givenResponse.responseBody.getBytes().length - range.to()
                    );
                } else {
                    content = givenResponse.responseBody.getBytes(range.from(), range.to());
                }

                InputStream stream = new ByteArrayInputStream(content);

                return givenResponse.withStreamBody(stream);
            } catch (IndexOutOfBoundsException exception) {
                return givenResponse.withResponseCode(ResponseCodes.BAD_REQUEST);
            }
        };

        return response
            .withResponseCode(ResponseCodes.PARTIAL_CONTENT)
            .withResponseBodyFilter(responseBodyFilter);
    }

    class ParseIntWithDefault {
        public Integer apply(String string, Integer givenDefault) {
            return string == null || string.isEmpty() ? givenDefault : Integer.parseInt(string);
        }
    }

    class Range {
        String raw;
        ArrayList<String> values;

        Range(String rawRange) {
            ArrayList<String> rangePair = new ArrayList(Arrays.asList(rawRange.split("=")));

            this.raw = rawRange;
            this.values = new ArrayList(Arrays.asList(rangePair.get(1).split("-", 2)));
        }

        public Integer from() {
            ParseIntWithDefault parseInt = new ParseIntWithDefault();
            return parseInt.apply(values.get(0), -1);
        }

        public Integer to() {
            ParseIntWithDefault parseInt = new ParseIntWithDefault();
            return parseInt.apply(values.get(1), -1);
        }

        public boolean noDefinedEnd() {
            return to() == -1;
        }

        public boolean noDefinedStart() {
            return from() == -1;
        }
    }

}