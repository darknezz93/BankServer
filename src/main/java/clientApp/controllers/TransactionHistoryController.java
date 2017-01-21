package clientApp.controllers;

import clientApp.helper.ClientAuth;
import clientApp.layouts.TransactionTable;
import domain.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import service.TransactionService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by adam on 18.01.17.
 */

/**
 * Kotnroler odpowiedzialny za okno histori operacji
 */
public class TransactionHistoryController {

    @FXML
    private TableView<TransactionTable> tableView;

    @FXML
    private TableColumn<TransactionTable, String> titleColumn;

    @FXML
    private TableColumn<TransactionTable, String> senderAccountColumn;

    @FXML
    private TableColumn<TransactionTable, String> receiverAccountColumn;

    @FXML
    private TableColumn<TransactionTable, Double> amountColumn;

    @FXML
    private TableColumn<TransactionTable, String> operationTypeColumn;

    @FXML
    private TableColumn<TransactionTable, Double> balanceColumn;


    private ObservableList<TransactionTable> transactionData = FXCollections.observableArrayList();


    /**
     * Metoda inicjująca wywoływana po utworzeniu okna i wypełniająca poszczególne pola tabeli
     * @throws MalformedURLException
     */
    @FXML
    public void initialize() throws MalformedURLException {
        TransactionService transactionService = getTransactionService();
        List<Transaction> transactions =  transactionService.getTransactions(ClientAuth.getEncodedAuth());
        for(Transaction trn : transactions) {
            transactionData.add(new TransactionTable(trn.getTitle(),
                    trn.getSourceAccountNumber(),
                    trn.getTargetAccountNumber(),
                    trn.getBalance(),
                    trn.getOperationType().toString(),
                    trn.getAmount()));
        }
        tableView.setItems(transactionData);
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        senderAccountColumn.setCellValueFactory(cellData -> cellData.getValue().sourceAccountNumberProperty());
        receiverAccountColumn.setCellValueFactory(cellData -> cellData.getValue().targetAccountNumberProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        operationTypeColumn.setCellValueFactory(cellData -> cellData.getValue().operationTypeProperty());
        balanceColumn.setCellValueFactory(cellData -> cellData.getValue().balanceProperty().asObject());
    }

    /**
     * Zwraca transactionService
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
}
