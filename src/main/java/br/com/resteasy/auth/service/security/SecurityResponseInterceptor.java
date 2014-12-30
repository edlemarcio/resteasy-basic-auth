package br.com.resteasy.auth.service.security;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by enrique1 on 12/29/14.
 *
 * In order to make a specific header to be accepted in the REST service, we have to define the acceptance of HTTP Header very specifically in the response filter interceptor.
 * See more at: http://www.developerscrappad.com/1814/java/java-ee/rest-jax-rs/java-ee-7-jax-rs-2-0-simple-rest-api-authentication-authorization-with-custom-http-header/#sthash.zJbby05W.dpuf
 *
 */
@Provider
@PreMatching
public class SecurityResponseInterceptor implements ContainerResponseFilter {

    @Override public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext responseCtx)
        throws IOException {

        responseCtx.getHeaders().add( "Access-Control-Allow-Origin", "*" );    // You may further limit certain client IPs with Access-Control-Allow-Origin instead of '*'
        responseCtx.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
        responseCtx.getHeaders().add( "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT" );
        responseCtx.getHeaders().add( "Access-Control-Allow-Headers", SecurityHttpHeaders.AUTHORIZATION_PROPERTY + ", " + SecurityHttpHeaders.AUTHENTICATION_PROPERTY );

    }
}
