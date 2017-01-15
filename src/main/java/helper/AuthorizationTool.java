package helper;

import database.DatabaseService;
import domain.User;
import org.glassfish.jersey.internal.util.Base64;
import org.mongodb.morphia.AuthenticationException;
import org.mongodb.morphia.Datastore;

import java.util.StringTokenizer;

/**
 * Created by adam on 08.01.17.
 */
public class AuthorizationTool {

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
