package duckling.responses;

public interface ResponseBodyFilter {
    Response apply(Response response);
}
