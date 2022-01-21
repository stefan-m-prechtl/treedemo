package de.esg.treedemo.shared.boundary;

import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import de.esg.treedemo.shared.boundary.security.RoleNames;

@ApplicationScoped
@ApplicationPath("demo")
@DeclareRoles({ RoleNames.ADMIN, RoleNames.EXTERN, RoleNames.INTERN, RoleNames.READER })
public class ApplicationDemo extends Application
{
	// nichts zu tun!
}
