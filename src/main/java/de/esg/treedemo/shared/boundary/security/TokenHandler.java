package de.esg.treedemo.shared.boundary.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public final class TokenHandler
{
	private final static String KEY_USERNAME = "username";
	private final static String KEY_USERROLE = "userrole";
	// TODO : sichere Signatur
	private final static String SECRET = "secret";

	private TokenHandler()
	{
		// nothing todo!
	}

	public static String createTokenFor(String username, String userrole)
	{
		final Algorithm algorithm = Algorithm.HMAC256(SECRET);
		final Map<String, String> payloadClaims = new HashMap<>();
		payloadClaims.put(KEY_USERNAME, username);
		payloadClaims.put(KEY_USERROLE, userrole);

		// //@formatter:off
		final String token = JWT.create()
				.withIssuer("ESG")
				.withSubject("TreeDemo")
				.withPayload(payloadClaims)
				.sign(algorithm);
		//@formatter:on

		return token;
	}

	public static Optional<DecodedJWT> decodeToken(String token)
	{
		Optional<DecodedJWT> result = Optional.empty();
		try
		{
			final Algorithm algorithm = Algorithm.HMAC256(SECRET);
			final JWTVerifier verifier = JWT.require(algorithm).withIssuer("ESG").build();
			final DecodedJWT jwt = verifier.verify(token);

			// TODO Token prüfen
			// jwt.getAudience();
			// jwt.getExpiresAt()
			// ...

			result = Optional.of(jwt);
		}
		catch (final Exception e)
		{
			System.out.println(e.toString());
		}
		return result;
	}

	public static String getUsernameFromToken(DecodedJWT jwt)
	{
		return jwt.getClaim(KEY_USERNAME).asString();
	}

	public static String getUserroleFromToken(DecodedJWT jwt)
	{
		return jwt.getClaim(KEY_USERROLE).asString();
	}

}
