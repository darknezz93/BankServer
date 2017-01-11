package server;


import helper.CustomHeaders;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.jaxws.JaxwsHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import service.AccountService;
import service.UserService;
import service.rest.Rest;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

/**
 * Created by adam on 06.01.17.
 */
public class GrizzlyServer {

    private static final int SOAP_PORT = 8000;
    private static final int REST_PORT = 8080;

    private static HttpServer httpServer;

    public static void main(String[] args) throws IOException {

        openSoapServices();
    }

    private static void openSoapServices() throws IOException {

        /** SOAP */
        URI soapUri = UriBuilder.fromUri("http://localhost/").port(SOAP_PORT).build();
        ResourceConfig config = new ResourceConfig(Rest.class).register(DeclarativeLinkingFeature.class).register(CustomHeaders.class);
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(soapUri, config);

        HttpHandler userHandler = new JaxwsHandler(new UserService());
        HttpHandler accountHandler = new JaxwsHandler(new AccountService());

        httpServer.getServerConfiguration().addHttpHandler(userHandler, "/user");
        httpServer.getServerConfiguration().addHttpHandler(accountHandler, "/account");

        /** REST */
        NetworkListener networkListener = new NetworkListener("rest-listener", "0.0.0.0", REST_PORT);
        httpServer.addListener(networkListener);

        httpServer.start();

    }



}
