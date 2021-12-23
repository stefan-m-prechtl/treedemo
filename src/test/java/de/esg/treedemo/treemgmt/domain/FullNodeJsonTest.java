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
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import de.esg.treedemo.shared.boundary.JsonbContextResolver;

@Tag("unit-test")
@DisplayName("Tests für Json-Konvertierung Node")
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class FullNodeJsonTest
{
	JsonbConfig config;
	Jsonb jsonb;

	FullNode generateFullNode()
	{
		final var node = new Node(1L, "Root");
		final var fullTree = new FullTree("Testbaum", node);
		final var nodeObj = fullTree.getRootNode();
		return nodeObj;

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
		final var nodeObj = this.generateFullNode();

		// act
		final var nodeJson = this.jsonb.toJson(nodeObj);
		System.out.println(nodeJson);

		// assert
	//@formatter:off
	assertAll("User As Json",
	  () -> assertThat(nodeJson).isNotNull(),
	  () -> assertThat(nodeJson).isNotEmpty(),
	  () -> assertThat(nodeJson).contains("id"),
	  () -> assertThat(nodeJson).contains("name")
	 );
	//@formatter:on
	}

	@Test
	@Order(2)
	void convertFromJson()
	{
		// prepare
		final var nodeObj = this.generateFullNode();
		final var nodeJson = this.jsonb.toJson(nodeObj);

		// act
		final var nodeObjFromJson = this.jsonb.fromJson(nodeJson, FullNode.class);

		// assert
		//@formatter:off
		assertAll("User from Json",
				() ->	assertThat(nodeObjFromJson).isNotNull(),
				() ->	assertThat(nodeObjFromJson.getName()).isEqualTo(nodeObj.getName())
		);
		//	@formatter:off
	}
}
