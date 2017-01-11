package service;

import database.DatabaseService;
import domain.Account;
import domain.Transaction;
import domain.User;
import helper.AuthorizationTool;
import operation.OperationType;
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
public class TransactionService {

    @Resource
    private WebServiceContext context;

    private AuthorizationTool authTool = new AuthorizationTool();


    @WebMethod
    public void doPayment(@WebParam(name="accountNumber") String accountNumber,
                          @WebParam(name="amount") double amount) throws Exception {
        User user = authTool.checkUserExistence(context);
        Datastore datastore = DatabaseService.getDatastore();
        Account account = findUserAccountByAccountNumber(user.getAccounts(), accountNumber);
            if(amount < 0) {
                throw new Exception("The amount can not be less than 0.");
            } else {
                account.decreaseBalance(amount);
                datastore.save(account);
        }
        account.increaseBalance(amount);
        datastore.save(account);
    }

    @WebMethod
    public void doWithdrawal(@WebParam(name="accountNumber") String accountNumber,
                             @WebParam(name="amount") double amount) throws Exception {
        User user = authTool.checkUserExistence(context);
        Datastore datastore = DatabaseService.getDatastore();
        Account account = findUserAccountByAccountNumber(user.getAccounts(), accountNumber);
        if(amount > account.getBalance()) {
            throw new Exception("The amount is greater than the available funds in your account");
        } else if(amount < 0) {
            throw new Exception("The amount can not be less than 0.");
        } else {
            account.decreaseBalance(amount);
            datastore.save(account);
        }
    }

    @WebMethod
    public void doInternalTransfer(@WebParam(name="sourceAccountNumber") String sourceAccountNumber,
                                   @WebParam(name="targetAccountNumber") String targetAccountNumber,
                                   @WebParam(name="title") String title,
                                   @WebParam(name="amount") double amount) throws Exception {
        User user = authTool.checkUserExistence(context);
        Datastore datastore = DatabaseService.getDatastore();
        Account sourceAccount = findUserAccountByAccountNumber(user.getAccounts(), sourceAccountNumber);
        Account targetAccount = DatabaseService.findAccountByAccountNumber(datastore, targetAccountNumber);
        if(amount > sourceAccount.getBalance()) {
            throw new Exception("The amount is greater than the available funds in your account");
        } else if(amount < 0) {
            throw new Exception("The amount can not be less than 0.");
        } else {
            sourceAccount.decreaseBalance(amount);
            targetAccount.increaseBalance(amount);
            datastore.save(sourceAccount);
            datastore.save(targetAccount);
            Transaction transaction = new Transaction(title,
                    sourceAccountNumber,
                    targetAccountNumber,
                    sourceAccount.getBalance(),
                    OperationType.InternalTransfer,
                    amount);
            datastore.save(transaction);

        }
    }


    private Account findUserAccountByAccountNumber(List<Account> accounts, String accountNumber) throws Exception {
        Account acc = accounts.stream().filter(account -> account.getAccountNumber().equals(accountNumber)).findAny().orElse(null);
        if(acc == null) {
            throw new Exception("Account does not exists");
        }
        return acc;
    }





}
