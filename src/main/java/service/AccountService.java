package service;

import database.DatabaseService;
import domain.Account;
import domain.User;
import helper.AuthorizationTool;
import org.mongodb.morphia.Datastore;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by adam on 06.01.17.
 */

@WebService
public class AccountService {

    @Resource
    private WebServiceContext context;

    private AuthorizationTool authTool = new AuthorizationTool();

    private static final String BANK_NUMBER = "00109683";

    @WebMethod
    public Account addAccount() {
        System.out.println("Adding new account.");
        Datastore datastore = DatabaseService.getDatastore();
        User user = authTool.checkUserExistence(context);
        Account account = new Account(generateAccountNumber(datastore));
        user.addAccount(account);
        datastore.save(account);
        datastore.save(user);
        return account;
    }


    @WebMethod
    public List<Account> getAccounts() {
        System.out.println("Getting user accounts.");
        User user = authTool.checkUserExistence(context);
        return user.getAccounts();
    }

    private String generateAccountNumber(Datastore datastore) {
        String accountNumber = generateCheckSum() + BANK_NUMBER;
        List<Account> accounts = datastore.find(Account.class).asList();
        List<Long> numbers = new ArrayList<>();
        if(accounts != null) {
            for(Account account : accounts) {
                numbers.add(Long.valueOf(account.getAccountNumber().substring(8, 26)));
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
