import java.util.Map;

public class GraphqlRequest<T> {
    public String query;
    public Map<String, T> variables;
}