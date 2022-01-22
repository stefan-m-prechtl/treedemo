package de.esg.treedemo.usermgmt.boundary;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.esg.treedemo.shared.boundary.exceptionhandling.ExceptionHandlingInterceptor;
import de.esg.treedemo.shared.boundary.security.RoleNames;
import de.esg.treedemo.shared.boundary.security.TokenHandler;
import de.esg.treedemo.treemgmt.boundary.Constants;
import de.esg.treedemo.usermgmt.domain.LoginData;

@Stateless(description = "REST-Interface")
@Path(Constants.pathUser)
@Interceptors(ExceptionHandlingInterceptor.class)
public class UserResource
{
	@Context
	protected UriInfo uriInfo;

	@POST
	@Path("/login")
	@PermitAll()
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createResource(@Valid LoginData logindata)
	{

		final String user = logindata.getUser();

		// String passwd =logindata.getPasswd();
		// TODO: Logindaten prüfen und Rolle für Benutzer aus Datenbank laden

		final String role = RoleNames.ADMIN;
		final String token = TokenHandler.createTokenFor(user, role);

		return Response.ok(token).build();
	}

}
