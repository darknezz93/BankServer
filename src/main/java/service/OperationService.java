package service;

import database.DatabaseService;
import domain.Account;
import domain.User;
import helper.AuthorizationTool;
import org.mongodb.morphia.Datastore;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import java.util.List;

/**
 * Created by adam on 06.01.17.
 */

@WebService
public class OperationService {

    @Resource
    private WebServiceContext context;

    private AuthorizationTool authTool = new AuthorizationTool();

    @WebMethod
    public void doPayment(@WebParam(name="accountNumber") String accountNumber,
                          @WebParam(name="amount") double amount) throws Exception {
        User user = authTool.checkUserExistence(context);
        Datastore datastore = DatabaseService.getDatastore();
        Account account = findUserAccountByAccountNumber(user.getAccounts(), accountNumber);
        if(account == null) {
            throw new Exception("Account does not exists.");
        }
        account.updateBalance(amount);
    }

    @WebMethod
    public void doWithdrawal() {

    }

    @WebMethod
    public void doInternalTransfer() {

    }

    @WebMethod
    public void doExternalTransfer() {

    }

    private Account findUserAccountByAccountNumber(List<Account> accounts, String accountNumber) {
        return accounts.stream().filter(account -> account.getAccountNumber().equals(accountNumber)).findAny().orElse(null);
    }



}
