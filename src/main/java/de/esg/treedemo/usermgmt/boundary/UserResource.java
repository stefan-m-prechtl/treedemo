package de.esg.treedemo.usermgmt.boundary;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
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

	@Inject
	private DemoIdentityStore identityStore;

	@POST
	@Path("/login")
	@PermitAll()
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createResource(@Valid LoginData logindata)
	{
		final String user = logindata.getUser();
		final String passwd = logindata.getPasswd();

		// TODO HACK mit Identitystore und fixer Rolle ersetzen
		final var resultValidation = this.identityStore.validate(new UsernamePasswordCredential(user, passwd));
		if (resultValidation.getStatus().equals(CredentialValidationResult.Status.INVALID))
		{
			return Response.status(403).header("reason", "User/Passwort ungültig").build();
		}

		final String role = RoleNames.ADMIN;
		final String token = TokenHandler.createTokenFor(user, role);

		return Response.ok(token).build();
	}

}
