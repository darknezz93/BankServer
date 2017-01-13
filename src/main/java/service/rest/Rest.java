package service.rest;

import database.DatabaseService;
import domain.Account;
import domain.Transaction;
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
        double amount = convertToDouble(restObject.getAmount());
        Transaction transaction = new Transaction(restObject.getTitle(),
                restObject.getSender_account(),
                restObject.getReceiver_account(),
                OperationType.ExternalTransfer,
                amount);

        if(amount < 0) {
            JSONObject json = new JSONObject();
            json.put("error", "Amount value must be > 0");
            return Response.status(Response.Status.BAD_REQUEST).entity(json.toString()).build();
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
