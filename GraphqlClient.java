import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


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
        q.query = readFile("ZenterApiQueries/GetVersion.graphql");

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
            StringEntity postEntity = new StringEntity(gson.toJson(query), "UTF-8");

            //String postingString = EntityUtils.toString(postEntity, "UTF-8");
            //System.out.println(postingString);

            HttpClient   httpClient    = HttpClientBuilder.create().build();
            HttpPost     post          = new HttpPost(this.url);

            post.setEntity(postEntity);
            post.setHeader("Content-type", "application/json");
            post.setHeader("Content-type", "application/json");
            HttpResponse  response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();

            String responseString = EntityUtils.toString(entity, "UTF-8");
            //System.out.println(responseString);
            Type resultType = TypeToken.getParameterized(GraphqlResponse.class, type).getType();

            return gson.fromJson(responseString, resultType);

        }catch (Exception ex)
        {
            System.out.println(ex);
        }
        return null;
    }

	private static String readFile(String filePath)
	{
		StringBuilder contentBuilder = new StringBuilder();

		try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
		{
		    stream.forEach(s -> contentBuilder.append(s).append("\n"));
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}

		return contentBuilder.toString();
	}



	public Boolean AddRecipientsToList(Integer listId, ArrayList<Integer> recipientsIds)
    {
        GraphqlRequest q = new GraphqlRequest();
        q.query = readFile("ZenterApiQueries/AddRecipientsToList.graphql");
        q.variables = new HashMap<String, String>();
        q.variables.put("listId", listId);
        q.variables.put("recipientIds",recipientsIds);

        GraphqlResponse<CreateRecipient> result = this.query(q, CreateRecipient.class);

        //return result.data.addRecipient;
        return false;
    }


    private class LeCount
    {
	    int id;
		int countRecipients;
	}

    private class AddListToJob
    {
    	public LeCount AddListToJob;
    }

    public Boolean AddListToJob(List list, Job job)
    {
    	GraphqlRequest q = new GraphqlRequest();
        q.query = readFile("ZenterApiQueries/AddListToJob.graphql");
        q.variables = new HashMap<String, Integer>();
        q.variables.put("listId", list.id);
        q.variables.put("jobId", job.id);

        GraphqlResponse<AddListToJob> result = this.query(q, AddListToJob.class);


		//result.data.AddListToJob;

    	return false;
    }

    private class CreateRecipient
    {
    	public Recipient addRecipient;
    }

	public Recipient CreateRecipient(String name, String email)
    {
        GraphqlRequest q = new GraphqlRequest();
        q.query = readFile("ZenterApiQueries/AddRecipient.graphql");
       	q.variables = new HashMap<String, String>();
        q.variables.put("name",name);
        q.variables.put("email",email);

        GraphqlResponse<CreateRecipient> result = this.query(q, CreateRecipient.class);

        return result.data.addRecipient;
    }

    public class FindRecipient
    {
    	public ArrayList<Recipient> recipients;
    }

    public Recipient FindRecipientByEmail(String email)
    {
        GraphqlRequest q = new GraphqlRequest();
        q.query = readFile("ZenterApiQueries/FindRecipient.graphql");
       	q.variables = new HashMap<String, String>();
        q.variables.put("email",email);

        GraphqlResponse<FindRecipient> result = this.query(q, FindRecipient.class);
        if (result.data.recipients.size() > 0)
        {
        	return result.data.recipients.get(0);
        }
        return null;
    }



    private class CreateList
    {
    	public List CreateList;
    }

    public List CreateList(String title)
    {
        GraphqlRequest q = new GraphqlRequest();
        q.query = readFile("ZenterApiQueries/CreateList.graphql");
        q.variables = new HashMap<String, String>();
        q.variables.put("title",title);

        GraphqlResponse<CreateList> result = this.query(q, CreateList.class);

        return result.data.CreateList;
    }

    private class CreateJob
    {
    	public Job CreateJob;
    }

    public Job CreateEmailJob(String title, String subject, int templateId)
    {
        GraphqlRequest q = new GraphqlRequest();
        q.query = readFile("ZenterApiQueries/CreateEmailJob.graphql");
        q.variables = new HashMap<String, String>();
        q.variables.put("title",title);
        q.variables.put("subject",subject);
        q.variables.put("templateId",templateId);

        GraphqlResponse<CreateJob> result = this.query(q, CreateJob.class);

        return result.data.CreateJob;
    }


    public class CreateTemplate
    {
    	public Template CreateTemplate;
    }

    public Template CreateTemplate(String title, String body)
    {
        GraphqlRequest<String> q = new GraphqlRequest();
        q.query = readFile("ZenterApiQueries/CreateTemplate.graphql");
        q.variables = new HashMap<String, String>();
        q.variables.put("title",title);
        q.variables.put("body",body);

        GraphqlResponse<CreateTemplate> result = this.query(q, CreateTemplate.class);

        if (result == null)
        {
        	return null;
        }
        return result.data.CreateTemplate;
    }

    public class FindTemplate
    {
    	public Template template;
    }

    public Template FindTemplateByTitle(String title)
    {
        GraphqlRequest q = new GraphqlRequest();
        q.query = readFile("ZenterApiQueries/FindTemplate.graphql");
        q.variables = new HashMap<String, String>();
        q.variables.put("title",title);

        GraphqlResponse<FindTemplate> result = this.query(q, FindTemplate.class);

        return result.data.template;
    }


    public class SendJob
    {
    	public Job SendJob;
    }

    public void SendJob(Job job)
    {
        GraphqlRequest q = new GraphqlRequest();
        q.query = readFile("ZenterApiQueries/SendJob.graphql");
        q.variables = new HashMap<String, String>();
        q.variables.put("id",job.id);

        GraphqlResponse<SendJob> result = this.query(q, SendJob.class);

        System.out.println(result.data.SendJob.sendStatus);
    }

    public class CreateAssetQueryResponse
    {
        Asset addAssetNode;
    }

    public Asset CreateAsset(string filename, string base64EncodedData, int parentId)
    {
        GraphqlRequest q = new GraphqlRequest();
        q.query = readFile("ZenterApiQueries/CreateAsset.graphql");
        q.variables = new HashMap<String, String>();
        q.variables.put("name", filename);
        q.variables.put("base64EncodedData", base64EncodedData);
        q.variables.put("parentId", parentId);

        GraphqlResponse<CreateAssetQueryResponse> result = this.query(q, CreateAssetQueryResponse.class);

        return result.data.addAssetNode;
    }
}