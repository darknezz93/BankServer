package helper;

import database.DatabaseService;
import domain.User;
import org.glassfish.jersey.internal.util.Base64;
import org.mongodb.morphia.AuthenticationException;
import org.mongodb.morphia.Datastore;

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by adam on 08.01.17.
 */
public class AuthorizationTool {

    public User checkUserExistence(WebServiceContext webServiceContext) {
        MessageContext messageContext = webServiceContext.getMessageContext();
        Map httpHeaders = (Map) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        String userName = (String) httpHeaders.get("username");
        String password = (String) httpHeaders.get("password");
        Datastore datastore = DatabaseService.getDatastore();
        User user = (User) datastore.find(User.class).field("username").equal(userName).field("password").equal(password);
        if(user == null) {
            throw new AuthenticationException("Username or password invalid.");
        }
        return user;
    }

    public User checkUserExistence(String encodedAuth) {
        String usernameAndPassword = new String(Base64.decode(encodedAuth.getBytes()));
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String userName = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        Datastore datastore = DatabaseService.getDatastore();

        User user = datastore.find(User.class).field("userName").equal(userName).field("password").equal(password).get();
        if(user == null) {
            throw new AuthenticationException("Username or password invalid.");
        }
        return user;

    }

}
