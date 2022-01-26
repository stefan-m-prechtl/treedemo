package de.esg.treedemo.treemgmt.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullTree
{
	private final Tree tree;
	private FullNode rootFullNode;
	private final List<FullNode> allChildNodes;
	private final Map<String, List<FullNode>> levelLists;

	public FullTree(final String treeName, final Node rootNode)
	{
		this.tree = new Tree(treeName);
		this.rootFullNode = new FullNode(this, rootNode);

		ArrayList<Node> path = new ArrayList<Node>();
		path.add(rootFullNode.getNode());
		this.rootFullNode.setPath(path);
		this.allChildNodes = new ArrayList<FullNode>();
		this.levelLists = new HashMap<String, List<FullNode>>();

		this.levelLists.put("L0", Arrays.asList(this.rootFullNode));
	}

	public FullTree(final Tree tree)
	{
		this.tree = tree;
		this.allChildNodes = new ArrayList<FullNode>();
		this.levelLists = new HashMap<String, List<FullNode>>();
	}

	public FullNode getRootNode()
	{
		return this.rootFullNode;
	}

	public void setRootNode(final FullNode rootFullNode)
	{
		this.rootFullNode = rootFullNode;
		this.levelLists.put("L0", Arrays.asList(this.rootFullNode));
	}

	public String getName()
	{
		return this.tree.getName();
	}

	public Tree getTree()
	{
		return this.tree;
	}

	public long getId()
	{
		return this.tree.getId();
	}

	public void addNode(final FullNode node)
	{
		this.allChildNodes.add(node);

		final String strLevel = "L" + String.valueOf(node.getLevel());

		if (this.levelLists.containsKey(strLevel))
		{
			final List<FullNode> list = this.levelLists.get(strLevel);
			list.add(node);
		}
		else
		{
			final List<FullNode> list = new ArrayList<FullNode>();
			list.add(node);
			this.levelLists.put(strLevel, list);
		}
	}

	public List<FullNode> getLevelList(final long level)
	{
		List<FullNode> result = new ArrayList<>();
		final String strLevel = "L" + String.valueOf(level);
		if (this.levelLists.containsKey(strLevel))
		{
			result = this.levelLists.get(strLevel);
		}
		return result;
	}

	public List<FullNode> getChildNodes()
	{
		return Collections.unmodifiableList(this.allChildNodes);
	}

	public void printTree()
	{
		this.levelLists.keySet().forEach(level -> {
			final List<FullNode> nodes = this.levelLists.get(level);
			nodes.forEach(node -> {
				System.out.print(node.getName());
				System.out.print(",");
			});
			System.out.println();
		});
	}
}
