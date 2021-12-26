package de.esg.treedemo.treemgmt;

import de.esg.treedemo.treemgmt.domain.FullNode;
import de.esg.treedemo.treemgmt.domain.FullTree;
import de.esg.treedemo.treemgmt.domain.Node;

public class DataCreator
{
	private static long idx = 0;

	private DataCreator()
	{
	}

	static public FullTree generateDummyTree(final String treeName, final long maxLevel, final long cntChildPerNode)
	{
		final Node rootNode = new Node(idx++, "K1");
		final FullTree fullTree = new FullTree(treeName, rootNode);
		for (int i = 0; i < maxLevel; i++)
		{
			System.out.println("Create node for leve:" + i);
			final var currentLevelList = fullTree.getLevelList(i);
			currentLevelList.forEach(fullnode -> {
				for (int c = 0; c < cntChildPerNode; c++)
				{
					final Node childNode = new Node(idx++, fullnode.getName() + "." + String.valueOf(c));
					final FullNode childFullNode = new FullNode(fullTree, childNode);
					fullnode.addNode(childFullNode);
				}
			});
		}
		return fullTree;
	}
}
