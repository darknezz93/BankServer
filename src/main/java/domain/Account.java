package domain;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.ValidationException;

import javax.validation.constraints.NotNull;

/**
 * Created by adam on 06.01.17.
 */

/**
 * Klasa domenowa dla Rachunku
 */
@Entity
public class Account {


    @Id
    @NotNull
    private String accountNumber;

    @NotNull
    private double balance;

    public Account() {}

    public Account(String accountNumber) {
        this.balance = 0;
        this.accountNumber = accountNumber;
    }


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Zwiększa saldo
     * @param amount
     */
    public void increaseBalance(double amount) {
        if(amount > 0) {
            balance += amount;
        } else {
            throw new ValidationException("Invalid amount.");
        }
    }

    /**
     * Zmniejsza saldo
     * @param amount
     */
    public void decreaseBalance(double amount) {
        if(amount > 0) {
            balance -= amount;
        } else {
            throw new ValidationException("Invalid amount.");
        }
    }
}
