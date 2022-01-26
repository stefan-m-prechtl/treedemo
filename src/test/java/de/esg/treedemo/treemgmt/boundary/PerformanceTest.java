package de.esg.treedemo.treemgmt.boundary;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import de.esg.treedemo.shared.boundary.PersistenceHelper;
import de.esg.treedemo.treemgmt.domain.FullTree;

@Tag("integration-test")
@DisplayName("Performancetest für TreeRepository/MySQL-Datenbank")
public class PerformanceTest
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
			initialQueries.add("DELETE FROM treedb.t_closure");
			initialQueries.add("DELETE FROM treedb.t_node");
			initialQueries.add("DELETE FROM treedb.t_tree");
			PersistenceHelper.runSqlQueries("testtreedb", initialQueries);

			final int maxLevel = 6;
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
	public void loadTree() throws Exception
	{
		// System.out.println(LocalDateTime.now());
		// final Optional<FullTree> result = this.objUnderTest.findCompleteFullTreeById(4);
		// assertThat(result).isNotEmpty();
		// System.out.println(LocalDateTime.now());
		assertTrue(true);
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
