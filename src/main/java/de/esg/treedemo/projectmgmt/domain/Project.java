package de.esg.treedemo.projectmgmt.domain;

import java.util.Arrays;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import de.esg.treedemo.treemgmt.boundary.Constants;

@Entity
@Table(name = Constants.TableProject, schema = Constants.Schema)
public class Project
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank
	@Column(name = "projectname")
	private String name;

	public Project()
	{
		// JPA, JSONB benötigen Default-Konstruktor
		this.id = -1L;
	}

	public Project(final String name)
	{
		this();
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

	@Override
	public int hashCode()
	{
		final var values = Arrays.asList(this.name);
		return Objects.hashCode(values);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if ((obj == null) || (this.getClass() != obj.getClass()))
		{
			return false;
		}
		if (obj == this)
		{
			return true;
		}
		final var other = (Project) obj;

		if ((this.id > -1) && (other.id > -1))
		{
			return (this.id == other.id);
		}

		return this.getName().equals(other.getName());
	}

}
