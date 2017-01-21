package database;

import com.mongodb.MongoClient;
import domain.Account;
import domain.Transaction;
import domain.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by adam on 06.01.17.
 */

/**
 * Klasa odpowiedzialna za nawiązanie połączenia z bazą danych
 */
public class DatabaseService {

    private static Datastore datastore;

    /**
     * Zwraca obiekt datastore, który umożliwia interakcję z bazą danych mongoDb
     * @return
     */
    public static Datastore getDatastore() {
        Morphia morphia = new Morphia();
        MongoClient mongoClient = new MongoClient("localhost", 8004);
        morphia.map(Account.class);
        morphia.map(Transaction.class);
        morphia.map(User.class);
        datastore = morphia.createDatastore(mongoClient, "bank");
        return datastore;
    }

    /**
     * Znajduje rachunek na podstawie numeru rachunku
     * @param datastore
     * @param targetAccountNumber
     * @return
     * @throws Exception
     */
    public static Account findAccountByAccountNumber(Datastore datastore, String targetAccountNumber) throws Exception {
        Account account = datastore.createQuery(Account.class).field("accountNumber").equal(targetAccountNumber).get();
        return account;
    }


}
