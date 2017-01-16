package domain;

import helper.ObjectIdJaxAdapter;
import operation.OperationType;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created by adam on 06.01.17.
 */

@Entity
public class Transaction {

    @Id
    @XmlJavaTypeAdapter(ObjectIdJaxAdapter.class)
    private ObjectId id;

    @NotNull
    private String title;

    @NotNull
    private String sourceAccountNumber;

    @NotNull
    private String targetAccountNumber;

    @NotNull
    private double balance;

    @NotNull
    private OperationType operationType;

    @NotNull
    private double amount;

    public Transaction() {}

    public Transaction(String title,
                       String sourceAccountNumber,
                       String targetAccountNumber,
                       OperationType operationType,
                       double amount) {
       this(title, sourceAccountNumber, targetAccountNumber, 0, operationType, amount);
    }

    public Transaction(String title,
                       String sourceAccountNumber,
                       String targetAccountNumber,
                       double balance,
                       OperationType operationType,
                       double amount) {
        this.title = title;
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
        this.balance = balance;
        this.operationType = operationType;
        this.amount = amount;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public void setTargetAccountNumber(String targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
