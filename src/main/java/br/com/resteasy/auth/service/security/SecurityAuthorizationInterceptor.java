package br.com.resteasy.auth.service.security;

import br.com.resteasy.auth.dao.UserDao;
import br.com.resteasy.auth.model.User;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.util.Base64;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.*;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by enrique1 on 12/29/14.
 */
@Provider
@PreMatching
public class SecurityAuthorizationInterceptor implements ContainerRequestFilter {

    private static final ServerResponse ACCESS_DENIED = new ServerResponse("Access denied for this resource", 401, new Headers<>());
    private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse("Nobody can access this resource", 403, new Headers<>());
    private static final ServerResponse SERVER_ERROR = new ServerResponse("INTERNAL SERVER ERROR", 500, new Headers<>());

    @Override public void filter(ContainerRequestContext requestContext)
	throws IOException {

        // IMPORTANT!!! First, Acknowledge any pre-flight test from browsers for this case before validating the headers (CORS stuff)
        if ( requestContext.getRequest().getMethod().equals( "OPTIONS" ) ) {
            requestContext.abortWith( Response.status(Response.Status.OK).build() );

	    return;
        }

        ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker)requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();
        //Access allowed for all
        if( ! method.isAnnotationPresent(PermitAll.class)) {
	    //Access denied for all
	    if(method.isAnnotationPresent(DenyAll.class)) {
	        requestContext.abortWith(ACCESS_FORBIDDEN);
	        return;
	    }

	    //Get request headers
	    final MultivaluedMap<String, String> headers = requestContext.getHeaders();

	    //Fetch authorization header
	    final List<String> authorization = headers.get(SecurityHttpHeaders.AUTHORIZATION_PROPERTY);

	    //If no authorization information present; block access
	    if(authorization == null || authorization.isEmpty()) {
	        requestContext.abortWith(ACCESS_DENIED);
	        return;
	    }

	    //Get encoded username and password
	    final String encodedToken = authorization.get(0).replaceFirst(SecurityHttpHeaders.AUTHORIZATION_SCHEME + " ", "");

	    //Decode username and password
	    String decodedToken = null;
	    try {
	        decodedToken = new String(Base64.decode(encodedToken));
	    } catch (IOException exc) {
	        requestContext.abortWith(SERVER_ERROR);
	        return;
	    }

	    //Split username and password tokens
	    final StringTokenizer tokenizer = new StringTokenizer(decodedToken, ":");
	    final int userId = Integer.parseInt(tokenizer.nextToken());
	    final String campanhaId = tokenizer.nextToken();
            final String perfil = tokenizer.nextToken();

	    //Verifying Username and password
	    System.out.println(userId);
	    System.out.println(campanhaId);
            System.out.println(perfil);

            //Verify user access
	    if(method.isAnnotationPresent(RolesAllowed.class)) {
	        RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
	        Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

	        //Is user valid?
	        if( ! isUserAllowed(userId, campanhaId, perfil, rolesSet)) {
		    requestContext.abortWith(ACCESS_DENIED);
		    return;
	        }
	    }
        }
    }

    private boolean isUserAllowed(final int userId, String campanhaId, String perfil, final Set<String> rolesSet) {
	boolean isAllowed = false;

        Optional<User> userOpt = UserDao.get(userId);

	//Step 2. Verify user role
	if(userOpt.isPresent() && rolesSet.contains(userOpt.get().getPerfil())) {
	    isAllowed = true;
	}
	return isAllowed;
    }

}
