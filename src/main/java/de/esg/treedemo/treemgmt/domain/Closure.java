package de.esg.treedemo.treemgmt.domain;

import java.util.Arrays;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;

import de.esg.treedemo.treemgmt.boundary.Constants;

@Entity
@Table(name = Constants.TableClosure, schema = Constants.Schema)
public class Closure
{
	@Id
	@Column(name = "tree_id")
	private long treeId;

	@Id
	@Column(name = "ancestor_id")
	private long ancestorId;

	@Id
	@Column(name = "descendant_id")
	private long descendantId;

	@Column(name = "depth")
	private long depth;

	public Closure()
	{
		// JPA, JSONB benötigen Default-Konstruktor
	}

	public Closure(final long ancestorId, final long descendantId, final long treeId, final long depth)
	{
		this.ancestorId = ancestorId;
		this.descendantId = descendantId;
		this.treeId = treeId;
		this.depth = depth;
	}

	public long getTreeId()
	{
		return treeId;
	}

	public void setTreeId(long treeId)
	{
		this.treeId = treeId;
	}

	public long getAncestorId()
	{
		return ancestorId;
	}

	public void setAncestorId(long ancestorId)
	{
		this.ancestorId = ancestorId;
	}

	public long getDescendantId()
	{
		return descendantId;
	}

	public void setDescendantId(long descendantId)
	{
		this.descendantId = descendantId;
	}

	public long getDepth()
	{
		return depth;
	}

	public void setDepth(long depth)
	{
		this.depth = depth;
	}

	@Override
	public String toString()
	{
		// @formatter:off
		final var result = MoreObjects.toStringHelper(this)
				.add("ancestorId",this.ancestorId)
				.add("descendantId",this.descendantId)
				.add("treeId",this.treeId)
				.add("level",this.depth)
				.toString();
		return result;
		// @formatter:on
	}

	@Override
	public int hashCode()
	{
		final var values = Arrays.asList(this.ancestorId, this.descendantId);
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
		final var other = (Closure) obj;

		return (this.getAncestorId() == other.getAncestorId()) && (this.getDescendantId() == other.getDescendantId());
	}

}
