package service;

import database.DatabaseService;
import domain.User;
import helper.AuthorizationTool;
import org.glassfish.jersey.internal.util.Base64;
import org.mongodb.morphia.Datastore;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.StringTokenizer;

/**
 * Created by adam on 06.01.17.
 */

@WebService(endpointInterface = "service.UserService")
public class UserServiceImpl implements UserService {

    private AuthorizationTool authTool = new AuthorizationTool();

    @WebMethod
    public boolean registerUser(@WebParam(name="encodedAuth") String encodedAuth) throws Exception {
        try {
            System.out.println("User registration");
            Datastore datastore = DatabaseService.getDatastore();
            String usernameAndPassword = new String(Base64.decode(encodedAuth.getBytes()));
            final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
            final String userName = tokenizer.nextToken();
            final String password = tokenizer.nextToken();
            byte[] bytesEncodedPass = org.apache.commons.codec.binary.Base64.encodeBase64(password.getBytes());
            String encodedPassword = new String(bytesEncodedPass);
            User user = new User(userName, encodedPassword);
            User userFromDb = datastore.createQuery(User.class).field("userName").equal(userName).get();
            if(userFromDb != null) {
                //throw new Exception("User already exists");
                return false;
            }
            datastore.save(user);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @WebMethod
    public boolean login(@WebParam(name="encodedAuth") String encodedAuth) {
        User user = authTool.checkUserExistence(encodedAuth);
        if(user != null) {
            return true;
        }
        return false;
    }




}
