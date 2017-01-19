package service.rest;

import database.DatabaseService;
import domain.Account;
import domain.Transaction;
import domain.User;
import operation.OperationType;
import org.json.JSONObject;
import org.mongodb.morphia.Datastore;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by adam on 11.01.17.
 */

@Singleton
@Path("/")
public class Rest {

    @POST
    @Path("/transfer")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response doExternalTransfer(RestObject restObject) throws Exception {
        System.out.println("Rest");
        if(restObject.getAmount() == 0 ||
                restObject.getReceiver_account() == null ||
                restObject.getSender_account() == null ||
                restObject.getTitle() == null) {
            JSONObject json = new JSONObject();
            json.put("error", "JSON format error.");
            return Response.status(Response.Status.BAD_REQUEST).entity(json.toString()).build();
        }
        double amount = convertToDouble(restObject.getAmount());
        Datastore datastore = DatabaseService.getDatastore();
        Account accountSrc = DatabaseService.findAccountByAccountNumber(datastore, restObject.getSender_account());
        Account account = DatabaseService.findAccountByAccountNumber(datastore, restObject.getReceiver_account());
        if(account == null) {
            JSONObject json = new JSONObject();
            json.put("error", "Target account does not exists.");
            return Response.status(Response.Status.NOT_FOUND).entity(json.toString()).build();
        }
        if(accountSrc != null) {
            JSONObject json = new JSONObject();
            json.put("error", "Target account cannot be located in the same bank.");
            return Response.status(Response.Status.FORBIDDEN).entity(json.toString()).build();
        }
        account.increaseBalance(amount);
        datastore.save(account);
        User user = findUserByAccountNumber(restObject.getReceiver_account());
        Transaction transaction = new Transaction(restObject.getTitle(),
                restObject.getSender_account(),
                restObject.getReceiver_account(),
                OperationType.ExternalTransfer,
                amount,
                user);
        datastore.save(transaction);
        return Response.status(201).entity(transaction).build();
    }


    private double convertToDouble(int amount) {
        String amountStr = Integer.toString(amount);
        amountStr = amountStr.substring(0, amountStr.length() - 2) + "." + amountStr.substring(amountStr.length() - 2, amountStr.length());
        return Double.parseDouble(amountStr);
    }

    private User findUserByAccountNumber(String accountNumber) throws Exception {
        Datastore datastore = DatabaseService.getDatastore();
        User user = datastore.createQuery(User.class).field("accounts").contains(accountNumber).get();
        return user;
    }



}
