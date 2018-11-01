
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import java.util.ArrayList;

//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;


public class ZenterApi
{
	String Url;
	GraphqlClient client;

	public ZenterApi(String url)
	{
		this.Url = url;
		this.client = new GraphqlClient(url);
	}


	public Boolean Login(String email, String password)
    {
    	String token = client.GetLoginToken(email, password);

        this.client.changeUrl(this.Url+"?token="+token);

        if (!this.client.IsLoggedIn())
        {
            return false;
        }

        return true;
    }

    public Recipient FindOrCreateRecipient(Recipient recipient)
    {
        if(recipient == null)return null;

        Recipient r = this.client.FindRecipientByEmail(recipient.email);

        if (r != null)
        {
            return r;
        }
        return this.client.CreateRecipient(recipient.name,recipient.email);
    }

    public Job CreateJob(String jobTitle, String jobSubject, String listTitle, ArrayList<Recipient> recipients)
    {
        Template template = this.client.FindTemplateByTitle("test1");

        if (template == null)
        {
            template = this.client.CreateTemplate("test1", readFile("email.html"));
        }

    	List list = this.client.CreateList(listTitle);

    	ArrayList<Integer> recipientIds = new ArrayList<Integer>();
    	for (Recipient r : recipients)
    	{
    		Recipient newRecipient = this.client.CreateRecipient(r.name,r.email);
    		recipientIds.add(newRecipient.id);
    	}

    	this.client.AddRecipientsToList(list.id, recipientIds);

    	Job job = this.client.CreateEmailJob(jobTitle, jobSubject, template.id);

    	this.client.AddListToJob(list, job);

        return job;
    }

    public void SendJob(Job job)
    {
        this.client.SendJob(job);
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
}