package service;

import database.DatabaseService;
import domain.Account;
import domain.Transaction;
import domain.User;
import helper.AuthorizationTool;
import operation.OperationType;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.mongodb.morphia.Datastore;
import service.rest.RestAuthenticationFilter;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

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

    @WebMethod
    public void doExternalTransfer(@WebParam(name="sourceAccountNumber") String sourceAccountNumber,
                                   @WebParam(name="targetAccountNumber") String targetAccountNumber,
                                   @WebParam(name="title") String title,
                                   @WebParam(name="amount") double amount) throws Exception {
        User user = authTool.checkUserExistence(context);
        Datastore datastore = DatabaseService.getDatastore();
        Account sourceAccount = findUserAccountByAccountNumber(user.getAccounts(), sourceAccountNumber);

        if(amount > sourceAccount.getBalance()) {
            throw new Exception("The amount is greater than the available funds in your account");
        } else if(amount < 0) {
            throw new Exception("The amount can not be less than 0.");
        } else {
            String url = getReceiverUrl(targetAccountNumber);
            url += "/transfer";
            HttpClient httpClient = new HttpClient();
            PostMethod postMethod = new PostMethod(url);
            byte[] bytesAuth = Base64.encodeBase64((RestAuthenticationFilter.USERNAME + ":" + RestAuthenticationFilter.PASSWORD).getBytes());
            String encodedAuth = new String(bytesAuth);
            Header mtHeader = new Header();
            mtHeader.setName("content-type");
            mtHeader.setValue("application/x-www-form-urlencoded");
            mtHeader.setName("accept");
            mtHeader.setValue("application/json");
            mtHeader.setName("Authorization");
            mtHeader.setValue("Basic " + encodedAuth);
            postMethod.addParameter("sender_account", sourceAccountNumber);
            postMethod.addParameter("receiver_account", targetAccountNumber);
            postMethod.addParameter("title", title);
            postMethod.addParameter("amount", prepareAmount(amount));
            httpClient.executeMethod(postMethod);
            String output = postMethod.getResponseBodyAsString();
            postMethod.releaseConnection();
            System.out.println("Output: " + output);
        }

    }


    private Account findUserAccountByAccountNumber(List<Account> accounts, String accountNumber) throws Exception {
        Account acc = accounts.stream().filter(account -> account.getAccountNumber().equals(accountNumber)).findAny().orElse(null);
        if(acc == null) {
            throw new Exception("Account does not exists");
        }
        return acc;
    }

    private String getReceiverUrl(String accountNumber) throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream("addresses.properties");
        properties.load(inputStream);
        String index = accountNumber.substring(4, 10);
        String url = properties.getProperty(index);
        if(url == null) {
            throw new Exception("Receiver bank does not exists.");
        }
        return url;
    }

    private String prepareAmount(double amount) {
        String amountStr = Double.toString(amount);
        amountStr = amountStr.replaceAll(".", "");
        return amountStr;
    }





}
