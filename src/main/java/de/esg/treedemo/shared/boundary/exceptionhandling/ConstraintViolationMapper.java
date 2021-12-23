package de.esg.treedemo.shared.boundary.exceptionhandling;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException>
{

	@Override
	public Response toResponse(ConstraintViolationException exception)
	{
		return Response.status(Response.Status.BAD_REQUEST).header("reason", this.prepareMessage(exception)).build();
	}

	private String prepareMessage(ConstraintViolationException exception)
	{
		StringBuilder message = new StringBuilder();
		for (ConstraintViolation<?> cv : exception.getConstraintViolations())
		{
			message.append(cv.getPropertyPath() + " " + cv.getMessage() + "\n");
		}
		return message.toString();
	}
}