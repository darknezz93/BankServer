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

@WebService(endpointInterface = "service.AccountService")
public class AccountServiceImpl implements AccountService{

    private AuthorizationTool authTool = new AuthorizationTool();

    private static final String BANK_NUMBER = "00109683";

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


    @WebMethod
    public List<Account> getAccounts(@WebParam(name="encodedAuth") String encodedAuth) {
        System.out.println("Getting user accounts.");
        User user = authTool.checkUserExistence(encodedAuth);
        return user.getAccounts();
    }

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


    private String generateCheckSum() {
        return "00";
    }
}
