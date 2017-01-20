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
public class PaymentController {

    @FXML
    private ComboBox<Account> accountComboBox;

    @FXML
    private AmountTextField amountTextField;

    @FXML
    private Button paymentButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Label balanceLabel;

    private List<Account> accounts = new ArrayList<>();

    @FXML
    public void initialize() throws MalformedURLException {
        AccountService accountService = getAccountService();
        accounts = accountService.getAccounts(ClientAuth.getEncodedAuth());
        accountComboBox.setItems(FXCollections.observableArrayList(accounts));
        accountComboBox.setConverter(new StringConverter<Account>() {
            @Override
            public String toString(Account object) {
                return object.getAccountNumber();
            }

            @Override
            public Account fromString(String string) {
                return null;
            }
        });

        accountComboBox.valueProperty().addListener(new ChangeListener<Account>() {
            @Override
            public void changed(ObservableValue<? extends Account> observable, Account oldValue, Account newValue) {
                if(newValue != null) {
                    balanceLabel.setText(String.valueOf(newValue.getBalance()));
                }
            }
        });
    }


    @FXML
    public void listenPaymentButton() throws Exception {
        resetLabels();
        if(accountComboBox.getSelectionModel().getSelectedItem() == null) {
            errorLabel.setText("Wybierz numer rachunku");
            return;
        }
        String accountNumber = accountComboBox.getSelectionModel().getSelectedItem().getAccountNumber();
        if (amountTextField.getText().equals("")) {
            errorLabel.setText("Wprowadź kwotę");
            return;
        }
        double amount = Double.valueOf(amountTextField.getText());
        TransactionService trnService = getTransactionService();
        Account account = trnService.doPayment(accountNumber, amount, ClientAuth.getEncodedAuth());
        //updateAccounts(account);
        refreshComboBoxes(amount);
    }

    private AccountService getAccountService() throws MalformedURLException {
        URL url = new URL("http://localhost:8000/account?wsdl");
        QName qname = new QName("http://service/", "AccountServiceImplService");
        Service service = Service.create(url, qname);
        AccountService webService = service.getPort(AccountService.class);
        return webService;
    }

    private void updateAccounts(Account account) {
        for(Account acc : accounts) {
            if(account.getAccountNumber().equals(acc.getAccountNumber())) {
                acc = account;
                balanceLabel.setText(String.valueOf(acc.getBalance()));
                accountComboBox.setItems(FXCollections.observableArrayList(accounts));
                accountComboBox.setValue(acc);
                return;
            }
        }
    }

    private void refreshComboBoxes(double amount) throws MalformedURLException {
        AccountService accountService = getAccountService();
        accounts = accountService.getAccounts(ClientAuth.getEncodedAuth());
        Account senderAcc = accountComboBox.getSelectionModel().getSelectedItem();
        accountComboBox.setItems(FXCollections.observableArrayList(accounts));
        accountComboBox.getSelectionModel().select(senderAcc);
        balanceLabel.setText(String.valueOf(senderAcc.getBalance() + amount));
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
