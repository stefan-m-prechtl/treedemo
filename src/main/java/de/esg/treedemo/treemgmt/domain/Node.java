package de.esg.treedemo.treemgmt.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.google.common.base.MoreObjects;

import de.esg.treedemo.treemgmt.boundary.Constants;

@Entity
@Table(name = Constants.TableNode, schema = Constants.Schema)
//@formatter:off
@NamedQueries({
	@NamedQuery(name = Constants.NodeSelectAll, query = "SELECT n FROM Node n"),
	@NamedQuery(name = Constants.NodeSelectByTreeId, query = "SELECT n FROM Node n WHERE n.treeId = :treeId")
})
//@formatter:on
public class Node implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	@Id
	@Column(name = "tree_id")
	private long treeId;

	@NotBlank
	@Column(name = "nodename")
	private String name;

	public Node()
	{
		// JPA, JSONB benötigen Default-Konstruktor
		this.id = -1;
	}

	public Node(final long id, final String name)
	{
		this.id = id;
		this.name = name;
	}

	public long getId()
	{
		return this.id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(final String name)
	{
		this.name = Objects.requireNonNull(name);
	}

	public long getTreeId()
	{
		return this.treeId;
	}

	public void setTreeId(final long treeId)
	{
		this.treeId = treeId;
	}

	@Override
	public String toString()
	{
		// @formatter:off
		final var result = MoreObjects.toStringHelper(this)
				.add("id", this.getId())
				.add("treeId", this.getTreeId())
				.add("name", this.name)
				.toString();
		return result;
		// @formatter:on
	}

	@Override
	public int hashCode()
	{
		final var values = Arrays.asList(this.name, this.treeId);
		return Objects.hashCode(values);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (obj == this)
		{
			return true;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		final var other = (Node) obj;

		if ((this.id > -1) && (other.id > -1))
		{
			return (this.id == other.id);
		}

		return (this.getName().equals(other.getName())) && (this.getTreeId() == other.getTreeId());
	}

}
