package service;

import database.DatabaseService;
import domain.User;
import helper.AuthorizationTool;
import org.mongodb.morphia.Datastore;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by adam on 06.01.17.
 */

@WebService
public class UserService {

    private AuthorizationTool authTool = new AuthorizationTool();

    @WebMethod
    public void registerUser(@WebParam(name="userName") String userName,
                             @WebParam(name="password") String password) throws Exception {
        System.out.println("User registration");
        Datastore datastore = DatabaseService.getDatastore();
        User user = new User(userName, password);
        User userFromDb = datastore.createQuery(User.class).field("userName").equal(userName).get();
        if(userFromDb == null) {
            throw new Exception("User already exists");
        } else {
            datastore.save(user);
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
