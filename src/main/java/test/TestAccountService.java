package test;

import domain.Account;
import service.AccountService;

import javax.xml.ws.WebServiceRef;
import java.net.MalformedURLException;
import java.util.List;


/**
 * Created by adam on 07.01.17.
 */
public class TestAccountService {

    @WebServiceRef(wsdlLocation="http://localhost:8000/account/addAccount?wsdl")
    static AccountService accountService = new AccountService();

    private static final String WS_URL = "http://localhost:8000/account/addAccount?wsdl";


    public static void main(String[] args) throws MalformedURLException {
        TestAccountService testSoapService = new TestAccountService();
        testSoapService.doTestAddAccount();
        testSoapService.doTestGetAccounts();
    }


    public void doTestAddAccount() {
        try {
            Account account = accountService.addAccount("YWRtaW46cGFzc3dvcmQ=");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void doTestGetAccounts() {
        try {
            List<Account> accounts = accountService.getAccounts("YWRtaW46cGFzc3dvcmQ=");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
