package de.esg.treedemo.treemgmt.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import de.esg.treedemo.treemgmt.DataCreator;

@Tag("unit-test")
@DisplayName("Pure Unit-Test 'FullTree'")
@TestInstance(Lifecycle.PER_CLASS)
public class FullTreeUnitTest
{
	@Test
	public void createOneNodeTree()
	{
		final Node rootNode = new Node(0, "Rootknoten");
		final FullTree objUnderTest = new FullTree("Baum 1", rootNode);

		assertThat(objUnderTest.getLevelList(0)).hasSize(1);
		assertThat(objUnderTest.getLevelList(0)).contains(objUnderTest.getRootNode());
		assertThat(objUnderTest.getRootNode().getFullTree()).isEqualTo(objUnderTest);
		assertThat(objUnderTest.getRootNode().getName()).isEqualTo("Rootknoten");
		assertThat(objUnderTest.getRootNode().getLevel()).isEqualTo(0);
	}

	@Test
	public void createTreeLevel2()
	{
		final long maxLevel = 2;
		final long cntChildPerNode = 3;
		final FullTree objUnderTest = DataCreator.generateDummyTree("Testbaum", maxLevel, cntChildPerNode);
		final FullNode root = objUnderTest.getRootNode();

		assertThat(objUnderTest.getChildNodes()).hasSize(12);
		assertThat(objUnderTest.getRootNode().getChildren()).hasSize(3);

		assertThat(objUnderTest.getRootNode().getLevel()).isEqualTo(0);
		assertThat(objUnderTest.getLevelList(0)).hasSize(1);
		assertThat(objUnderTest.getLevelList(1)).hasSize(3);
		assertThat(objUnderTest.getLevelList(2)).hasSize(9);

	}
}
