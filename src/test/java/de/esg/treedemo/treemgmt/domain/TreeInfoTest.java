package de.esg.treedemo.treemgmt.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import de.esg.treedemo.treemgmt.boundary.Constants;

@Tag("integration-test")
@DisplayName("Integrationstests TreeInno-Entity/MySQL-Datenbank")
public class TreeInfoTest
{
	private final String jpaContext = "testtreedb";

	private EntityManager em;

	private TreeInfo objUnderTest;
	private long id = -1;

	@BeforeEach
	public void setUpEach()
	{
		// JPA-Umgebung initialisieren
		final var factory = Persistence.createEntityManagerFactory(this.jpaContext);
		this.em = factory.createEntityManager();
	}

	@AfterEach
	public void tearDownEach() throws Exception
	{
		this.em.clear();
		this.em.close();
	}

	@Test
	@DisplayName("Read entity")
	@Tag("integration-test")
	public void readAll()
	{
		// act
		List<TreeInfo> result = new ArrayList<>();
		final TypedQuery<TreeInfo> qry = this.em.createNamedQuery(Constants.TreeViewSelectAll, TreeInfo.class);
		result = qry.getResultList();

		// assert
		assertThat(result).isNotNull();
	}

}
