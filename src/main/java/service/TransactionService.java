package service;

import domain.Account;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

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
    public void doWithdrawal(@WebParam(name="accountNumber") String accountNumber,
                             @WebParam(name="amount") double amount,
                             @WebParam(name="encodedAuth") String encodedAuth) throws Exception;

    @WebMethod
    public void doInternalTransfer(@WebParam(name="sourceAccountNumber") String sourceAccountNumber,
                                   @WebParam(name="targetAccountNumber") String targetAccountNumber,
                                   @WebParam(name="title") String title,
                                   @WebParam(name="amount") double amount,
                                   @WebParam(name="encodedAuth") String encodedAuth) throws Exception;

    @WebMethod
    public void doExternalTransfer(@WebParam(name="sourceAccountNumber") String sourceAccountNumber,
                                   @WebParam(name="targetAccountNumber") String targetAccountNumber,
                                   @WebParam(name="title") String title,
                                   @WebParam(name="amount") double amount,
                                   @WebParam(name="encodedAuth") String encodedAuth) throws Exception;


}
