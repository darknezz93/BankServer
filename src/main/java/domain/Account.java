package domain;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.validation.constraints.NotNull;

/**
 * Created by adam on 06.01.17.
 */

@Entity
public class Account {


    @Id
    @NotNull
    private String accountNumber;

    @NotNull
    private double balance;

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
}
