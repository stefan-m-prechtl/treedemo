package de.esg.treedemo.treemgmt.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import de.esg.treedemo.shared.boundary.PersistenceHelper;
import de.esg.treedemo.treemgmt.domain.FullTree;
import de.esg.treedemo.treemgmt.domain.Node;
import de.esg.treedemo.treemgmt.domain.Relation;
import de.esg.treedemo.treemgmt.domain.Tree;
import de.esg.treedemo.treemgmt.domain.TreeInfo;

@Tag("integration-test")
@DisplayName("Integrationstests TreeRepository/MySQL-Datenbank")
@TestInstance(Lifecycle.PER_CLASS)
public class TreeRepositoryTest
{
	private TreeRepository objUnderTest;
	private EntityManager em;
	private EntityTransaction tx;

	private static boolean treeIsNotYetCreated = true;
	private long treeId = -1;

	@BeforeEach
	void setUpEach()
	{
		final var factory = Persistence.createEntityManagerFactory("testtreedb");
		this.em = factory.createEntityManager();
		this.tx = this.em.getTransaction();

		this.objUnderTest = new TreeRepository();
		this.objUnderTest.em = this.em;

		if (treeIsNotYetCreated)
		{
			// Alle Daten in DB löschen, evt. Initialdaten erzeugen
			final List<String> initialQueries = new ArrayList<String>();
			initialQueries.add("DELETE FROM treedb.t_relation");
			initialQueries.add("DELETE FROM treedb.t_node");
			initialQueries.add("DELETE FROM treedb.t_tree");
			PersistenceHelper.runSqlQueries("testtreedb", initialQueries);

			final int maxLevel = 5;
			final int cntChildren = 3;
			this.treeId = this.saveFullTree(this.objUnderTest, maxLevel, cntChildren);
			treeIsNotYetCreated = false;
		}
	}

	@AfterEach
	void tearDownEach()
	{
		this.em.clear();
		this.em.close();
	}

	@Test
	public void loadAllTreeInfos()
	{
		final List<TreeInfo> result = this.objUnderTest.loadAllTreeInfos();
		assertThat(result).isNotEmpty();
		assertThat(result.size()).isGreaterThan(0);
	}

	@Test
	public void findTreeById()
	{
		final Optional<Tree> result = this.objUnderTest.findTreeById(this.treeId);
		assertThat(result).isPresent();
	}

	@Test
	public void findFullNodeWithChildren()
	{
		final var result = this.objUnderTest.findFullNodeWithChildren(this.treeId, 0L);
		assertThat(result).isPresent();
		assertThat(result.get().getNode().getId()).isEqualTo(0L);
		assertThat(result.get().getFullTree().getTree().getId()).isEqualTo(this.treeId);
		assertThat(result.get().getChildren()).isNotEmpty();
	}

	@Test
	public void loadNodesForTree()
	{
		final Stream<Node> result = this.objUnderTest.loadNodesForTree(this.treeId);
		assertThat(result.findAny().isPresent()).isTrue();
	}

	@Test
	public void loadRelationsForTree()
	{
		final Stream<Relation> result = this.objUnderTest.loadRelationsForTree(this.treeId);
		assertThat(result.findAny().isPresent()).isTrue();
	}

	@Test
	public void findFullTreeById() throws Exception
	{
		final Optional<FullTree> result = this.objUnderTest.findFullTreeById(this.treeId);
		assertThat(result).isNotEmpty();
	}

	@Test
	public void findCompleteFullTreeById() throws Exception
	{
		final Optional<FullTree> result = this.objUnderTest.findCompleteFullTreeById(this.treeId);
		assertThat(result).isNotEmpty();
	}

	private long saveFullTree(final TreeRepository repository, final long maxLevel, final int cntChildPerNode)
	{
		final FullTree fullTree = DataCreator.generateDummyTree("Testbaum", maxLevel, cntChildPerNode);
		this.tx.begin();
		repository.saveFullTree(fullTree);
		this.tx.commit();
		return fullTree.getTree().getId();
	}
}
