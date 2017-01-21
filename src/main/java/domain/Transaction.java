package domain;

import helper.ObjectIdJaxAdapter;
import operation.OperationType;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created by adam on 06.01.17.
 */

/**
 * Klasa domenowa dla transakcji
 */
@Entity
public class Transaction {

    @Id
    @XmlJavaTypeAdapter(ObjectIdJaxAdapter.class)
    private ObjectId identifier;

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

    @Reference
    private User user;

    public Transaction() {}

    public Transaction(String title,
                       String sourceAccountNumber,
                       String targetAccountNumber,
                       OperationType operationType,
                       double amount,
                       User user) {
       this(title, sourceAccountNumber, targetAccountNumber, 0, operationType, amount, user);
    }

    public Transaction(String title,
                       String sourceAccountNumber,
                       String targetAccountNumber,
                       double balance,
                       OperationType operationType,
                       double amount,
                       User user) {
        this.title = title;
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
        this.balance = balance;
        this.operationType = operationType;
        this.amount = amount;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
