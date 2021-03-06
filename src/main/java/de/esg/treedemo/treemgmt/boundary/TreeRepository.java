package de.esg.treedemo.treemgmt.boundary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Path;

import de.esg.treedemo.treemgmt.domain.Closure;
import de.esg.treedemo.treemgmt.domain.FullNode;
import de.esg.treedemo.treemgmt.domain.FullTree;
import de.esg.treedemo.treemgmt.domain.Node;
import de.esg.treedemo.treemgmt.domain.Relation;
import de.esg.treedemo.treemgmt.domain.Tree;
import de.esg.treedemo.treemgmt.domain.TreeInfo;

@Stateless(description = "Respository für Domänenklassen")
@Path(Constants.pathTree)
public class TreeRepository
{
	@PersistenceContext(unitName = Constants.PersistenceContext)
	EntityManager em;

	/**
	 * Baum laden: Rootknoten und seine Kinder.
	 *
	 * @param treeId
	 * @return
	 * @throws Exception
	 */
	public Optional<FullTree> findFullTreeById(final long treeId) throws Exception
	{
		Optional<FullTree> resultTree = Optional.empty();
		Optional<FullNode> resultRootNode = Optional.empty();

		resultRootNode = this.findFullNodeWithChildren(treeId, 0L);
		if (resultRootNode.isPresent())
		{
			// Baum aus RootNode holen
			final var rootNode = resultRootNode.get();
			final FullTree fullTree = rootNode.getFullTree();
			// Rootknoten in Ergebnisbaum eintragen
			fullTree.setRootNode(rootNode);

			resultTree = Optional.of(fullTree);
		}
		return resultTree;
	}

	/**
	 * Knoten eines Baumes mit seinen Kindern laden
	 *
	 * @param treeId
	 * @param nodeId
	 * @return
	 */
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
				final var rootNode = nodes.remove(0);
				final var rootFullNode = new FullNode(fullTree, rootNode);

				nodes.forEach(node -> {
					final var fullNode = new FullNode(fullTree, node);
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

		// var sqlQuery = "SELECT * FROM treedb.t_node n where (n.id in (select child_id from treedb.t_relation where parent_id=#nodeID and tree_id=#treeID) and
		// n.tree_id=#treeID) or (n.id=#nodeID and n.tree_id=#treeID) order by n.id";
		var sqlQuery = "SELECT * FROM treedb.t_node n where (n.id=#nodeID and n.tree_id=#treeID)union all SELECT * FROM treedb.t_node n where (n.tree_id=#treeID and (n.id in (select child_id from treedb.t_relation r where r.parent_id=#nodeID and r.tree_id=#treeID)))	order by id";

		sqlQuery = sqlQuery.replaceAll("#treeID", Long.toString(treeId));
		sqlQuery = sqlQuery.replaceAll("#nodeID", Long.toString(nodeId));

		result = this.em.createNativeQuery(sqlQuery, Node.class).getResultList();

		return result;
	}

	public Optional<FullTree> findCompleteFullTreeById(final long id) throws Exception
	{
		Optional<FullTree> result = Optional.empty();

		final Optional<Tree> resultFindTree = this.findTreeById(id);

		if (resultFindTree.isPresent())
		{
			// Leeren Baum erzeugen
			final FullTree fullTree = new FullTree(resultFindTree.get());

			// Alle DB-Knoten laden
			final Stream<Node> nodes = this.loadNodesForTree(id);
			// DB-Knoten in Business-Knoten umwandeln
			final Map<Long, FullNode> mapFullNodes = new HashMap<Long, FullNode>();
			nodes.forEach(node -> {
				final FullNode fullNode = new FullNode(fullTree, node);
				mapFullNodes.put(node.getId(), fullNode);
			});
			// Alle DB-Beziehungen laden
			final Stream<Relation> relations = this.loadRelationsForTree(id);
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
		final TypedQuery<Tree> qry = this.em.createNamedQuery(Constants.TreeSelectAll, Tree.class);
		result = qry.getResultList();
		return result;
	}

	public List<TreeInfo> loadAllTreeInfos()
	{
		List<TreeInfo> result = new ArrayList<>();
		final TypedQuery<TreeInfo> qry = this.em.createNamedQuery(Constants.TreeViewSelectAll, TreeInfo.class);
		result = qry.getResultList();
		return result;
	}

	public Optional<Tree> findTreeById(final long id)
	{
		final var entity = this.em.find(Tree.class, id);
		final var result = Optional.ofNullable(entity);
		return result;
	}

	Stream<Node> loadNodesForTree(final long treeid)
	{
		final TypedQuery<Node> qry = this.em.createNamedQuery(Constants.NodeSelectByTreeId, Node.class);
		qry.setParameter("treeId", treeid);
		final Stream<Node> result = qry.getResultStream();
		return result;
	}

	Stream<Relation> loadRelationsForTree(final long treeid)
	{

		final TypedQuery<Relation> qry = this.em.createNamedQuery(Constants.RelationSelectByTreeId, Relation.class);
		qry.setParameter("treeId", treeid);
		final Stream<Relation> result = qry.getResultStream();
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

		// 4. Alle Closures speichern
		final Set<Closure> allClosures = new HashSet<>();
		final long treeId = fullTree.getId();
		var rootClosure = new Closure(rootNode.getId(), rootNode.getId(), treeId, 0);
		allClosures.add(rootClosure);

		fullTree.getChildNodes().forEach(fullNode -> {

			// Eigener Verweis mit Tiefe 0
			long ancestorID = fullNode.getId();
			long descendantID = fullNode.getId();
			long depth = 0;
			var closure = new Closure(ancestorID, descendantID, treeId, 0);
			allClosures.add(closure);

			// Verweise der Tiefe 1,2,... erzeugen
			var path = fullNode.getPath();
			for (int idx = path.size() - 1; idx >= 0; idx--)
			{
				ancestorID = path.get(idx).getId();
				closure = new Closure(ancestorID, descendantID, treeId, depth++);
				allClosures.add(closure);
			}
		});

		allClosures.forEach(closure -> {
			this.em.persist(closure);
		});
	}

	private List<Relation> buildRelationForNode(final FullNode fullNode)
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
