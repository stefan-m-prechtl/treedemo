package de.esg.treedemo.treemgmt.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.json.Json;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.esg.treedemo.shared.boundary.JsonbContextResolver;

@Tag("unit-test")
@DisplayName("Tests für Json-Konvertierung FullTree")
@TestMethodOrder(OrderAnnotation.class)
public class FullTreeJsonTest
{
	JsonbConfig config;
	Jsonb jsonb;

	FullTree generateFullTree()
	{
		final var node = new Node(1L, "Root");
		final var fullTree = new FullTree("Testbaum", node);
		fullTree.getRootNode().addNode(new FullNode(fullTree, new Node(2L, "Kind1")));
		fullTree.getRootNode().addNode(new FullNode(fullTree, new Node(3L, "Kind2")));

		return fullTree;

	}

	@BeforeEach
	void initJsonbConfig()
	{
		final var jcr = new JsonbContextResolver();
		this.jsonb = jcr.getContext(Json.class);
	}

	@Test
	@Order(1)
	void convertToJson()
	{
		// prepare
		final var treeObj = this.generateFullTree();

		// act
		final var treeJson = this.jsonb.toJson(treeObj);
		System.out.println(treeJson);

		// assert
	//@formatter:off
	assertAll("FullTree As Json",
	  () -> assertThat(treeJson).isNotNull(),
	  () -> assertThat(treeJson).isNotEmpty(),
	  () -> assertThat(treeJson).contains("id"),
	  () -> assertThat(treeJson).contains("name")
	 );
	//@formatter:on
	}

	@Test
	@Order(2)
	void convertFromJson()
	{
		// prepare
		final var treeObj = this.generateFullTree();
		final var treeJson = this.jsonb.toJson(treeObj);

		// act
		final var treeObjFromJson = this.jsonb.fromJson(treeJson, FullTree.class);

		// assert
		//@formatter:off
		assertAll("User from Json",
				() ->	assertThat(treeObjFromJson).isNotNull(),
				() ->	assertThat(treeObjFromJson.getName()).isEqualTo(treeObj.getName())
		);
		//	@formatter:off
	}
}
