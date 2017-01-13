package service.rest;

/**
 * Created by adam on 13.01.17.
 */
public class RestObject {

    private int amount;

    private String sender_account;

    private String receiver_account;

    private String title;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSender_account() {
        return sender_account;
    }

    public void setSender_account(String sender_account) {
        this.sender_account = sender_account;
    }

    public String getReceiver_account() {
        return receiver_account;
    }

    public void setReceiver_account(String receiver_account) {
        this.receiver_account = receiver_account;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
