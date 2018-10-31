import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GraphqlResponse<T>
{
	@SerializedName("data")
	@Expose
	public T data;

	public GraphqlResponse(T t) {
        this.data = t;
    }
}