package test;

import service.TransactionServiceImpl;

import javax.xml.ws.WebServiceRef;
import java.net.MalformedURLException;

/**
 * Created by adam on 13.01.17.
 */
public class TestTransactionService {

    @WebServiceRef(wsdlLocation="http://localhost:8000/transaction/doPayment?wsdl")
    static TransactionServiceImpl transactionService = new TransactionServiceImpl();


    public static void main(String[] args) throws MalformedURLException {
        TestTransactionService testSoapService = new TestTransactionService();
        testSoapService.doTestDoPayment();
    }


    public void doTestDoPayment() {
        try {
           // transactionService.doPayment("213123123213", 78);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
