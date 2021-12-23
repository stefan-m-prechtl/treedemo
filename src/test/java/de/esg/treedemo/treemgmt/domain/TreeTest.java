package de.esg.treedemo.treemgmt.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import de.esg.treedemo.shared.boundary.PersistenceHelper;

@Tag("integration-test")
@DisplayName("Integrationstests Tree-Entity/MySQL-Datenbank")
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TreeTest
{
	private final static String jpaContext = "testtreedb";

	private static EntityManager em;
	private static EntityTransaction tx;

	private Tree objUnderTest;
	private long id = -1;

	@BeforeAll
	public static void setUpAll()
	{
		// JPA-Umgebung initialisieren
		final var factory = Persistence.createEntityManagerFactory(jpaContext);
		em = factory.createEntityManager();
		tx = em.getTransaction();

		// Alle Daten in DB löschen, evt. Initialdaten erzeugen
		final List<String> initialQueries = new ArrayList<String>();
		initialQueries.add("DELETE FROM treedb.t_tree");
		PersistenceHelper.runSqlQueries(jpaContext, initialQueries);

	}

	@AfterAll
	public static void tearDownAll() throws Exception
	{
		// Alle Daten in DB löschen, evt. Initialdaten erzeugen
		final List<String> finalQueries = new ArrayList<String>();
		finalQueries.add("DELETE FROM treedb.t_tree");
		PersistenceHelper.runSqlQueries(jpaContext, finalQueries);

		em.clear();
		em.close();
	}

	@Test
	@DisplayName("Create entity")
	@Order(1)
	@Tag("integration-test")
	public void create()
	{
		this.objUnderTest = this.createObjUnderTest();

		// act
		tx.begin();
		em.persist(this.objUnderTest);
		tx.commit();
		// assert
		assertThat(this.objUnderTest.getId()).isGreaterThan(-1);

		// ID intern für spätere Tests vermerken
		this.id = this.objUnderTest.getId();
	}

	@Test
	@DisplayName("Read entity")
	@Order(2)
	@Tag("integration-test")
	public void read()
	{
		// act
		final var readEntity = em.find(Tree.class, this.id);
		// assert
		assertThat(readEntity).isNotNull();
		assertThat(readEntity.getId()).isEqualTo(this.id);
	}

	@Test
	@DisplayName("Update entity")
	@Order(3)
	@Tag("integration-test")
	public void update()
	{
		// act
		tx.begin();
		final var updatedEntity = em.merge(this.updateObjUnderTest(this.objUnderTest));
		tx.commit();
		// assert
		assertThat(updatedEntity).isNotNull();
		assertThat(updatedEntity.getId()).isEqualTo(this.objUnderTest.getId());
	}

	@Test
	@DisplayName("Delete entity")
	@Order(4)
	@Tag("integration-test")
	public void delete()
	{
		// act
		tx.begin();
		var entity = em.merge(this.objUnderTest);
		em.remove(entity);
		tx.commit();

		// assert
		final var readEntity = em.find(Tree.class, this.id);
		assertThat(readEntity).isNull();

	}

	protected Tree createObjUnderTest()
	{
		final var objUnderTest = new Tree("Baum 1");
		return objUnderTest;
	}

	protected Tree updateObjUnderTest(final Tree entity)
	{
		final var updatedObject = entity;
		updatedObject.setName("Baum Eins");

		return updatedObject;
	}
}
