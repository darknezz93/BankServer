package clientApp.helper;

/**
 * Created by adam on 16.01.17.
 */
public class ClientAuth {
    private static String encodedAuth = "";

    public static String getEncodedAuth() {
        return encodedAuth;
    }

    public static void setEncodedAuth(String encodedAuth) {
        ClientAuth.encodedAuth = encodedAuth;
    }
}
