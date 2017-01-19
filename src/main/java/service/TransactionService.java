package service;

import domain.Account;
import domain.Transaction;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by adam on 16.01.17.
 */

@WebService
public interface TransactionService {

    @WebMethod
    public Account doPayment(@WebParam(name="accountNumber") String accountNumber,
                             @WebParam(name="amount") double amount,
                             @WebParam(name="encodedAuth") String encodedAuth) throws Exception;

    @WebMethod
    public Account doWithdrawal(@WebParam(name="accountNumber") String accountNumber,
                             @WebParam(name="amount") double amount,
                             @WebParam(name="encodedAuth") String encodedAuth) throws Exception;

    @WebMethod
    public Account doInternalTransfer(@WebParam(name="sourceAccountNumber") String sourceAccountNumber,
                                   @WebParam(name="targetAccountNumber") String targetAccountNumber,
                                   @WebParam(name="title") String title,
                                   @WebParam(name="amount") double amount,
                                   @WebParam(name="encodedAuth") String encodedAuth) throws Exception;

    @WebMethod
    public int doExternalTransfer(@WebParam(name="sourceAccountNumber") String sourceAccountNumber,
                                                              @WebParam(name="targetAccountNumber") String targetAccountNumber,
                                                              @WebParam(name="title") String title,
                                                              @WebParam(name="amount") double amount,
                                                              @WebParam(name="encodedAuth") String encodedAuth) throws Exception;

    @WebMethod
    public List<Transaction> getTransactions(@WebParam(name="encodedAuth") String encodedAuth);


}
