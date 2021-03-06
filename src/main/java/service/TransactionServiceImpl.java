package service;

import database.DatabaseService;
import domain.Account;
import domain.Transaction;
import domain.User;
import helper.AuthorizationTool;
import operation.OperationType;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.mongodb.morphia.Datastore;
import service.rest.RestAuthenticationFilter;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by adam on 06.01.17.
 */

/**
 * Klasa pełniąca rolę serwisu usług transakcyjnych
 */
@WebService(endpointInterface = "service.TransactionService")
public class TransactionServiceImpl implements TransactionService{


    private AuthorizationTool authTool = new AuthorizationTool();

    /**
     * Wpłata własna
     * @param accountNumber
     * @param amount
     * @param encodedAuth
     * @return
     * @throws Exception
     */
    @WebMethod
    public Account doPayment(@WebParam(name="accountNumber") String accountNumber,
                          @WebParam(name="amount") double amount,
                          @WebParam(name="encodedAuth") String encodedAuth) throws Exception {
        User user = authTool.checkUserExistence(encodedAuth);
        Datastore datastore = DatabaseService.getDatastore();
        Account account = findUserAccountByAccountNumber(user.getAccounts(), accountNumber);
        if(amount < 0) {
            throw new Exception("The amount can not be less than 0.");
        }
        account.increaseBalance(amount);
        datastore.save(account);
        Transaction transaction = new Transaction("Wplata na konto",
                accountNumber,
                accountNumber,
                account.getBalance(),
                OperationType.Payment,
                amount,
                user);
        datastore.save(transaction);
        return account;
    }

    /**
     * Wypłata własna
     * @param accountNumber
     * @param amount
     * @param encodedAuth
     * @return
     * @throws Exception
     */
    @WebMethod
    public Account doWithdrawal(@WebParam(name="accountNumber") String accountNumber,
                             @WebParam(name="amount") double amount,
                             @WebParam(name="encodedAuth") String encodedAuth) throws Exception {
        User user = authTool.checkUserExistence(encodedAuth);
        Datastore datastore = DatabaseService.getDatastore();
        Account account = findUserAccountByAccountNumber(user.getAccounts(), accountNumber);
        if(amount > account.getBalance()) {
            throw new Exception("The amount is greater than the available funds in your account");
        } else if(amount < 0) {
            throw new Exception("The amount can not be less than 0.");
        } else {
            account.decreaseBalance(amount);
            datastore.save(account);
            Transaction transaction = new Transaction("Wyplata z konta",
                    accountNumber,
                    accountNumber,
                    account.getBalance(),
                    OperationType.Withdrawal,
                    amount,
                    user);
            datastore.save(transaction);
        }
        return account;
    }

    /**
     * Przelew wewnętrzny
     * @param sourceAccountNumber
     * @param targetAccountNumber
     * @param title
     * @param amount
     * @param encodedAuth
     * @return
     * @throws Exception
     */
    @WebMethod
    public Account doInternalTransfer(@WebParam(name="sourceAccountNumber") String sourceAccountNumber,
                                   @WebParam(name="targetAccountNumber") String targetAccountNumber,
                                   @WebParam(name="title") String title,
                                   @WebParam(name="amount") double amount,
                                   @WebParam(name="encodedAuth") String encodedAuth) throws Exception {
        User user = authTool.checkUserExistence(encodedAuth);
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
                    amount,
                    user);
            datastore.save(transaction);
            return sourceAccount;
        }
    }

    /**
     * Przelew zewnętrzny
     * @param sourceAccountNumber
     * @param targetAccountNumber
     * @param title
     * @param amount
     * @param encodedAuth
     * @return
     * @throws Exception
     */
    @WebMethod
    public int doExternalTransfer(@WebParam(name="sourceAccountNumber") String sourceAccountNumber,
                                                              @WebParam(name="targetAccountNumber") String targetAccountNumber,
                                                              @WebParam(name="title") String title,
                                                              @WebParam(name="amount") double amount,
                                                              @WebParam(name="encodedAuth") String encodedAuth) throws Exception {
        User user = authTool.checkUserExistence(encodedAuth);
        Datastore datastore = DatabaseService.getDatastore();
        Account sourceAccount = findUserAccountByAccountNumber(user.getAccounts(), sourceAccountNumber);
        int statusCode;
        JSONObject json = new JSONObject();
        HttpResponse response;

        if(amount > sourceAccount.getBalance()) {
            throw new Exception("The amount is greater than the available funds in your account");
        } else if(amount < 0) {
            throw new Exception("The amount can not be less than 0.");
        } else {
            String url = getReceiverUri(targetAccountNumber);
            if(url == null) {
                return Response.Status.CONFLICT.getStatusCode();
            }
            url += "/transfer";

            byte[] bytesAuth = Base64.encodeBase64((RestAuthenticationFilter.USERNAME + ":" + RestAuthenticationFilter.PASSWORD).getBytes());
            String encodedString = new String(bytesAuth);

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            json.put("sender_account", sourceAccountNumber);
            json.put("receiver_account", targetAccountNumber);
            json.put("title", title);
            json.put("amount", prepareAmount(amount));
            StringEntity se = new StringEntity(json.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            Header mtHeader = new BasicHeader("Authorization", "Basic " + encodedString);
            post.setHeader(mtHeader);
            response = httpClient.execute(post);
            statusCode = response.getStatusLine().getStatusCode();
            sourceAccount.decreaseBalance(amount);
            datastore.save(sourceAccount);
            Transaction transaction = new Transaction(title,
                    sourceAccountNumber,
                    targetAccountNumber,
                    sourceAccount.getBalance(),
                    OperationType.ExternalTransfer,
                    amount,
                    user);
            datastore.save(transaction);
        }
        return statusCode;
    }

    /**
     * Pobranie listy operacji
     * @param encodedAuth
     * @return
     */
    @WebMethod
    public List<Transaction> getTransactions(@WebParam(name="encodedAuth") String encodedAuth) {
        User user = authTool.checkUserExistence(encodedAuth);
        Datastore datastore = DatabaseService.getDatastore();
        List<Transaction> transactions = datastore.createQuery(Transaction.class).field("user").equal(user).asList();
        return transactions;
    }

    /**
     * Znajduje konto spośród przekazanej lsity kont na podstawie numeru konta
     * @param accounts
     * @param accountNumber
     * @return
     * @throws Exception
     */
    private Account findUserAccountByAccountNumber(List<Account> accounts, String accountNumber) throws Exception {
        Account acc = accounts.stream().filter(account -> account.getAccountNumber().equals(accountNumber)).findAny().orElse(null);
        if(acc == null) {
            throw new Exception("Account does not exists");
        }
        return acc;
    }

    /**
     * Zwraca uri odbiorcy
     * @param accountNumber
     * @return
     * @throws Exception
     */
    private String getReceiverUri(String accountNumber) throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream("src/main/resources/addresses.properties");
        properties.load(inputStream);
        String index = accountNumber.substring(4, 10);
        String url = properties.getProperty(index);
        return url;
    }

    /**
     * Przygotowuje kwotę dla przelewu zewnętrznego
     * @param amount
     * @return
     */
    private String prepareAmount(double amount) {
        String amountStr = Double.toString(amount);
        if(amountStr.contains(".") || amountStr.contains(",")) {
            amountStr = amountStr.replace(".", "");
            amountStr += "0";
        } else  {
            amountStr += "00";
        }
        return amountStr;
    }





}
