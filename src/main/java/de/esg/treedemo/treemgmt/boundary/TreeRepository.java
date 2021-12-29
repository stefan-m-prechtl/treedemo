package de.esg.treedemo.treemgmt.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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

	public Optional<FullTree> findFullTreeById(final long treeId) throws Exception
	{
		Optional<FullTree> resultTree = Optional.empty();
		Optional<FullNode> resultRootNode = Optional.empty();

		resultRootNode = this.findFullNodeWithChildren(treeId, 0L);

		if (resultRootNode.isPresent())
		{
			var rootNode = resultRootNode.get();

			// Baum aus RootNode holen
			final FullTree fullTree = rootNode.getFullTree();
			// Rootknoten in Ergebnisbaum eintragen
			fullTree.setRootNode(rootNode);

			resultTree = Optional.of(fullTree);
		}
		return resultTree;
	}

	public Optional<FullNode> findFullNodeWithChildren(final long treeId, final long nodeId)
	{
		Optional<FullNode> result = Optional.empty();

		final Optional<Tree> resultFindTree = this.findTreeById(treeId);
		if (resultFindTree.isPresent())
		{
			final FullTree fullTree = new FullTree(resultFindTree.get());
			final List<Node> nodes = this.loadNodeWithChildren(treeId, nodeId);

			if (nodes.size() > 0)
			{
				var rootNode = nodes.remove(0);
				var rootFullNode = new FullNode(fullTree, rootNode);

				nodes.forEach(node -> {
					var fullNode = new FullNode(fullTree, node);
					rootFullNode.addNode(fullNode);
				});

				result = Optional.of(rootFullNode);
			}
		}

		return result;
	}

	
	@SuppressWarnings("unchecked")
	private List<Node> loadNodeWithChildren(final long treeId, final long nodeId)
	{
		 List<Node> result = new ArrayList<Node>();

		var sqlQuery = "SELECT * FROM treedb.t_node n where (n.id in (select child_id from treedb.t_relation where parent_id=#nodeID and tree_id=#treeID) and n.tree_id=#treeID) or (n.id=#nodeID and n.tree_id=#treeID) order by n.id";
		sqlQuery = sqlQuery.replaceAll("#treeID", Long.toString(treeId));
		sqlQuery = sqlQuery.replaceAll("#nodeID", Long.toString(nodeId));

		result = (List<Node>)this.em.createNativeQuery(sqlQuery, Node.class).getResultList();

		return result;
	}

	/*
	 * public Optional<FullTree> findFullTreeById(final long id) throws Exception { Optional<FullTree> result = Optional.empty();
	 *
	 * final Optional<Tree> resultFindTree = this.findTreeById(id);
	 *
	 * if (resultFindTree.isPresent()) { // Leeren Baum erzeugen final FullTree fullTree = new FullTree(resultFindTree.get());
	 *
	 * // Alle DB-Knoten laden final List<Node> nodes = this.loadNodesForTree(id); // DB-Knoten in Business-Knoten umwandeln final Map<Long, FullNode> mapFullNodes
	 * = new HashMap<Long, FullNode>(nodes.size()); nodes.forEach(node -> { final FullNode fullNode = new FullNode(fullTree, node); mapFullNodes.put(node.getId(),
	 * fullNode); }); // Alee DB-Beziehungen laden final List<Relation> relations = this.loadRelationsForTree(id); // Parent-/Child Verknüpfungen erzeugen:
	 * relations.forEach(relation -> { final FullNode parent = mapFullNodes.get(relation.getParentId()); parent.setLevel(relation.getLevel()); final FullNode child
	 * = mapFullNodes.get(relation.getChildId()); parent.addNode(child); });
	 *
	 * // Rootknoten in Ergebnisbaum eintragen final FullNode rootFullNode = mapFullNodes.get(0L); fullTree.setRootNode(rootFullNode);
	 *
	 * result = Optional.of(fullTree); } return result; }
	 */

	public List<Tree> loadAllTrees()
	{
		List<Tree> result = new ArrayList<>();
		final TypedQuery<Tree> qry = this.em.createNamedQuery(Constants.TreeSelectAll, Tree.class);
		result = qry.getResultList();
		return result;
	}

	public Optional<Tree> findTreeById(final long id)
	{
		final var entity = this.em.find(Tree.class, id);
		final var result = Optional.ofNullable(entity);
		return result;
	}

	List<Node> loadNodesForTree(final long treeid)
	{
		List<Node> result = new ArrayList<Node>();
		final TypedQuery<Node> qry = this.em.createNamedQuery(Constants.NodeSelectByTreeId, Node.class);
		qry.setParameter("treeId", treeid);
		result = qry.getResultList();
		return result;
	}

	List<Relation> loadRelationsForTree(final long treeid)
	{
		List<Relation> result = new ArrayList<Relation>();
		final TypedQuery<Relation> qry = this.em.createNamedQuery(Constants.RelationSelectByTreeId, Relation.class);
		qry.setParameter("treeId", treeid);
		result = qry.getResultList();
		return result;
	}

	public long getIDFromRootNodeForTree(final long treeid)
	{
		long result = -1;
		final TypedQuery<Relation> qry = this.em.createNamedQuery(Constants.RelationSelectLevelZeroByTreeId, Relation.class);
		qry.setParameter("treeId", treeid);
		final Relation relation = qry.getResultList().get(0);
		result = relation.getParentId();
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
