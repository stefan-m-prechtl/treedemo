package de.esg.treedemo.treemgmt.boundary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Path;

import de.esg.treedemo.treemgmt.domain.FullNode;
import de.esg.treedemo.treemgmt.domain.FullTree;
import de.esg.treedemo.treemgmt.domain.Node;
import de.esg.treedemo.treemgmt.domain.Relation;
import de.esg.treedemo.treemgmt.domain.Tree;

@Stateless(description = "Respository für Domänenklassen")
@Path(Constants.pathTree)
public class TreeRepository
{
	@PersistenceContext(unitName = Constants.PersistenceContext)
	EntityManager em;

	public TreeRepository()
	{

	}

	public Optional<FullTree> findFullTreeById(final long id) throws Exception
	{
		Optional<FullTree> result = Optional.empty();

		final Optional<Tree> resultFindTree = this.findTreeById(id);

		if (resultFindTree.isPresent())
		{
			// Leeren Baum erzeugen
			final FullTree fullTree = new FullTree(resultFindTree.get());

			// Alle Knoten/Beziehungen laden
			final List<Node> nodes = this.loadNodesForTree(id);
			final Map<Long, FullNode> mapFullNodes = new HashMap<Long, FullNode>(nodes.size());
			nodes.forEach(node -> {
				final FullNode fullNode = new FullNode(fullTree, node);
				mapFullNodes.put(node.getId(), fullNode);
			});

			final List<Relation> relations = this.loadRelationsForTree(id);
			// Parent-/Child Verknüpfungen erzeugen:
			relations.forEach(relation -> {
				final FullNode parent = mapFullNodes.get(relation.getParentId());
				parent.setLevel(relation.getLevel());
				final FullNode child = mapFullNodes.get(relation.getChildId());
				parent.addNode(child);
			});

			// Rootknoten in Ergebnisbaum eintragen
			final FullNode rootFullNode = mapFullNodes.get(0L);
			fullTree.setRootNode(rootFullNode);

			result = Optional.of(fullTree);
		}
		return result;
	}

	public List<Tree> loadAllTrees()
	{
		List<Tree> result = new ArrayList<>();
		try
		{
			final TypedQuery<Tree> qry = this.em.createNamedQuery(Constants.TreeSelectAll, Tree.class);
			result = qry.getResultList();
		}
		// kein Ergebnis
		catch (final NoResultException e)
		{
			// nichts zu tun: dann wird leere Liste geliefert!
		}
		return result;

	}

	public Optional<Tree> findTreeById(final long id)
	{
		final var entity = this.em.find(Tree.class, id);
		final var result = Optional.ofNullable(entity);
		return result;
	}

	public List<Node> loadNodesForTree(final long treeid)
	{
		List<Node> result = new ArrayList<Node>();
		try
		{
			final TypedQuery<Node> qry = this.em.createNamedQuery(Constants.NodeSelectByTreeId, Node.class);
			qry.setParameter("treeId", treeid);
			result = qry.getResultList();

		}
		// kein Ergebnis
		catch (final NoResultException e)
		{
			// nichts zu tun: dann wird leere Liste geliefert!
		}
		return result;
	}

	public List<Relation> loadRelationsForTree(final long treeid)
	{
		List<Relation> result = new ArrayList<Relation>();
		try
		{
			final TypedQuery<Relation> qry = this.em.createNamedQuery(Constants.RelationSelectByTreeId, Relation.class);
			qry.setParameter("treeId", treeid);
			result = qry.getResultList();

		}
		// kein Ergebnis
		catch (final NoResultException e)
		{
			// nichts zu tun: dann wird leere Liste geliefert!
		}
		return result;
	}

	public long getIDFromRootNodeForTree(final long treeid)
	{
		long result = -1;
		try
		{
			final TypedQuery<Relation> qry = this.em.createNamedQuery(Constants.RelationSelectLevelZeroByTreeId, Relation.class);
			qry.setParameter("treeId", treeid);
			final Relation relation = qry.getResultList().get(0);
			result = relation.getParentId();
		}
		// kein Ergebnis
		catch (final NoResultException e)
		{
			// nichts zu tun
		}
		return result;
	}

	public void saveFullTree(final FullTree fullTree)
	{
		final Tree tree = fullTree.getTree();

		// 1. Baum in DB speichern
		this.em.persist(tree);
		this.em.flush();

		// 2. Alle Knoten speichern
		final Node rootNode = fullTree.getRootNode().getNode();
		rootNode.setTreeId(tree.getId());
		this.em.persist(rootNode);

		fullTree.getChildNodes().forEach(fullNode -> {
			final Node node = fullNode.getNode();
			node.setTreeId(tree.getId());
			this.em.persist(node);
		});
		this.em.flush();

		// 3. Alle Relationen speichern
		final List<Relation> allRelations = new ArrayList<Relation>();
		final var rootRelation = this.buildRelationForNode(fullTree.getRootNode());
		allRelations.addAll(rootRelation);

		fullTree.getChildNodes().forEach(fullNode -> {
			final var relation = this.buildRelationForNode(fullNode);
			allRelations.addAll(relation);
		});

		final int size = allRelations.size();
		allRelations.forEach(relation -> {
			this.em.persist(relation);
		});
	}

	public List<Relation> buildRelationForNode(final FullNode fullNode)
	{
		final List<Relation> result = new ArrayList<Relation>();

		final long level = fullNode.getLevel();
		final long treeId = fullNode.getFullTree().getTree().getId();
		final long parentId = fullNode.getNode().getId();

		fullNode.getChildren().forEach(childNode -> {
			final long childId = childNode.getNode().getId();
			final var relation = new Relation(parentId, childId, treeId, level);
			result.add(relation);
		});

		return result;
	}
}
