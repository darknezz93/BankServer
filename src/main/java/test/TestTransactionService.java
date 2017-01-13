package test;

import service.TransactionService;

import javax.xml.ws.WebServiceRef;
import java.net.MalformedURLException;

/**
 * Created by adam on 13.01.17.
 */
public class TestTransactionService {

    @WebServiceRef(wsdlLocation="http://localhost:8000/transaction/doPayment?wsdl")
    static TransactionService transactionService = new TransactionService();


    public static void main(String[] args) throws MalformedURLException {
        TestTransactionService testSoapService = new TestTransactionService();
        testSoapService.doTestDoPayment();
    }


    public void doTestDoPayment() {
        try {
            transactionService.doPayment("213123123213", 78);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
