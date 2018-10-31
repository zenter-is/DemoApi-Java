

public class Example2
{
    public static final String API_ENDPOINT = "https://vrs.africaprudential.com/Api/V2";

    public static final String API_USER = "483_api@zenter.is";
    public static final String API_PASSPHRASE = "ea4941d2771a750d0bb50585429ab300";


    public static void main(String[] args) {
        GraphqlClient client = new GraphqlClient(API_ENDPOINT);
        String token = client.GetLoginToken(API_USER, API_PASSPHRASE);

        client.changeUrl(API_ENDPOINT+"?token="+token);

        if (!client.IsLoggedIn())
        {
            System.out.println("Was not able to log in!");
            System.exit(1);
        }

        System.out.println("Success!");
    }
}
