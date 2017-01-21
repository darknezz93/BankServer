package service.rest;

import org.glassfish.jersey.internal.util.Base64;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Created by adam on 11.01.17.
 */

/**
 * Klasa odpowiedzialna za autoryzację dostępu do usług restowych
 */
@Provider
public class RestAuthenticationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
            .entity("You cannot access this resource").build();

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "admin";

    /**
     * Metoda filtrująca dostęp do usług restowych
     * @param requestContext
     */
    @Override
    public void filter(ContainerRequestContext requestContext)
    {
        System.out.println("Filter");
        Method method = resourceInfo.getResourceMethod();

        final MultivaluedMap<String, String> headers = requestContext.getHeaders();

        final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

        if(authorization == null || authorization.isEmpty()) {
            requestContext.abortWith(ACCESS_DENIED);
            return;
        }

        final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

        String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));

        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        System.out.println(username);
        System.out.println(password);

        if(!password.equals(PASSWORD) || !username.equals(USERNAME)) {
            requestContext.abortWith(ACCESS_DENIED);
            return;
        }
    }


}
