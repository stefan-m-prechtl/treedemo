package de.esg.treedemo.usermgmt.boundary;

import java.util.HashMap;
import java.util.Map;

import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

public class DemoIdentityStore implements IdentityStore
{
	private static final Map<String, String> unsecureStore = new HashMap<>();

	public DemoIdentityStore()
	{
		unsecureStore.put("admin", "geheim");
		unsecureStore.put("reader", "123");
	}

	@Override
	public CredentialValidationResult validate(final Credential credential)
	{
		final UsernamePasswordCredential c = (UsernamePasswordCredential) credential;
		final var user = c.getCaller();
		final var passwd = c.getPasswordAsString();

		if (unsecureStore.containsKey(user))
		{
			final var storedpasswd = unsecureStore.get(user);
			if (storedpasswd.equals(passwd))
			{
				return new CredentialValidationResult(user);
			}
		}
		return CredentialValidationResult.INVALID_RESULT;
	}

}