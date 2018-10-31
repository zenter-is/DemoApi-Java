import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class GraphqlClient
{
	private String url;

	public GraphqlClient(String url)
	{
		this.url = url;
	}

	public void changeUrl(String url)
	{
		this.url = url;
	}

	public String GetVersion(String email, String password)
    {
        GraphqlRequest q = new GraphqlRequest();
        q.query = "query{ version}";

        GraphqlResponse<Version> result = this.query(q, Version.class);
        return result.data.version;
    }

    public String GetLoginToken(String email, String password)
    {
        GraphqlRequest q = new GraphqlRequest();
        q.query = "mutation($email:String!,$password:String!){ loginApiUser(email:$email, password:$password)}";
        q.variables = new HashMap<String, String>();
        q.variables.put("email",email);
        q.variables.put("password",password);

        GraphqlResponse<Login> result = this.query(q, Login.class);
        return result.data.loginApiUser;
    }

    public Boolean IsLoggedIn()
    {
        GraphqlRequest q = new GraphqlRequest();
        q.query = "query{ loggedIn:isPriviliged }";

        GraphqlResponse<LoggedIn> result = this.query(q, LoggedIn.class);

        return result.data.loggedIn;
    }

    public <T> GraphqlResponse<T> query( GraphqlRequest query, Class<T> type)
    {
        Gson gson          = new Gson();
        try {
            StringEntity postEntity = new StringEntity(gson.toJson(query));
            String postingString = EntityUtils.toString(postEntity, "UTF-8");

            HttpClient   httpClient    = HttpClientBuilder.create().build();
            HttpPost     post          = new HttpPost(this.url);

            post.setEntity(postEntity);
            post.setHeader("Content-type", "application/json");
            HttpResponse  response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();

            String responseString = EntityUtils.toString(entity, "UTF-8");

            Type resultType = TypeToken.getParameterized(GraphqlResponse.class, type).getType();

            //Type resultType = new TypeToken<GraphqlResponse<T>>() {}.getType();
            GraphqlResponse<T> response = gson.fromJson(responseString, resultType);

            return response;

        }catch (Exception ex)
        {
            System.out.println(ex);
        }
        return null;
    }
}