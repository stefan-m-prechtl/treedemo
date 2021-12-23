package de.esg.treedemo.treemgmt.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import de.esg.treedemo.treemgmt.DataCreator;
import de.esg.treedemo.treemgmt.domain.FullTree;
import de.esg.treedemo.treemgmt.domain.Node;
import de.esg.treedemo.treemgmt.domain.Relation;
import de.esg.treedemo.treemgmt.domain.Tree;

@TestInstance(Lifecycle.PER_CLASS)
public class TreeRepositoryTest
{
	private TreeRepository objUnderTest;
	private EntityManager em;
	private EntityTransaction tx;

	private final long treeId = 7;

	@BeforeEach
	void setUpEach()
	{
		final var factory = Persistence.createEntityManagerFactory("testtreedb");
		this.em = factory.createEntityManager();
		this.tx = this.em.getTransaction();

		this.objUnderTest = new TreeRepository();
		this.objUnderTest.em = this.em;
	}

	@AfterEach
	void tearDownEach()
	{
		this.em.clear();
		this.em.close();
	}

	@Test
	public void findTreeById()
	{
		final Optional<Tree> result = this.objUnderTest.findTreeById(this.treeId);
		assertThat(result).isPresent();
	}

	@Test
	public void loadNodesForTree()
	{
		final List<Node> result = this.objUnderTest.loadNodesForTree(this.treeId);
		assertThat(result).isNotEmpty();
		assertThat(result.size()).isGreaterThan(0);
	}

	@Test
	public void loadRelationsForTree()
	{
		final List<Relation> result = this.objUnderTest.loadRelationsForTree(this.treeId);
		assertThat(result).isNotEmpty();
		assertThat(result.size()).isGreaterThan(0);
	}

	@Test
	public void findFullTreeById() throws Exception
	{
		final Optional<FullTree> result = this.objUnderTest.findFullTreeById(this.treeId);
		assertThat(result).isNotEmpty();
	}

	@Test
	// @Disabled
	public void saveFullTree()
	{
		final long maxLevel = 5;
		final int cntChildPerNode = 3;
		final FullTree fullTree = DataCreator.generateDummyTree("Testbaum", maxLevel, cntChildPerNode);

		this.tx.begin();
		this.objUnderTest.saveFullTree(fullTree);
		this.tx.commit();

	}
}
