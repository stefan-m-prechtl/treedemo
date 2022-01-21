package de.esg.treedemo.shared.boundary.security;

import static java.util.Arrays.asList;
import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;

import java.util.Enumeration;
import java.util.HashSet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@ApplicationScoped
public class DemoAuthenticationMechanism implements HttpAuthenticationMechanism
{
	private final static String USER = "admin";
	private final static String PASSWD = "geheim";
	
	@Inject
	private IdentityStoreHandler identityStoreHandler;

	@Override
	public AuthenticationStatus validateRequest(final HttpServletRequest request, final HttpServletResponse response,
			final HttpMessageContext httpMessageContext) throws AuthenticationException
	{
		final String name = USER;
		final String password = PASSWD;

		final Enumeration<String> headers = request.getHeaderNames();
		final Cookie[] cookies = request.getCookies();

		final AuthenticationStatus result = httpMessageContext.notifyContainerAboutLogin(this.validate(new UsernamePasswordCredential(name, password)));

		return result;

	}

	CredentialValidationResult validate(final UsernamePasswordCredential usernamePasswordCredential)
	{
		CredentialValidationResult resultFromStore = this.identityStoreHandler.validate(usernamePasswordCredential);
			
		if (usernamePasswordCredential.compareTo(USER, PASSWD))
		{
			final CredentialValidationResult result = new CredentialValidationResult(USER, new HashSet<>(asList(RoleNames.ADMIN)));
			return result;
		}

		return INVALID_RESULT;
	}

}
