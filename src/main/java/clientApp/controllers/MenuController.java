package clientApp.controllers;

import clientApp.helper.AlertMessage;
import clientApp.helper.ClientAuth;
import domain.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import service.AccountService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by adam on 16.01.17.
 */

/**
 * Kontroler menu
 */
public class MenuController {

    @FXML
    private Button paymentButton;

    @FXML
    private Button withdrawalButton;

    @FXML
    private Button internalTransferButton;

    @FXML
    private Button externalTransferButton;

    @FXML
    private Button operationHistoryButton;

    @FXML
    private Button addAccountButton;

    /**
     * Otwiera okno płat własnych
     * @throws IOException
     */
    @FXML
    public void listenPaymentButton() throws IOException {
        openNewWindow("src/main/java/clientApp/layouts/payment.fxml", "Wpłata na konto");
    }

    /**
     * Otwiera okno wypłat własnych
     * @throws IOException
     */
    @FXML
    public void listenWithdrawalButton() throws IOException {
        openNewWindow("src/main/java/clientApp/layouts/withdrawal.fxml", "Wypłata z konta");
    }

    /**
     * Otwiera okno przelewów wewnętrznych
     * @throws IOException
     */
    @FXML
    public void listenInternalTransferButton() throws IOException {
        openNewWindow("src/main/java/clientApp/layouts/internalTransfer.fxml", "Przelew wewnątrzbankowy");
    }

    /**
     * Otwiera okno przelewów zewnętrznych
     * @throws IOException
     */
    @FXML
    public void listenExternalTransferButton() throws IOException {
        openNewWindow("src/main/java/clientApp/layouts/externalTransfer.fxml", "Przelew zewnętrzny");
    }

    /**
     * Otwiera okno historii operacji
     * @throws IOException
     */
    @FXML
    public void listenOperationHistoryButton() throws IOException {
        openNewWindow("src/main/java/clientApp/layouts/transactionHistory.fxml", "Historia operacji");
    }

    /**
     * Metoda wywoływana po naćiśnięciu przycisku dodawania nowego konta
     * @throws MalformedURLException
     */
    @FXML
    public void listenAddAccountButton() throws MalformedURLException {
        AccountService accountService = getAccountService();
        Account account = accountService.addAccount(ClientAuth.getEncodedAuth());
        if(account == null) {
            AlertMessage.errorBox("Wystąpił błąd podczas tworzenia konta.", "Błąd serwera");
        }
        String message = "Dodano konto o numerze: " + account.getAccountNumber();
        AlertMessage.infoBox(message, "Konto zostało utworzone");
    }

    /**
     * Zwraca accountService
     * @return
     * @throws MalformedURLException
     */
    private AccountService getAccountService() throws MalformedURLException {
        URL url = new URL("http://localhost:8000/account?wsdl");
        QName qname = new QName("http://service/", "AccountServiceImplService");
        Service service = Service.create(url, qname);
        AccountService webService = service.getPort(AccountService.class);
        return webService;
    }

    /**
     * Metoda otwierająca nowe okno
     * @param fxmlFilePath - scieżka do pliku fxml opisującego nowe okno
     * @param windowTitle - tytuł okna
     * @throws IOException
     */
    private void openNewWindow(String fxmlFilePath, String windowTitle) throws IOException {
        URL url = new File(fxmlFilePath).toURL();
        Parent root1 = FXMLLoader.load(url);
        Stage stage = new Stage();
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root1));
        stage.show();
    }

}
