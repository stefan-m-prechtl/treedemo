package de.esg.treedemo.treemgmt.boundary;

public final class Constants
{
	private Constants()
	{
		// keine Instanz notwendig
	}

	// REST Resource
	public final static String pathTree = "/treemgmt/fulltree";
	public final static String pathPing = "/ping";
	public final static String pathUser = "/user";
	public final static String PersistenceContext = "treedb";

	// JPA: Schema, Tabelle, Named Queries
	public final static String Schema = "treedb";

	public final static String TableTree = "t_tree";
	public final static String TableView = "v_tree_info";
	public final static String TreeSelectAll = "TreeSelectAll";
	public final static String TreeViewSelectAll = "TreeViewSelectAll";
	public final static String TreeSelectByName = "TreeSelectByName";

	public final static String TableNode = "t_node";
	public final static String NodeSelectAll = "NodeSelectAll";
	public final static String NodeSelectByTreeId = "NodeSelectByTreeId";

	public final static String TableRelation = "t_relation";
	public final static String RelationSelectAll = "RelationSelectAll";
	public final static String RelationSelectByTreeId = "RelationSelectByTreeId";
	public final static String RelationSelectLevelZeroByTreeId = "RelationSelectLevelZeroByTreeId";

	public final static String TableProject = "t_project";

}
