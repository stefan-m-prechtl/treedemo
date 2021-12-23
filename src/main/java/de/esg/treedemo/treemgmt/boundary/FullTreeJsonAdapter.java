package de.esg.treedemo.treemgmt.boundary;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;

import de.esg.treedemo.treemgmt.domain.FullTree;
import de.esg.treedemo.treemgmt.domain.Tree;

public class FullTreeJsonAdapter implements JsonbAdapter<FullTree, JsonObject>
{
	public final static String field_id = "id";
	public final static String field_name = "name";
	public final static String field_root = "rootnode";

	@Override
	public JsonObject adaptToJson(final FullTree fullTree) throws Exception
	{
		//@formatter:off
		final var result = Json.createObjectBuilder()
				.add(field_id, String.valueOf(fullTree.getTree().getId()))
				.add(field_name, fullTree.getTree().getName())
				.add(field_root, new FullNodeJsonAdapter().adaptToJson(fullTree.getRootNode()))
				.build();
		//@formatter:on
		return result;
	}

	@Override
	public FullTree adaptFromJson(final JsonObject jsonObj) throws Exception
	{
		FullTree result;
		final var id = jsonObj.getString(field_id);
		final var name = jsonObj.getString(field_name);

		final var tree = new Tree(name);
		tree.setId(Long.parseLong(id));
		result = new FullTree(tree);

		return result;
	}
}
