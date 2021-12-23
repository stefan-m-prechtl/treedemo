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

import com.google.common.base.MoreObjects;

import de.esg.treedemo.treemgmt.boundary.Constants;

@Entity
@Table(name = Constants.TableRelation, schema = Constants.Schema)
//@formatter:off
@NamedQueries({
	@NamedQuery(name = Constants.RelationSelectAll, query = "SELECT r FROM Relation r"),
	@NamedQuery(name = Constants.RelationSelectByTreeId, query = "SELECT r FROM Relation r WHERE r.treeId = :treeId"),
	@NamedQuery(name = Constants.RelationSelectLevelZeroByTreeId, query = "SELECT r FROM Relation r where r.treeId = :treeId and r.level = 0")
})
//@formatter:on
public class Relation implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "tree_id")
	private long treeId;

	@Id
	@Column(name = "parent_id")
	private long parentId;

	@Id
	@Column(name = "child_id")
	private long childId;

	@Column(name = "treelevel")
	private long level;

	public Relation()
	{
		// JPA, JSONB benötigen Default-Konstruktor
	}

	public Relation(final long parentId, final long childId, final long treeId, final long level)
	{
		this.parentId = parentId;
		this.childId = childId;
		this.treeId = treeId;
		this.level = level;
	}

	public long getParentId()
	{
		return this.parentId;
	}

	public void setParentId(final long parentId)
	{
		this.parentId = parentId;
	}

	public long getChildId()
	{
		return this.childId;
	}

	public void setChildId(final long childId)
	{
		this.childId = childId;
	}

	public long getTreeId()
	{
		return this.treeId;
	}

	public void setTreeId(final long treeId)
	{
		this.treeId = treeId;
	}

	public long getLevel()
	{
		return this.level;
	}

	public void setLevel(final long level)
	{
		this.level = level;
	}

	@Override
	public String toString()
	{
		// @formatter:off
		final var result = MoreObjects.toStringHelper(this)
				.add("parentId",this.parentId)
				.add("childId",this.childId)
				.add("treeId",this.treeId)
				.add("level",this.level)
				.toString();
		return result;
		// @formatter:on
	}

	@Override
	public int hashCode()
	{
		final var values = Arrays.asList(this.parentId, this.childId);
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
		final var other = (Relation) obj;

		return (this.getParentId() == other.getParentId()) && (this.getChildId() == other.getChildId());
	}

}
