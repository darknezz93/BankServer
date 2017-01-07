package test;

import domain.Account;
import service.AccountService;
import service.UserService;

import javax.xml.ws.WebServiceRef;
import java.net.MalformedURLException;

/**
 * Created by adam on 07.01.17.
 */
public class TestAccountService {

    @WebServiceRef(wsdlLocation="http://localhost:8000/account/addAccount?wsdl")
    static AccountService userService = new AccountService();


    public static void main(String[] args) throws MalformedURLException {
        TestAccountService testSoapService = new TestAccountService();
        testSoapService.doTestAddAccount();
    }





    public void doTestAddAccount() {
        try {
            Account account = userService.addAccount();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
