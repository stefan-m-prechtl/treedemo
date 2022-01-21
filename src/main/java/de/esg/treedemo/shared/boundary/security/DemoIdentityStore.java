package de.esg.treedemo.shared.boundary.security;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

@ApplicationScoped
public class DemoIdentityStore implements IdentityStore
{
// private static final Map<String, String> unsecureStore = new HashMap<>();

	public DemoIdentityStore()
	{
		// Don't do this at home, highly unsecure!
		// unsecureStore.put("prs", "STEFAN");
		// unsecureStore.put("admin", "ADMIN");
	}

	@Override
	public CredentialValidationResult validate(final Credential credential)
	{
		return new CredentialValidationResult("admin");
	}
}
