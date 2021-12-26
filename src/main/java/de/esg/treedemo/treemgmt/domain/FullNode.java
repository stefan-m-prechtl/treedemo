package de.esg.treedemo.treemgmt.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FullNode
{
	private final Node node;
	private final List<FullNode> children;
	private final FullTree tree;
	private long level;

	public FullNode(final FullTree tree, final Node node)
	{
		this.tree = tree;
		this.node = node;
		this.children = new ArrayList<FullNode>();
		this.level = 0;
	}

	public void addNode(final FullNode fullNode)
	{
		fullNode.setLevel(this.getLevel() + 1);
		this.children.add(fullNode);
		this.tree.addNode(fullNode);
	}

	public Node getNode()
	{
		return this.node;
	}

	public String getName()
	{
		return this.node.getName();
	}

	public long getLevel()
	{
		return this.level;
	}

	public void setLevel(final long level)
	{
		this.level = level;
	}

	public FullTree getFullTree()
	{
		return this.tree;
	}

	public List<FullNode> getChildren()
	{
		return Collections.unmodifiableList(this.children);
	}
}
