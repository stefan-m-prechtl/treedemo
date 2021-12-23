package de.esg.treedemo.shared.boundary;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import de.esg.treedemo.treemgmt.boundary.FullNodeJsonAdapter;
import de.esg.treedemo.treemgmt.boundary.FullTreeJsonAdapter;

@Provider
public class JsonbContextResolver implements ContextResolver<Jsonb>
{
	@Override
	public Jsonb getContext(final Class<?> type)
	{
		// @formatter:off
		final JsonbConfig config = new JsonbConfig()
				.withAdapters(new FullNodeJsonAdapter())
				.withAdapters(new FullTreeJsonAdapter());
	   // @formatter:on

		final var jsonb = JsonbBuilder.create(config);

		return jsonb;
	}

}
