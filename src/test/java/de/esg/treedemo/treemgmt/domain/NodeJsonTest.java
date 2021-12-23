package de.esg.treedemo.treemgmt.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

@Tag("unit-test")
@DisplayName("Tests für Json-Konvertierung Node")
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class NodeJsonTest
{
	Jsonb jsonb;

	Node generateNode()
	{
		final var nodeObj = new Node(2, "Testknoten");
		return nodeObj;

	}

	@BeforeEach
	void initJson()
	{
		this.jsonb = JsonbBuilder.create();
	}

	@Test
	@Order(1)
	void convertToJson()
	{
		// prepare
		final var nodeObj = this.generateNode();

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
		final var nodeObj = this.generateNode();
		final var nodeJson = this.jsonb.toJson(nodeObj);

		// act
		final var nodeObjFromJson = this.jsonb.fromJson(nodeJson, Node.class);

		// assert
		//@formatter:off
		assertAll("User from Json",
		  () ->	assertThat(nodeObjFromJson).isNotNull(),
		  () ->	assertThat(nodeObjFromJson).isEqualTo(nodeObj),
		  () ->	assertThat(nodeObjFromJson.getName()).isEqualTo(nodeObj.getName())
		);
		//@formatter:off
	}
}
