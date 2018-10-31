

public class Example2
{
    public static void main(String[] args) {

        String postUrl = "http://zenter.local/api/v2";
        GraphqlClient client = new GraphqlClient(postUrl);

        String token = client.GetLoginToken("1_api@zenter.is", "a2d80ec3929f885b55d8e403b97a4da3");

        client.changeUrl(postUrl+"?token="+token);

        if (!client.IsLoggedIn())
        {
            System.out.println("Was not able to log in!");
            System.exit(1);
        }

        System.out.println("Success!");
    }
}
