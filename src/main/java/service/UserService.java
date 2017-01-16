package service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by adam on 16.01.17.
 */

@WebService
public interface UserService {

    @WebMethod
    public boolean registerUser(@WebParam(name="encodedAuth") String encodedAuth) throws Exception;

    @WebMethod
    public boolean login(@WebParam(name="encodedAuth") String encodedAuth);
}
