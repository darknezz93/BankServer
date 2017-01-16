package service;

import domain.Account;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by adam on 16.01.17.
 */

@WebService
public interface AccountService {

    @WebMethod
    public Account addAccount(@WebParam(name="encodedAuth") String encodedAuth);

    @WebMethod
    public List<Account> getAccounts(@WebParam(name="encodedAuth") String encodedAuth);

    @WebMethod
    public List<Account> getOtherAccounts(@WebParam(name="encodedAuth") String encodedAuth);
}
