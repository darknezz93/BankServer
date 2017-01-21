package service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by adam on 16.01.17.
 */

/**
 * Interfejs dla serwisu usług użytkownika
 */
@WebService
public interface UserService {

    /**
     * Rejestracja użytkownika
     * @param encodedAuth
     * @return
     * @throws Exception
     */
    @WebMethod
    public boolean registerUser(@WebParam(name="encodedAuth") String encodedAuth) throws Exception;

    /**
     * Logowanie użytkownika
     * @param encodedAuth
     * @return
     */
    @WebMethod
    public boolean login(@WebParam(name="encodedAuth") String encodedAuth);
}
