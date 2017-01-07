package database;

import com.mongodb.MongoClient;
import domain.Account;
import domain.Operation;
import domain.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by adam on 06.01.17.
 */
public class DatabaseService {

    private static Datastore datastore;

    public static Datastore getDatastore() {
        Morphia morphia = new Morphia();
        MongoClient mongoClient = new MongoClient("localhost", 8004);
        morphia.map(Account.class);
        morphia.map(Operation.class);
        morphia.map(User.class);
        datastore = morphia.createDatastore(mongoClient, "bank");
        return datastore;
    }



}
