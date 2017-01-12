package service.rest;

import database.DatabaseService;
import domain.Account;
import domain.Transaction;
import operation.OperationType;
import org.json.JSONObject;
import org.mongodb.morphia.Datastore;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by adam on 11.01.17.
 */

@Singleton
@Path("/rest")
public class Rest {

    @POST
    @Path("/transfer")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response doExternalTransfer(@QueryParam("amount") int amountInt,
                                       @QueryParam("sender_account") String senderAccount,
                                       @QueryParam("receiver_account") String receiverAccount,
                                       @QueryParam("title") String title) throws Exception {

        double amount = convertToDouble(amountInt);
        Transaction transaction = new Transaction(title,
                senderAccount,
                receiverAccount,
                OperationType.ExternalTransfer,
                amount);

        if(transaction == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Datastore datastore = DatabaseService.getDatastore();
        Account account = DatabaseService.findAccountByAccountNumber(datastore, transaction.getTargetAccountNumber());
        if(account == null) {
            JSONObject json = new JSONObject();
            json.put("error", "Target account does not exists");
            return Response.status(Response.Status.NOT_FOUND).entity(json.toString()).build();
        }
        account.increaseBalance(amount);
        datastore.save(account);
        datastore.save(transaction);
        return Response.status(201).entity(transaction).build();
    }


    private double convertToDouble(int amount) {
        String amountStr = Integer.toString(amount);
        amountStr = amountStr.substring(0, amountStr.length() - 2) + "." + amountStr.substring(amountStr.length() - 2, amountStr.length());
        return Double.parseDouble(amountStr);
    }



}
