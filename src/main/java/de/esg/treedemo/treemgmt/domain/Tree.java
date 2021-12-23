package de.esg.treedemo.treemgmt.domain;

import java.util.Arrays;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.google.common.base.MoreObjects;

import de.esg.treedemo.treemgmt.boundary.Constants;

@Entity
@Table(name = Constants.TableTree, schema = Constants.Schema)
//@formatter:off
@NamedQueries({
	@NamedQuery(name = Constants.TreeSelectAll, query = "SELECT t FROM Tree t"),
	@NamedQuery(name = Constants.TreeSelectByName, query = "SELECT t FROM Tree t WHERE t.name = :name")
})
//@formatter:on
public class Tree
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank
	@Column(name = "treename")
	private String name;

	public Tree()
	{
		// JPA, JSONB benötigen Default-Konstruktor
		this.id = -1L;
	}

	public Tree(final String name)
	{
		this.id = -1L;
		this.name = name;
	}

	public long getId()
	{
		return this.id;
	}

	public void setId(long id)
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

	@Override
	public String toString()
	{
		// @formatter:off
		final var result = MoreObjects.toStringHelper(this)
				.add("id",this.getId())
				.add("name",this.name)
				.toString();
		return result;
		// @formatter:on
	}

	@Override
	public int hashCode()
	{
		var values = Arrays.asList(this.name);
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
		final var other = (Tree) obj;

		if ((this.id > -1) && (other.id > -1))
		{
			return (this.id == other.id);
		}

		return this.getName().equals(other.getName());
	}

}
