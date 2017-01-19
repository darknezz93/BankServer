package clientApp.layouts;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by adam on 19.01.17.
 */
public class TransactionTable {

    private StringProperty title;

    private StringProperty sourceAccountNumber;

    private StringProperty targetAccountNumber;

    private DoubleProperty balance;

    private StringProperty operationType;

    private DoubleProperty amount;

    public TransactionTable(String title, String sourceAccountNumber, String targetAccountNumber, double balance, String operationType, double amount) {
        this.title = new SimpleStringProperty(title);
        this.sourceAccountNumber = new SimpleStringProperty(sourceAccountNumber);
        this.targetAccountNumber = new SimpleStringProperty(targetAccountNumber);
        this.balance = new SimpleDoubleProperty(balance);
        this.operationType = new SimpleStringProperty(operationType);
        this.amount = new SimpleDoubleProperty(amount);
    }


    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber.get();
    }

    public StringProperty sourceAccountNumberProperty() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber.set(sourceAccountNumber);
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber.get();
    }

    public StringProperty targetAccountNumberProperty() {
        return targetAccountNumber;
    }

    public void setTargetAccountNumber(String targetAccountNumber) {
        this.targetAccountNumber.set(targetAccountNumber);
    }

    public double getBalance() {
        return balance.get();
    }

    public DoubleProperty balanceProperty() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance.set(balance);
    }

    public String getOperationType() {
        return operationType.get();
    }

    public StringProperty operationTypeProperty() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType.set(operationType);
    }

    public double getAmount() {
        return amount.get();
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }
}
