package de.esg.treedemo.shared.boundary.security;

import static java.util.Arrays.asList;

import java.util.HashSet;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;

@ApplicationScoped
public class JwtAuthenticationMechanism implements HttpAuthenticationMechanism
{
	@Override
	public AuthenticationStatus validateRequest(final HttpServletRequest request, final HttpServletResponse response, final HttpMessageContext httpMessageContext)
			throws AuthenticationException
	{
		if (request.getPathInfo().equals("/user/login"))
		{
			return httpMessageContext.doNothing();
		}

		String authorization = request.getHeader("Authorization");
		if (!Strings.isNullOrEmpty(authorization))
		{
			authorization = authorization.strip();
			if (authorization.startsWith("Bearer", 0) && authorization.contains(" "))
			{
				final String token = authorization.split(" ")[1];
				final CredentialValidationResult resultValidation = this.validateToken(token);

				if (!resultValidation.equals(CredentialValidationResult.INVALID_RESULT))
				{
					return httpMessageContext.notifyContainerAboutLogin(resultValidation);
				}
				response.addHeader("Authorization-Error", "JWT is invalid");
			}
			else
			{
				response.addHeader("Authorization-Error", "Bearer/Token is missing");
			}
		}
		else
		{
			response.addHeader("Authorization-Error", "Authorization is missing");
		}
		return httpMessageContext.responseUnauthorized();
	}

	private CredentialValidationResult validateToken(String token)
	{
		final Optional<DecodedJWT> optionalToken = TokenHandler.decodeToken(token);
		if (optionalToken.isEmpty())
		{
			return CredentialValidationResult.INVALID_RESULT;
		}

		final DecodedJWT jwt = optionalToken.get();
		// Aktuelle Werte für Benutzer und Rolle aus Token extrahieren
		final String username = TokenHandler.getUsernameFromToken(jwt);
		final String userrole = TokenHandler.getUserroleFromToken(jwt);

		final CredentialValidationResult result = new CredentialValidationResult(username, new HashSet<>(asList(userrole)));
		return result;
	}
}
