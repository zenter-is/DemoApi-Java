

public class Example2
{
    public static final String API_ENDPOINT = "http://zenter.local/Api/V2";

    public static final String API_USER = "1_api@zenter.is";
    public static final String API_PASSPHRASE = "a2d80ec3929f885b55d8e403b97a4da3";


    public static void main(String[] args) {
        GraphqlClient client = new GraphqlClient(API_ENDPOINT);
        String token = client.GetLoginToken(API_USER, API_PASSPHRASE);

        client.changeUrl(API_ENDPOINT+"?token="+token);

        if (!client.IsLoggedIn())
        {
            System.out.println("Was not able to log in!");
            System.exit(1);
        }


        GraphqlClient.Job job = client.CreateEmailJob("Test","My subject");

        System.out.println(job.id);
        System.out.println("Success!");
    }
}
