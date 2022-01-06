package de.esg.treedemo.treemgmt.domain;

import javax.annotation.concurrent.Immutable;
import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;

import de.esg.treedemo.treemgmt.boundary.Constants;

@Entity
@Immutable
@Table(name = Constants.TableView, schema = Constants.Schema)
//@formatter:off
@NamedQueries({
	@NamedQuery(name = Constants.TreeViewSelectAll, query = "SELECT t FROM TreeInfo t")
})
//@formatter:on
public class TreeInfo
{
	TreeInfo()
	{

	}

	@Id
	private long id;
	private String name;
	@JsonbProperty("countNodes")
	private long cntNodes;
	@JsonbProperty("maxlevel")
	private long maxLevel;

	public long getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

	public long getCntNodes()
	{
		return this.cntNodes;
	}

	public long getMaxLevel()
	{
		return this.maxLevel;
	}

	@Override
	public String toString()
	{
		// @formatter:off
		final var result = MoreObjects.toStringHelper(this)
				.add("id", this.getId())
				.add("name", this.name)
				.add("countNodes", this.cntNodes)
				.add("maxLevel", this.maxLevel)
				.toString();
		return result;
		// @formatter:on
	}

}
