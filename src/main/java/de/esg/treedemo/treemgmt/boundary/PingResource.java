package de.esg.treedemo.treemgmt.boundary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Stateless(description = "REST-Interface für Ping")
@Path(Constants.pathPing)
public class PingResource
{
	@Inject
	@ConfigProperty(name = "PINGMSG")
	String pingMsg;

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
}
