package service;

import domain.Account;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by adam on 16.01.17.
 */

/**
 * Interfejs dla usługi obsługującej konta
 */
@WebService
public interface AccountService {

    /**
     * Dodaje nowy rachunek bankowy
     * @param encodedAuth
     * @return
     */
    @WebMethod
    public Account addAccount(@WebParam(name="encodedAuth") String encodedAuth);

    /**
     * Pobiera rachunki użytkownika
     * @param encodedAuth
     * @return
     */
    @WebMethod
    public List<Account> getAccounts(@WebParam(name="encodedAuth") String encodedAuth);

}
