package service;

import database.DatabaseService;
import domain.Account;
import domain.User;
import helper.AuthorizationTool;
import org.mongodb.morphia.Datastore;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by adam on 06.01.17.
 */

/**
 * Klasa odpowiedzalna ze pełnienie roli serwisu usług dotyczących rachunków
 */
@WebService(endpointInterface = "service.AccountService")
public class AccountServiceImpl implements AccountService{

    private AuthorizationTool authTool = new AuthorizationTool();

    private static final String BANK_NUMBER = "00109683";

    /**
     * Dodaje nowe konto
     * @param encodedAuth
     * @return
     */
    @WebMethod
    public Account addAccount(@WebParam(name="encodedAuth") String encodedAuth) {
        System.out.println("Adding new account.");
        Datastore datastore = DatabaseService.getDatastore();
        User user = authTool.checkUserExistence(encodedAuth);
        Account account = new Account(generateAccountNumber(datastore));
        user.addAccount(account);
        datastore.save(account);
        datastore.save(user);
        return account;
    }

    /**
     * Pobiera konta użytkownika
     * @param encodedAuth
     * @return
     */
    @WebMethod
    public List<Account> getAccounts(@WebParam(name="encodedAuth") String encodedAuth) {
        System.out.println("Getting user accounts.");
        User user = authTool.checkUserExistence(encodedAuth);
        return user.getAccounts();
    }

    /**
     * Tworzy nowy rachunek bankowy
     * @param datastore
     * @return
     */
    private String generateAccountNumber(Datastore datastore) {
        String accountNumber = generateCheckSum() + BANK_NUMBER;
        List<Account> accounts = datastore.find(Account.class).asList();
        List<Long> numbers = new ArrayList<>();
        if(accounts.size() != 0) {
            for(Account account : accounts) {
                numbers.add(Long.valueOf(account.getAccountNumber().substring(10, 26)));
            }
            Long max = Collections.max(numbers);
            accountNumber += Long.toString(max+1);
        } else {
            accountNumber += "1000000000000000";
        }
        return accountNumber;
    }

    /**
     * Zwraca sumę kontrolną
     * @return
     */
    private String generateCheckSum() {
        return "00";
    }
}
