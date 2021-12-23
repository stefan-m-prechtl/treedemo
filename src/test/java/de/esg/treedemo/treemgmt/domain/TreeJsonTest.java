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
@DisplayName("Tests für Json-Konvertierung Tree")
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TreeJsonTest
{
	Jsonb jsonb;

	Tree generateTree()
	{
		final var treeObj = new Tree("Baum 1");
		return treeObj;
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
		final var treeObj = this.generateTree();

		// act
		final var treeJson = this.jsonb.toJson(treeObj);
		System.out.println(treeJson);

		// assert
		//@formatter:off
		assertAll("User As Json",
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
		final var treeObj = this.generateTree();
		final var treeJson = this.jsonb.toJson(treeObj);

		// act
		final var treeObjFromJson = this.jsonb.fromJson(treeJson, Tree.class);

		// assert
		//@formatter:off
		assertAll("User from Json",
		  () ->	assertThat(treeObjFromJson).isNotNull(),
		  () ->	assertThat(treeObjFromJson).isEqualTo(treeObj),
		  () ->	assertThat(treeObjFromJson.getName()).isEqualTo(treeObj.getName())
		);
		//@formatter:off
	}
}
