package de.esg.treedemo.treemgmt.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.MoreObjects;

public class FullNode
{
	private final FullTree tree;
	private final Node node;
	private final List<FullNode> children;
	private FullNode parent;
	private ArrayList<Node> path;

	private long level;

	public FullNode(final FullTree tree, final Node node)
	{
		this.tree = tree;
		this.node = node;
		this.children = new ArrayList<FullNode>();
		this.path = new ArrayList<Node>();
		this.level = 0;
	}

	public void addNode(final FullNode fullNode)
	{
		fullNode.setLevel(this.getLevel() + 1);
		fullNode.setParent(this);

		var childPath = new ArrayList<Node>(this.getPath());
		childPath.add(fullNode.getNode());
		fullNode.setPath(childPath);

		this.children.add(fullNode);
		this.tree.addNode(fullNode);
	}

	public Node getNode()
	{
		return this.node;
	}

	public long getTreeId()
	{
		return this.tree.getId();
	}

	public long getId()
	{
		return this.node.getId();
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

	public ArrayList<Node> getPath()
	{
		return path;
	}

	public void setPath(ArrayList<Node> path)
	{
		this.path = path;
	}

	public FullNode getParent()
	{
		return parent;
	}

	public void setParent(FullNode parent)
	{
		this.parent = parent;
	}

	public FullTree getFullTree()
	{
		return this.tree;
	}

	public List<FullNode> getChildren()
	{
		return Collections.unmodifiableList(this.children);
	}

	@Override
	public String toString()
	{
		// @formatter:off
		final var result = MoreObjects.toStringHelper(this)
				.add("id", this.getId())
				.add("treeId", this.getTreeId())
				.add("name", this.getNode().getName())
				.toString();
		return result;
		// @formatter:on
	}

}
