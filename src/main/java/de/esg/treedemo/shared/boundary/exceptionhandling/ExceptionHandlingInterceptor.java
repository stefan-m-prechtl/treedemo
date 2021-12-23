package de.esg.treedemo.shared.boundary.exceptionhandling;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ExceptionHandlingInterceptor
{
	@Inject
	LoggerExposer logger;

	@AroundInvoke
	public Object handleException(final InvocationContext context)
	{
		final Object proceedResponse;
		try
		{
			proceedResponse = context.proceed();
		}
		catch (final Exception ex)
		{
			this.logger.fatalErrorConsumer().accept(ex);

			if (ex instanceof WebApplicationException)
			{
				return ((WebApplicationException) ex).getResponse();
			}
			return Response.status(500).header("reason", "Fehler:" + ex.getMessage()).build();

		}
		return proceedResponse;
	}

}
