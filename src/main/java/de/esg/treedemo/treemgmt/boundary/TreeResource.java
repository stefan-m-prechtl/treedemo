package de.esg.treedemo.treemgmt.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless(description = "REST-Interface für FullTree")
@Path(Constants.pathTree)
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
		final var allTrees = this.repository.loadAllTrees();
		return Response.ok(allTrees).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTreeResourceById(@PathParam("id") final String resourceId) throws Exception
	{
		final long id = Long.parseLong(resourceId);

		final var result = this.repository.findFullTreeById(id);

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
}
