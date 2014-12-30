package br.com.resteasy.auth.service;

import br.com.resteasy.auth.dao.UserDao;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Created by enrique1 on 12/27/14.
 */
@Path("/users/")
@Produces("application/json")
public class UserService {

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") int id, @Context Request request) {
        //Create cache control header
        CacheControl cc = new CacheControl();
        //Set max age to one day
        cc.setMaxAge(86400);

        Response.ResponseBuilder rb = null;

        //Calculate the ETag on last modified date of user resource
        EntityTag etag = new EntityTag(UserDao.getLastModifiedById(id).hashCode()+"");

        //Verify if it matched with etag available in http request
        rb = request.evaluatePreconditions(etag);

        //If ETag matches the rb will be non-null;
        //Use the rb to return the response without any further processing
        if (rb != null) {
	    return rb.cacheControl(cc).tag(etag).build();
        }

        //If rb is null then either it is first time request; or resource is modified
        //Get the updated representation and return with Etag attached to it
        rb = Response.ok(UserDao.get(id).get()).cacheControl(cc).tag(etag);
        return rb.build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id) {
	//Update the User resource
	UserDao.update(UserDao.get(id).get());
	return Response.status(200).build();
    }
}
