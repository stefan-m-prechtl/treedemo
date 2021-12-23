package de.esg.treedemo.shared.boundary.exceptionhandling;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GeneralExecutionMapper implements ExceptionMapper<Exception>
{
	@Inject
	LoggerExposer logger;

	@Override
	public Response toResponse(Exception ex)
	{
		if (ex instanceof WebApplicationException)
		{
			return ((WebApplicationException) ex).getResponse();
		}

		this.logger.fatalErrorConsumer().accept(ex);
		return Response.status(Response.Status.BAD_REQUEST).header("reason", "Fehler: " + ex.getMessage()).build();
	}

}
