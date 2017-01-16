package clientApp.controllers;

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
 * Created by adam on 16.01.17.
 */
public class InternalTransferController {

    @FXML
    private ComboBox<Account> senderAccountComboBox;

    @FXML
    private ComboBox<Account> receiverAccountComboBox;

    @FXML
    private AmountTextField amountTextField;

    @FXML
    private TextField titleTextField;

    @FXML
    private Button doInternalTransferButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Label successLabel;

    @FXML
    private Label balanceLabel;

    private List<Account> senderAccounts = new ArrayList<>();

    private List<Account> receiverAccounts = new ArrayList<>();

    @FXML
    public void initialize() throws MalformedURLException {
        AccountService accountService = getAccountService();
        senderAccounts = accountService.getAccounts(ClientAuth.getEncodedAuth());
        receiverAccounts = accountService.getOtherAccounts(ClientAuth.getEncodedAuth());
        initializeSenderAccountComboBox();
        initializeReceiverAccountComboBox();

    }

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
                balanceLabel.setText(String.valueOf(newValue.getBalance()));
            }
        });
    }

    private void initializeReceiverAccountComboBox() {
        receiverAccountComboBox.setItems(FXCollections.observableArrayList(receiverAccounts));
        receiverAccountComboBox.setConverter(new StringConverter<Account>() {
            @Override
            public String toString(Account object) {
                return object.getAccountNumber();
            }

            @Override
            public Account fromString(String string) {
                return null;
            }
        });
    }


    @FXML
    public void listenInternalTransferButton() throws Exception {
        resetLabels();
        if(senderAccountComboBox.getSelectionModel().getSelectedItem() == null ||
                receiverAccountComboBox.getSelectionModel().getSelectedItem() == null) {
            errorLabel.setText("Wybierz oba numery rachunków");
            return;
        }
        String senderAccountNumber = senderAccountComboBox.getSelectionModel().getSelectedItem().getAccountNumber();
        String receiverAccountNumber = receiverAccountComboBox.getSelectionModel().getSelectedItem().getAccountNumber();
        if (amountTextField.getText().equals("")) {
            errorLabel.setText("Wprowadź kwotę");
            return;
        }
        double amount = Double.valueOf(amountTextField.getText());
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
        Account account = trnService.doInternalTransfer(senderAccountNumber,
                receiverAccountNumber,
                title,
                amount,
                ClientAuth.getEncodedAuth());
        updateAccounts(account);
        successLabel.setText("Przelew wewnątrzbankowy zakończony pozytywnie.");
    }

    private AccountService getAccountService() throws MalformedURLException {
        URL url = new URL("http://localhost:8000/account?wsdl");
        QName qname = new QName("http://service/", "AccountServiceImplService");
        Service service = Service.create(url, qname);
        AccountService webService = service.getPort(AccountService.class);
        return webService;
    }

    private void updateAccounts(Account account) {
        for(Account acc : senderAccounts) {
            if(account.getAccountNumber().equals(acc.getAccountNumber())) {
                acc = account;
                balanceLabel.setText(String.valueOf(acc.getBalance()));
                return;
            }
        }
    }

    private TransactionService getTransactionService() throws MalformedURLException {
        URL url = new URL("http://localhost:8000/transaction?wsdl");
        QName qname = new QName("http://service/", "TransactionServiceImplService");
        Service service = Service.create(url, qname);
        TransactionService webService = service.getPort(TransactionService.class);
        return webService;
    }

    private void resetLabels() {
        errorLabel.setText("");
    }
}