package de.esg.treedemo.treemgmt.boundary;

import java.util.Iterator;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;

import de.esg.treedemo.treemgmt.domain.FullNode;
import de.esg.treedemo.treemgmt.domain.Node;

public class FullNodeJsonAdapter implements JsonbAdapter<FullNode, JsonObject>
{
	public final static String field_id = "id";
	public final static String field_name = "name";
	public final static String field_level = "level";
	public final static String field_children = "children";

	@Override
	public JsonObject adaptToJson(final FullNode fullNode) throws Exception
	{
		//@formatter:off
		final var result = Json.createObjectBuilder()
				.add(field_id, fullNode.getNode().getId())
				.add(field_name, fullNode.getName())
				.add(field_level, fullNode.getLevel())
				.add(field_children, this.adaptChildren(fullNode.getChildren()))
				.build();
		//@formatter:on
		return result;
	}

	private JsonArrayBuilder adaptChildren(final List<FullNode> children) throws Exception
	{
		final JsonArrayBuilder result = Json.createArrayBuilder();
		final FullNodeJsonAdapter adapter = new FullNodeJsonAdapter();
		for (final Iterator<FullNode> iterator = children.iterator(); iterator.hasNext();)
		{
			result.add(adapter.adaptToJson(iterator.next()));
		}

		return result;
	}

	@Override
	// TODO vollständige Implementierung: Kindknoten fehlen noch
	public FullNode adaptFromJson(final JsonObject jsonObj) throws Exception
	{
		FullNode result;
		final var id = jsonObj.getInt(field_id);
		final var name = jsonObj.getString(field_name);
		final var level = jsonObj.getInt(field_level);

		final var node = new Node(id, name);
		result = new FullNode(null, node);
		result.setLevel(level);

		return result;
	}
}
