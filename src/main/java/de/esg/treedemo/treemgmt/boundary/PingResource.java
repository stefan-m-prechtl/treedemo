package de.esg.treedemo.treemgmt.boundary;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.esg.treedemo.shared.boundary.security.RoleNames;

@Stateless(description = "REST-Interface für Ping")
@Path(Constants.pathPing)
public class PingResource
{
	@Inject
	@ConfigProperty(name = "PINGMSG")
	String pingMsg;
	
	@Inject
	private SecurityContext securityContext;

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping()
	{
		final var now = LocalDateTime.now();
		final var dateFormated = now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		final var timeFormated = now.format(DateTimeFormatter.ofPattern("HH:mm:ss SSS"));

		// @formatter:off
		final var result = Json.createObjectBuilder()
				.add("message", this.pingMsg)
				.add("date", dateFormated)
				.add("time", timeFormated)
				.build();
		// @formatter:on

		return Response.ok(result).build();
	}
	
	@GET
	@Path("/admin")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(RoleNames.ADMIN)
	public Response pingAdmin()
	{
		final var now = LocalDateTime.now();
		final var dateFormated = now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		final var timeFormated = now.format(DateTimeFormatter.ofPattern("HH:mm:ss SSS"));

		// @formatter:off
		final var result = Json.createObjectBuilder()
				.add("message", "Ping-Nachricht für Admin")
				.add("date", dateFormated)
				.add("time", timeFormated)
				.build();
		// @formatter:on

		return Response.ok(result).build();
	}
	
	@GET
	@Path("/reader")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(RoleNames.READER)
	public Response pingReader()
	{
		final var now = LocalDateTime.now();
		final var dateFormated = now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		final var timeFormated = now.format(DateTimeFormatter.ofPattern("HH:mm:ss SSS"));

		// @formatter:off
		final var result = Json.createObjectBuilder()
				.add("message", "Ping-Nachricht für Reader")
				.add("date", dateFormated)
				.add("time", timeFormated)
				.build();
		// @formatter:on
		
		final Principal principial = this.securityContext.getCallerPrincipal();

		return Response.ok(result).build();
	}
}
