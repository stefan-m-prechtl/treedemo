package de.esg.treedemo.treemgmt.boundary;

import java.net.URI;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.google.common.base.Strings;

import de.esg.treedemo.shared.boundary.exceptionhandling.ExceptionHandlingInterceptor;
import de.esg.treedemo.treemgmt.domain.FullTree;

@Stateless(description = "REST-Interface")
@Path(Constants.pathTree)
@Interceptors(ExceptionHandlingInterceptor.class)
public class TreeResource
{
	@Context
	protected UriInfo uriInfo;

	TreeRepository repository;

	@Inject
	public TreeResource(final TreeRepository repository)
	{
		this.repository = repository;
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllResources()
	{
		final var allTrees = this.repository.loadAllTreeInfos();
		return Response.ok(allTrees).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTreeResourceById(@PathParam("id") final String resourceId, @QueryParam("all") final String loadAll) throws Exception
	{
		final Optional<FullTree> result;

		final long id = Long.parseLong(resourceId);

		final var loadLazy = Strings.isNullOrEmpty(loadAll) ? true : false;

		if (loadLazy)
		{
			result = this.repository.findFullTreeById(id);
		}
		else
		{
			result = this.repository.findCompleteFullTreeById(id);
		}

		if (result.isPresent())
		{
			return Response.ok(result.get()).build();
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@GET
	@Path("/{treeid}/node/{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodeResourceById(@PathParam("treeid") final String treeId, @PathParam("nodeid") final String nodeId) throws Exception
	{
		final long idTree = Long.parseLong(treeId);
		final long idNode = Long.parseLong(nodeId);

		final var result = this.repository.findFullNodeWithChildren(idTree, idNode);

		if (result.isPresent())
		{
			return Response.ok(result.get()).build();
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDemoTree(@QueryParam("name") final String name, @QueryParam("max") final String maxTreeLevel, @QueryParam("children") final String countChildren)
	{
		// Parameter prüfen
		if (Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(maxTreeLevel) || Strings.isNullOrEmpty(countChildren))
		{
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Unvollständige Parameter").build();
		}

		long maxLevel = 0L;
		long cntChildPerNode = 0L;
		try
		{
			maxLevel = Long.parseLong(maxTreeLevel);
			cntChildPerNode = Long.parseLong(countChildren);
		} catch (final NumberFormatException e)
		{
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Numerische Parameter nicht numerisch").build();
		}

		final String treeName = name;

		// Dummy-Tree erzeugen und speichern
		final FullTree generatedTree = DataCreator.generateDummyTree(treeName, maxLevel, cntChildPerNode);
		this.repository.saveFullTree(generatedTree);
		final long treeID = generatedTree.getTree().getId();

		final URI linkURI = UriBuilder.fromUri(this.uriInfo.getAbsolutePath()).path(String.valueOf(treeID)).build();
		final Link link = Link.fromUri(linkURI).rel("self").type(MediaType.APPLICATION_JSON).build();
		return Response.noContent().links(link).build();
	}
}
