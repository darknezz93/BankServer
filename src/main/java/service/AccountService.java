package service;

import database.DatabaseService;
import domain.Account;
import org.mongodb.morphia.Datastore;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by adam on 06.01.17.
 */

@WebService
public class AccountService {

    @WebMethod
    public Account addAccount() {
        System.out.println("Adding new account.");
        Datastore datastore = DatabaseService.getDatastore();
        Account account = new Account(generateIBAN(datastore));
        String index = "109683";
        datastore.save(account);
        return account;
    }

    @WebMethod
    public List<Account> getAccounts() {
        System.out.println("Getting user accounts.");
        Datastore datastore = DatabaseService.getDatastore();
        //Pobranie usera oraz jego kont
        return null;
    }

    private String generateIBAN(Datastore datastore) {
        return "NieWartoBylo";
    }
}
