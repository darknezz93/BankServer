package clientApp.controllers;

import clientApp.helper.AccountNumberTextField;
import clientApp.helper.AmountTextField;
import clientApp.helper.ClientAuth;
import domain.Account;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import service.AccountService;
import service.TransactionService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adam on 17.01.17.
 */

/**
 * Kontroler odpowiedzialny za okno przelewów zewnętrznych
 */
public class ExternalTransferController {

    @FXML
    private ComboBox<Account> senderAccountComboBox;

    @FXML
    private AccountNumberTextField receiverAccountTextField;

    @FXML
    private AmountTextField amountTextField;

    @FXML
    private TextField titleTextField;

    @FXML
    private Button doExternalTransferButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Label successLabel;

    @FXML
    private Label balanceLabel;

    private List<Account> senderAccounts = new ArrayList<>();

    /**
     * Metoda inicjalizująca dane. Wywoływana po utworzeniu okna
     * @throws MalformedURLException
     */
    @FXML
    public void initialize() throws MalformedURLException {
        AccountService accountService = getAccountService();
        senderAccounts = accountService.getAccounts(ClientAuth.getEncodedAuth());
        initializeSenderAccountComboBox();
    }

    /**
     * Metoda inicjująca dla combo boxów z rachunkami
     */
    private void initializeSenderAccountComboBox() {
        senderAccountComboBox.setItems(FXCollections.observableArrayList(senderAccounts));
        senderAccountComboBox.setConverter(new StringConverter<Account>() {
            @Override
            public String toString(Account object) {
                return object.getAccountNumber();
            }

            @Override
            public Account fromString(String string) {
                return null;
            }
        });

        senderAccountComboBox.valueProperty().addListener(new ChangeListener<Account>() {
            @Override
            public void changed(ObservableValue<? extends Account> observable, Account oldValue, Account newValue) {
                if(newValue != null) {
                    balanceLabel.setText(String.valueOf(newValue.getBalance()));
                }
            }
        });
    }

    /**
     * Metoda wywoływana po naciśnięciu przycisku odpowiedzalnego za przelew zewnętrzny
     * @throws Exception
     */
    @FXML
    public void listenExternalTransferButton() throws Exception {
        resetLabels();
        if(senderAccountComboBox.getSelectionModel().getSelectedItem() == null ||
                receiverAccountTextField.getText().equals("")) {
            errorLabel.setText("Wybierz oba numery rachunków");
            return;
        }
        if(senderAccountComboBox.getSelectionModel().getSelectedItem().getAccountNumber()
                .equals(receiverAccountTextField.getText())) {
            errorLabel.setText("Rachunek odbiorcy powinien znajdować się w innym banku");
            return;

        }
        if(receiverAccountTextField.getText().length() < 26) {
            errorLabel.setText("Numer rachunku powinien skladac sie z 26 cyfr.");
            return;
        }
        String senderAccountNumber = senderAccountComboBox.getSelectionModel().getSelectedItem().getAccountNumber();
        String receiverAccountNumber = receiverAccountTextField.getText();
        if (amountTextField.getText().equals("")) {
            errorLabel.setText("Wprowadź kwotę");
            return;
        }
        int amount = Integer.valueOf(amountTextField.getText());
        if(amount > senderAccountComboBox.getSelectionModel().getSelectedItem().getBalance()) {
            errorLabel.setText("Kwota nie może być większa niż stan konta nadawcy.");
            return;
        }
        if(titleTextField.getText().equals("")) {
            errorLabel.setText("Wprowadź tytuł transakcji");
            return;
        }
        String title = titleTextField.getText();
        TransactionService trnService = getTransactionService();
        int response = trnService.doExternalTransfer(senderAccountNumber,
                receiverAccountNumber,
                title,
                amount,
                ClientAuth.getEncodedAuth());
        if(response == 401) {
            errorLabel.setText("Brak autoryzacji uzytkownika");
        }
        if(response == 201) {
            refreshComboBoxes(amount);
            successLabel.setText("Przelew zewnętrzny zakończony pozytywnie.");
        } else if(response == 400) {
            errorLabel.setText("Kwota musi być większa niż zero.");
        } else if(response == 404) {
            errorLabel.setText("Numer rachunku odbiorcy nie istnieje.");
        } else if(response == 403) {
            errorLabel.setText("Numer rachunku odbiorcy musi znajdować się w innym banku");
        } else if(response == 409){
            errorLabel.setText("Bank odbiorcy nie istnieje.");
        } else if(response == 500){
            errorLabel.setText("Błąd serwera.");
        }
    }

    /**
     * Metoda zwracajaca accountService
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
     * Metoda zwracająca transactionService
     * @return
     * @throws MalformedURLException
     */
    private TransactionService getTransactionService() throws MalformedURLException {
        URL url = new URL("http://localhost:8000/transaction?wsdl");
        QName qname = new QName("http://service/", "TransactionServiceImplService");
        Service service = Service.create(url, qname);
        TransactionService webService = service.getPort(TransactionService.class);
        return webService;
    }

    /**
     * Metoda uaktualniajaca zawartość comboBoxów
     * @param amount
     * @throws MalformedURLException
     */
    private void refreshComboBoxes(double amount) throws MalformedURLException {
        AccountService accountService = getAccountService();
        senderAccounts = accountService.getAccounts(ClientAuth.getEncodedAuth());
        Account senderAcc = senderAccountComboBox.getSelectionModel().getSelectedItem();
        senderAccountComboBox.setItems(FXCollections.observableArrayList(senderAccounts));
        senderAccountComboBox.getSelectionModel().select(senderAcc);
        balanceLabel.setText(String.valueOf(senderAcc.getBalance() - amount));
    }

    /**
     * Metoda resetująca zawartość labelek z błędami lub informacjami dla użytkownika
     */
    private void resetLabels() {
        errorLabel.setText("");
        successLabel.setText("");
    }
}
