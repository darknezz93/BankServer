package test;

import service.UserService;

import javax.xml.ws.WebServiceRef;
import java.net.MalformedURLException;

/**
 * Created by adam on 07.01.17.
 */
public class TestUserService {

    @WebServiceRef(wsdlLocation="http://localhost:8000/user/registerUser?wsdl")
    static UserService userService = new UserService();


    public static void main(String[] args) throws MalformedURLException {
        TestUserService testSoapService = new TestUserService();
        testSoapService.doTestRegisterUser();
    }


    public void doTestRegisterUser() {
        try {
            userService.registerUser("adam", "niewartobylo");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
